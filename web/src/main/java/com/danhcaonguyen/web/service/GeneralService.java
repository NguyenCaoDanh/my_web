package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.AccountRepository;
import io.jsonwebtoken.io.IOException;
import lombok.Getter;
import lombok.Setter;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GeneralService {
    @Autowired
    private AccountRepository accountRepository;

    // Lấy tài khoản hiện tại
    public Account getCurrentAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorHandler(HttpStatus.UNAUTHORIZED, "Account not found"));
    }

    // Kiểm tra tài khoản có liên kết với User
    public User getAssociatedUser(Account account) {
        if (account.getUser() == null) {
            throw new ErrorHandler(HttpStatus.BAD_REQUEST, "User not associated with the account");
        }
        return account.getUser();
    }

    // Lưu file vào thư mục
    public String saveFile(MultipartFile file, String subDirectory) throws IOException, java.io.IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/" + subDirectory;

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());
        return "/" + subDirectory + fileName;
    }
    public void validatePassword(String password) {
        PasswordValidator validator = new PasswordValidator(
                List.of(
                        new LengthRule(6, 128), // Độ dài tối thiểu 6 ký tự
                        new CharacterRule(EnglishCharacterData.UpperCase, 1), // Ít nhất 1 chữ in hoa
                        new CharacterRule(EnglishCharacterData.LowerCase, 1), // Ít nhất 1 chữ thường
                        new CharacterRule(EnglishCharacterData.Digit, 1),    // Ít nhất 1 chữ số
                        new CharacterRule(EnglishCharacterData.Special, 1),  // Ít nhất 1 ký tự đặc biệt
                        new WhitespaceRule() // Không chứa khoảng trắng
                )
        );

        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            throw new ErrorHandler(HttpStatus.BAD_REQUEST,
                    String.join(", ", validator.getMessages(result)));
        }
    }
    // Lấy đường dẫn đầy đủ từ file link
    public Path getFullPathFromLink(String link) {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/static" + link;
        return Paths.get(filePath);
    }

    // Kiểm tra file tồn tại
    public void validateFileExists(Path path) {
        if (!Files.exists(path)) {
            throw new ErrorHandler(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    // Đọc nội dung file và xác định MIME type
    public FileData getFileData(Path path) throws IOException, java.io.IOException {
        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        byte[] fileContent = Files.readAllBytes(path);
        return new FileData(mimeType, fileContent, path.getFileName().toString());
    }

    // Inner class chứa thông tin file
    @Getter
    @Setter
    public static class FileData {
        private final String mimeType;
        private final byte[] content;
        private final String fileName;

        public FileData(String mimeType, byte[] content, String fileName) {
            this.mimeType = mimeType;
            this.content = content;
            this.fileName = fileName;
        }

    }
}
