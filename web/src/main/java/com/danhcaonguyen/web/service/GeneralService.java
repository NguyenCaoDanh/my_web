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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class GeneralService {

    @Autowired
    private AccountRepository accountRepository;

    // Lấy tài khoản hiện tại từ context bảo mật
    public Account getCurrentAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorHandler(HttpStatus.UNAUTHORIZED, "Account not found"));
    }

    // Kiểm tra xem tài khoản có liên kết với User hay không
    public User getAssociatedUser(Account account) {
        if (account.getUser() == null) {
            throw new ErrorHandler(HttpStatus.BAD_REQUEST, "User not associated with the account");
        }
        return account.getUser();
    }

    // Lưu file vào thư mục chỉ định
    public String saveFile(MultipartFile file, String subDirectory) throws IOException, java.io.IOException {
        // Lấy tên file và làm sạch tên file
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Xác định thư mục upload
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/" + subDirectory;

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Xác định đường dẫn đầy đủ của file
        Path filePath = uploadPath.resolve(fileName);
        // Lưu file vào thư mục
        file.transferTo(filePath.toFile());
        // Trả về đường dẫn tương đối của file
        return "/" + subDirectory + fileName;
    }

    // Xác thực mật khẩu dựa trên các quy tắc bảo mật
    public void validatePassword(String password) {
        PasswordValidator validator = new PasswordValidator(
                List.of(
                        new LengthRule(6, 128), // Độ dài mật khẩu tối thiểu 6 và tối đa 128 ký tự
                        new CharacterRule(EnglishCharacterData.UpperCase, 1), // Ít nhất 1 chữ cái viết hoa
                        new CharacterRule(EnglishCharacterData.LowerCase, 1), // Ít nhất 1 chữ cái viết thường
                        new CharacterRule(EnglishCharacterData.Digit, 1),    // Ít nhất 1 chữ số
                        new CharacterRule(EnglishCharacterData.Special, 1),  // Ít nhất 1 ký tự đặc biệt
                        new WhitespaceRule() // Không chứa khoảng trắng
                )
        );

        // Kiểm tra mật khẩu với các quy tắc
        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            // Ném lỗi nếu mật khẩu không hợp lệ
            throw new ErrorHandler(HttpStatus.BAD_REQUEST,
                    String.join(", ", validator.getMessages(result)));
        }
    }

    // Lấy đường dẫn đầy đủ từ đường dẫn tương đối
    public Path getFullPathFromLink(String link) {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/static" + link;
        return Paths.get(filePath);
    }

    // Kiểm tra file có tồn tại hay không
    public void validateFileExists(Path path) {
        if (!Files.exists(path)) {
            throw new ErrorHandler(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    // Đọc nội dung file và xác định loại MIME
    public FileData getFileData(Path path) throws IOException, java.io.IOException {
        // Xác định MIME type của file
        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Loại MIME mặc định nếu không xác định được
        }
        // Đọc toàn bộ nội dung file dưới dạng byte
        byte[] fileContent = Files.readAllBytes(path);
        // Trả về thông tin file
        return new FileData(mimeType, fileContent, path.getFileName().toString());
    }

    // Inner class chứa thông tin file
        @Getter
        @Setter
        public static final class FileData {
        private final String mimeType;
        private final byte[] content;
        private final String fileName;

        public FileData(String mimeType, byte[] content, String fileName) {
            this.mimeType = mimeType;
            this.content = content;
            this.fileName = fileName;
        }

        public String mimeType() {
            return mimeType;
        }

        public byte[] content() {
            return content;
        }

        public String fileName() {
            return fileName;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (FileData) obj;
            return Objects.equals(this.mimeType, that.mimeType) &&
                    Objects.equals(this.content, that.content) &&
                    Objects.equals(this.fileName, that.fileName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mimeType, content, fileName);
        }

        @Override
        public String toString() {
            return "FileData[" +
                    "mimeType=" + mimeType + ", " +
                    "content=" + content + ", " +
                    "fileName=" + fileName + ']';
        }
    }
    // Save file into the specified directory
    public String saveFile1(MultipartFile file, String subDirectory) throws IOException, java.io.IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/" + subDirectory;

        Path uploadPath = Paths.get(uploadDir);
        // Ensure directory exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        // Overwrite existing file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/" + subDirectory + fileName;
    }

    // Delete file if it exists
    public void deleteFileIfExists(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            throw new ErrorHandler(HttpStatus.BAD_REQUEST, "File path cannot be empty");
        }

        Path filePath = getFullPathFromLink(relativePath);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new ErrorHandler(HttpStatus.NOT_FOUND, "File not found: " + relativePath);
            }
        } catch (IOException | java.io.IOException e) {
            throw new ErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file");
        }
    }

}
