package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.ExperienceCompany;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.AccountRepository;
import com.danhcaonguyen.web.repository.CvRepository;
import com.danhcaonguyen.web.repository.ExperienceCompanyRepository;
import com.danhcaonguyen.web.service.ExperienceCompanyService;
import com.danhcaonguyen.web.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class ExperienceCompanyImpl implements ExperienceCompanyService {
    @Autowired
    private ExperienceCompanyRepository experienceCompanyRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GeneralService generalService; // Dịch vụ chung cung cấp các chức năng hỗ trợ

    @Override
    public void save(ExperienceCompany experienceCompany) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Liên kết User hiện tại với CV và lưu vào database
            experienceCompany.setUser(currentUser);
            experienceCompanyRepository.save(experienceCompany);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình lưu CV
            throw new RuntimeException("Error while saving CV: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Iterator<ExperienceCompany> findAll() {
        return null;
    }

    @Override
    public ExperienceCompany findOne(Integer id) {
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
            ExperienceCompany experienceCompany = experienceCompanyRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "CV not found"));

            if (!experienceCompany.getUser().equals(currentAccount.getUser())) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            return experienceCompany;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<ExperienceCompany> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ExperienceCompany> getById(Integer id) {
        return experienceCompanyRepository.findById(id);
    }
}
