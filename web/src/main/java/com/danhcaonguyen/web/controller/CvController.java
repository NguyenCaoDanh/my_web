package com.danhcaonguyen.web.controller;
import com.danhcaonguyen.web.exception.ErrorHandler;

import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.exception.ExceptionResponse;
import com.danhcaonguyen.web.generic.GenericController;
import com.danhcaonguyen.web.generic.IService;
import com.danhcaonguyen.web.service.CvService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cv")
public class CvController extends GenericController<Cv, Integer> {
    @Autowired
    private CvService cvService;
    @Override
    public IService<Cv,Integer> getService() {
        return cvService;
    }
    private RequestResponse createResponse(String message, Object data) {
        return new RequestResponse(LocalDateTime.now().toString(), message, data);
    }
     @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdatePersonalInfo(

            @RequestParam("name") String userJson,
            @RequestParam(value = "link", required = false) MultipartFile link) {
        try {
            // Parse JSON từ chuỗi
            ObjectMapper objectMapper = new ObjectMapper();
            Cv cv = objectMapper.readValue(userJson, Cv.class);

            // Xử lý upload avatar nếu có
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
            return ResponseEntity.ok(new RequestResponse("Cv information saved/updated successfully."));
        } catch (ErrorHandler e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ExceptionResponse("An error occurred: " + e.getMessage()));
        }
    }
    @GetMapping("my-cv/{id}")
    public ResponseEntity<?> getCvById1(@PathVariable Integer id) {
        try {
            Cv cv = cvService.findOne(id);
            return ResponseEntity.ok(cv);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

}


