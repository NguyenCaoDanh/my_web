package com.danhcaonguyen.web.controller;

import com.danhcaonguyen.web.dto.response.CvResponse;
import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.service.CvService;
import com.danhcaonguyen.web.service.GeneralService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/cv")
public class CvController {

    @Autowired
    private CvService cvService; // Service xử lý logic liên quan đến CV
    @Autowired
    private GeneralService generalService; // Service xử lý các tác vụ chung

    /**
     * Tạo phản hồi chuẩn với thông báo, dữ liệu và thời gian hiện tại.
     *
     * @param message Thông báo phản hồi.
     * @param data    Dữ liệu phản hồi.
     * @return Đối tượng phản hồi chuẩn.
     */
    private RequestResponse createResponse(String message, Object data) {
        return new RequestResponse(LocalDateTime.now().toString(), message, data);
    }

    /**
     * API để lưu hoặc cập nhật thông tin CV.
     *
     * @param userJson Thông tin CV dưới dạng JSON.
     * @param link     File liên kết (CV).
     * @return Phản hồi chứa thông báo lưu hoặc cập nhật thành công/thất bại.
     */
    @PostMapping("/save")
    public ResponseEntity<RequestResponse> saveOrUpdatePersonalInfo(
            @RequestParam("name") String userJson,
            @RequestParam(value = "link", required = false) MultipartFile link) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Cv cv = objectMapper.readValue(userJson, Cv.class); // Chuyển đổi JSON thành đối tượng `Cv`

            Account currentAccount = generalService.getCurrentAccount(); // Lấy tài khoản hiện tại
            User user = generalService.getAssociatedUser(currentAccount); // Lấy thông tin người dùng liên kết

            // Nếu có file đính kèm, lưu file và thiết lập đường dẫn vào CV
            if (link != null && !link.isEmpty()) {
                String filePath = generalService.saveFile(link, user.getIdUser() + "/cv/");
                cv.setLink(filePath);
            }

            cv.setUser(user); // Gắn User vào CV
            cvService.save(cv); // Lưu thông tin CV

            return ResponseEntity.ok(createResponse("CV information saved/updated successfully.", null));
        } catch (ErrorHandler e) {
            // Xử lý lỗi đã xác định
            return ResponseEntity.status(e.getStatus()).body(createResponse(e.getMessage(), null));
        } catch (Exception e) {
            // Xử lý lỗi không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse("An error occurred: " + e.getMessage(), null));
        }
    }

    /**
     * API để lấy file CV theo ID.
     *
     * @param id ID của CV cần tải.
     * @return File CV với dữ liệu và header thích hợp.
     */
    @GetMapping("/my-cv/{id}")
    public ResponseEntity<?> getCvById(@PathVariable Integer id) {
        try {
            Cv cv = cvService.findOne(id); // Tìm CV theo ID
            Path filePath = generalService.getFullPathFromLink(cv.getLink()); // Lấy đường dẫn file

            generalService.validateFileExists(filePath); // Kiểm tra file có tồn tại không
            GeneralService.FileData fileData = generalService.getFileData(filePath); // Lấy dữ liệu file

            // Trả về file dưới dạng phản hồi
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileData.mimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.fileName() + "\"")
                    .body(fileData.content());
        } catch (ErrorHandler e) {
            return ResponseEntity.status(e.getStatus()).body(createResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse("An error occurred: " + e.getMessage(), null));
        }
    }

    /**
     * API để lấy danh sách tất cả CV của người dùng hiện tại với phân trang.
     *
     * @param pageable Đối tượng phân trang.
     * @return Phản hồi chứa danh sách CV và thông báo.
     */
    @GetMapping("/all")
    public ResponseEntity<RequestResponse> getAllCvs(Pageable pageable) {
        try {
            Page<Cv> cvs = cvService.findAll(pageable); // Lấy danh sách CV với phân trang
            List<CvResponse> cvResponses = cvs.getContent().stream()
                    .map(cv -> new CvResponse(cv.getCvName(), "/api/cv/my-cv/" + cv.getIdCv())) // Tạo đối tượng phản hồi cho mỗi CV
                    .collect(Collectors.toList());

            return ResponseEntity.ok(createResponse("CVs retrieved successfully.", cvResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse("An error occurred: " + e.getMessage(), null));
        }
    }
}
