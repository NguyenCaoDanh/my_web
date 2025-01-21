package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.AccountRepository;
import com.danhcaonguyen.web.repository.CvRepository;
import com.danhcaonguyen.web.service.CvService;
import com.danhcaonguyen.web.service.GeneralService;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;

@Service
public class CvServiceImpl  implements CvService {

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GeneralService generalService; // Dịch vụ chung cung cấp các chức năng hỗ trợ

    @Override
    public void save(Cv cv) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Liên kết User hiện tại với CV và lưu vào database
            cv.setUser(currentUser);
            cvRepository.save(cv);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình lưu CV
            throw new RuntimeException("Error while saving CV: " + e.getMessage(), e);
        }
    }





    @Override
    public void delete(Integer cvId) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Tìm CV theo ID
            Cv cv = cvRepository.findById(cvId)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            // Kiểm tra CV có thuộc về User hiện tại không
            if (!cv.getUser().equals(currentUser)) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            // Xóa file trong thư mục static/cv
            if (cv.getLink() != null) {
                // Tạo đường dẫn đầy đủ tới file trong thư mục static/cv
                String fileDirectory = "src/main/resources/static";
                Path filePath = Paths.get(fileDirectory, cv.getLink());

                if (Files.exists(filePath)) {
                    Files.delete(filePath); // Xóa file nếu tồn tại
                } else {
                    throw new ErrorHandler(HttpStatus.NOT_FOUND, "File not found in directory");
                }
            }

            // Xóa CV trong cơ sở dữ liệu
            cvRepository.delete(cv);

        } catch (Exception e) {
            // Xử lý lỗi trong quá trình xóa CV hoặc file
            throw new RuntimeException("Error while deleting CV: " + e.getMessage(), e);
        }
    }



    @Override
    public Iterator<Cv> findAll() {
        return null;
    }

    @Override
    public Cv findOne(Integer id) {
        try {
            // Lấy tài khoản đang đăng nhập
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account currentAccount = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.UNAUTHORIZED, "Account not found"));

            // Kiểm tra User
            if (currentAccount.getUser() == null) {
                throw new ErrorHandler(HttpStatus.BAD_REQUEST, "User not associated with the account");
            }

            // Tìm CV theo ID và đảm bảo nó thuộc về User hiện tại
            Cv cv = cvRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            if (!cv.getUser().equals(currentAccount.getUser())) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            return cv;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching CV: " + e.getMessage(), e);
        }
    }

//    @Override
//    public Optional<Cv> findById(Integer id) {
//        return Optional.empty();
//    }


//    @Override
//    public Optional<Cv> update(Integer id) {
//        try {
//            Account currentAccount = generalService.getCurrentAccount();
//            User currentUser = generalService.getAssociatedUser(currentAccount);
//
//            Cv existingCv = cvRepository.findById(id)
//                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));
//
//            if (!existingCv.getUser().equals(currentUser)) {
//                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
//            }
//
//            if (existingCv.getLink() != null) {
//                generalService.deleteFileIfExists(existingCv.getLink());
//            }
//
//            cvRepository.delete(existingCv);
//
//            Cv newCv = new Cv();
//            newCv.setCvName(newCv.getCvName());
//            newCv.setLink(newCv.getLink());
//            newCv.setUser(currentUser);
//          save(newCv);
//
//            return Optional.of(newCv);
//
//        } catch (ErrorHandler e) {
//            throw e;
//        } catch (Exception e) {
//            throw new RuntimeException("An unexpected error occurred", e);
//        }
//    }

    @Override
    public Optional<Cv> update(Integer id, String newName, MultipartFile newFile) {
        try {
            Account currentAccount = generalService.getCurrentAccount();
            User currentUser = generalService.getAssociatedUser(currentAccount);

            Cv existingCv = cvRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            if (!existingCv.getUser().equals(currentUser)) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            // Xóa file cũ nếu file mới được cung cấp
            if (newFile != null && !newFile.isEmpty()) {
                if (existingCv.getLink() != null) {
                    System.out.println("Deleting old file: " + existingCv.getLink());
                    generalService.deleteFileIfExists(existingCv.getLink()); // Xóa file cũ
                }

                // Lưu file mới
                String filePath = generalService.saveFile(newFile, currentUser.getIdUser() + "/cv/");
                existingCv.setLink(filePath);
                System.out.println("Saved new file: " + filePath);
            }

            // Cập nhật tên nếu có
            if (newName != null && !newName.isEmpty()) {
                existingCv.setCvName(newName);
            }

            cvRepository.save(existingCv); // Lưu thay đổi
            return Optional.of(existingCv);

        } catch (ErrorHandler e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }




    @Override
    public Optional<Cv> getById(Integer id) {
        return cvRepository.findById(id);
    }
    @Override
    public Page<Cv> findAll(Pageable pageable) {
        try {
            // Lấy tài khoản đang đăng nhập
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account currentAccount = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.UNAUTHORIZED, "Account not found"));

            // Kiểm tra User
            if (currentAccount.getUser() == null) {
                throw new ErrorHandler(HttpStatus.BAD_REQUEST, "User not associated with the account");
            }

            // Lấy danh sách CV của User đang đăng nhập với phân trang
            return cvRepository.findByUser(currentAccount.getUser(), pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching CVs: " + e.getMessage(), e);
        }
    }

}
