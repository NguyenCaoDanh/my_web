package com.danhcaonguyen.web.controller;

import com.danhcaonguyen.web.dto.response.CvResponse;
import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.service.CvService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    @Autowired
    private CvService cvService;

    private RequestResponse createResponse(String message, Object data) {
        return new RequestResponse(LocalDateTime.now().toString(), message, data);
    }

    @PostMapping("/save")
    public ResponseEntity<RequestResponse> saveOrUpdatePersonalInfo(@RequestParam("name") String userJson,
                                                                    @RequestParam(value = "link", required = false) MultipartFile link) {
        try {
            // Parse JSON từ chuỗi
            ObjectMapper objectMapper = new ObjectMapper();
            Cv cv = objectMapper.readValue(userJson, Cv.class);

            // Xử lý upload file nếu có
            if (link != null && !link.isEmpty()) {
                String fileName = StringUtils.cleanPath(link.getOriginalFilename());
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/cv/";

                Path uploadPath = Paths.get(uploadDir);

                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file
                Path filePath = uploadPath.resolve(fileName);
                link.transferTo(filePath.toFile());

                // Lưu đường dẫn file vào database
                cv.setLink("/cv/" + fileName);
            }

            // Lưu hoặc cập nhật thông tin
            cvService.save(cv);
            return ResponseEntity.ok(createResponse("CV information saved/updated successfully.", null));
        } catch (ErrorHandler e) {
            return ResponseEntity.status(e.getStatus()).body(createResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse("An error occurred: " + e.getMessage(), null));
        }
    }

    @GetMapping("/my-cv/{id}")
    public ResponseEntity<?> getCvById(@PathVariable Integer id) {
        try {
            Cv cv = cvService.findOne(id);

            // Lấy đường dẫn file từ link của CV
            String filePath = System.getProperty("user.dir") + "/src/main/resources/static" + cv.getLink();
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                throw new ErrorHandler(HttpStatus.NOT_FOUND, "File not found");
            }

            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            byte[] fileContent = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                    .body(fileContent);
        } catch (ErrorHandler e) {
            return ResponseEntity.status(e.getStatus()).body(createResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse("An error occurred: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<RequestResponse> getAllCvs(Pageable pageable) {
        try {
            Page<Cv> cvs = cvService.findAll(pageable);
            List<CvResponse> cvResponses = cvs.getContent().stream()
                    .map(cv -> new CvResponse(cv.getCvName(), "/api/cv/my-cv/" + cv.getIdCv()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(createResponse("CVs retrieved successfully.", cvResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse("An error occurred: " + e.getMessage(), null));
        }
    }
}
