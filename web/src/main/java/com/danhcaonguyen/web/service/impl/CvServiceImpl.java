package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.dto.response.CvResponse;
import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.AccountRepository;
import com.danhcaonguyen.web.repository.CvRepository;
import com.danhcaonguyen.web.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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


    @Override
    public Optional<CvResponse> update(Integer id, String newCvName) {
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

            // Cập nhật thông tin CV
            cv.setCvName(newCvName);
            String link = "";
            cv.setLink(link);
            cvRepository.save(cv);

            // Chuyển đổi đối tượng Cv thành CvResponse
            CvResponse responseDto = new CvResponse(cv.getCvName(), cv.getLink());
            return Optional.of(responseDto);

        } catch (ErrorHandler e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while updating CV: " + e.getMessage(), e);
        }
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
