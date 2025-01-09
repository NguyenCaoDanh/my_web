package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.AccountRepository;
import com.danhcaonguyen.web.repository.CvRepository;
import com.danhcaonguyen.web.repository.UserRepository;
import com.danhcaonguyen.web.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Override
    public void save(Cv cv) {
        try {
            // Lấy tài khoản đang đăng nhập
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account currentAccount = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.UNAUTHORIZED, "Account not found"));

            // Kiểm tra nếu CV đã tồn tại

            if (currentAccount.getUser() == null) {
                // Cập nhật thông tin CV
                throw new ErrorHandler(HttpStatus.BAD_REQUEST, "User not associated with the account");

            }
            cv.setUser(currentAccount.getUser());
            cvRepository.save(cv);

        } catch (Exception e) {
            throw new RuntimeException("Error while saving CV: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer cvId) {
        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

        // Xóa file trên hệ thống
        try {
            if (cv.getLink() != null) {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/cv/";
                Path filePath = Paths.get(uploadDir, cv.getLink().substring(4));
                Files.deleteIfExists(filePath);
            }
            cvRepository.delete(cv);

        } catch (IOException e) {
            throw new RuntimeException("Error while deleting CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Iterator<Cv> findAll() {
        return null;
    }

    @Override
    public Cv findOne(Integer integer) {
        try {
            return cvRepository.findById(integer).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Cv> update(Integer id) {
        try {
            Cv cv = cvRepository.findById(id).get();
            cv.setCvName(cv.getCvName());
            cvRepository.save(cv);
            return Optional.of(cv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }    }


}
