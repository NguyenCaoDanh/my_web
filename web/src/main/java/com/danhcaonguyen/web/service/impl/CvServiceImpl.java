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
    private CvRepository cvRepository; // Repository để thao tác với dữ liệu CV

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

            // Xóa CV
            cvRepository.delete(cv);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình xóa CV
            throw new RuntimeException("Error while deleting CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Iterator<Cv> findAll() {
        // Trả về null vì chưa triển khai, có thể triển khai sau nếu cần
        return null;
    }

    @Override
    public Cv findOne(Integer id) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Tìm CV theo ID
            Cv cv = cvRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            // Kiểm tra CV có thuộc về User hiện tại không
            if (!cv.getUser().equals(currentUser)) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            return cv; // Trả về CV tìm được
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình tìm CV
            throw new RuntimeException("Error while fetching CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cv> update(Integer id) {
        try {
            // Tìm CV theo ID
            Cv cv = cvRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            // Lưu thay đổi vào database
            cvRepository.save(cv);

            // Trả về CV sau khi cập nhật
            return Optional.of(cv);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình cập nhật CV
            throw new RuntimeException("Error while updating CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Cv> findAll(Pageable pageable) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Lấy danh sách CV thuộc về User hiện tại với phân trang
            return cvRepository.findByUser(currentUser, pageable);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình lấy danh sách CV
            throw new RuntimeException("Error while fetching CVs: " + e.getMessage(), e);
        }
    }
}
