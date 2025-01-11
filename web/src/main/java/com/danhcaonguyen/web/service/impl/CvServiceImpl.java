package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.CvRepository;
import com.danhcaonguyen.web.service.CvService;
import com.danhcaonguyen.web.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.Optional;

@Service
public class CvServiceImpl implements CvService {

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private GeneralService generalService;

    @Override
    public void save(Cv cv) {
        try {
            // Lấy tài khoản hiện tại và kiểm tra liên kết với User
            Account currentAccount = generalService.getCurrentAccount();
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Gắn User vào CV và lưu
            cv.setUser(currentUser);
            cvRepository.save(cv);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving CV: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer cvId) {
        // Implementation nếu cần thiết
    }

    @Override
    public Iterator<Cv> findAll() {
        return null;
    }

    @Override
    public Cv findOne(Integer id) {
        try {
            // Lấy tài khoản hiện tại và kiểm tra liên kết với User
            Account currentAccount = generalService.getCurrentAccount();
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Tìm CV theo ID và đảm bảo nó thuộc về User hiện tại
            Cv cv = cvRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            if (!cv.getUser().equals(currentUser)) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            return cv;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cv> update(Integer id) {
        try {
            Cv cv = cvRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));
            cvRepository.save(cv);
            return Optional.of(cv);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Cv> findAll(Pageable pageable) {
        try {
            // Lấy tài khoản hiện tại và kiểm tra liên kết với User
            Account currentAccount = generalService.getCurrentAccount();
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Lấy danh sách CV của User với phân trang
            return cvRepository.findByUser(currentUser, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching CVs: " + e.getMessage(), e);
        }
    }
}
