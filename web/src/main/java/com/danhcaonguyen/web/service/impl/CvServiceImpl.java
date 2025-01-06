package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.AccountRepository;
import com.danhcaonguyen.web.repository.CvRepository;
import com.danhcaonguyen.web.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
@Service
public class CvServiceImpl implements CvService {
    @Autowired
    private CvRepository cvRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public void save(Cv cv) {
        try {
            // Get the username of the currently logged-in user
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // Fetch the Account associated with the username
            Account currentAccount = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.UNAUTHORIZED, "Account not found"));

            // Ensure the Account has an associated User
            if (currentAccount.getUser() == null) {
                throw new ErrorHandler(HttpStatus.BAD_REQUEST, "User not associated with the account");
            }

            // Set the user of the CV to the currently logged-in user's User
            cv.setUser(currentAccount.getUser());

            // Save the CV to the repository
            cvRepository.save(cv);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving CV: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer cvId) {
        try {
            Cv cv = cvRepository.findById(cvId)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            // Xóa file trên hệ thống
            if (cv.getLink() != null) {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/cv/";
                Path filePath = Paths.get(uploadDir, cv.getLink().substring(4));
                Files.deleteIfExists(filePath);
            }

            // Xóa CV khỏi cơ sở dữ liệu
            cvRepository.delete(cv);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Iterator<Cv> findAll() {
        return null;
    }

    @Override
    public Cv findOne(Integer integer) {
        return null;
    }
}
