package com.danhcaonguyen.web.controller;

import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.exception.ExceptionResponse;
import com.danhcaonguyen.web.generic.GenericController;
import com.danhcaonguyen.web.generic.IService;
import com.danhcaonguyen.web.service.PersonalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/personal")
public class PersonalController extends GenericController<User, Integer> {
    @Autowired
    private PersonalService personalService;

    @Override
    public IService<User, Integer> getService() {
        return personalService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdatePersonalInfo(
            @RequestParam("user") String userJson,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            // Parse JSON từ chuỗi
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            // Xử lý upload avatar nếu có
            if (avatar != null && !avatar.isEmpty()) {
                String fileName = StringUtils.cleanPath(avatar.getOriginalFilename());
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";

                Path uploadPath = Paths.get(uploadDir);

                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file
                Path filePath = uploadPath.resolve(fileName);
                avatar.transferTo(filePath.toFile());

                // Lưu đường dẫn file vào database
                user.setAvatar("/images/" + fileName);
            }

            // Lưu hoặc cập nhật thông tin
            personalService.save(user);
            return ResponseEntity.ok(new RequestResponse("Personal information saved/updated successfully."));
        } catch (ErrorHandler e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ExceptionResponse("An error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentPersonalInfo() {
        try {
            // Lấy username từ tài khoản hiện tại
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // Lấy thông tin người dùng liên kết với tài khoản hiện tại
            User currentUser = personalService.findAll().next(); // Giả định chỉ có 1 User
            if (currentUser == null) {
                throw new ErrorHandler(HttpStatus.NOT_FOUND, "No personal information found for the current account.");
            }
            return ResponseEntity.ok(currentUser);
        } catch (ErrorHandler e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ExceptionResponse("An error occurred: " + e.getMessage()));
        }
    }
}
