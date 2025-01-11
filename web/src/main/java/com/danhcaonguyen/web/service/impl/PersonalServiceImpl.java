package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.UserRepository;
import com.danhcaonguyen.web.service.GeneralService;
import com.danhcaonguyen.web.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Iterator;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralService generalService;

    @Override
    public void save(User user) {
        try {
            // Lấy tài khoản hiện tại
            Account currentAccount = generalService.getCurrentAccount();

            // Kiểm tra nếu User đã tồn tại
            User existingUser = userRepository.findByAccount_IdAccount(currentAccount.getIdAccount());
            if (existingUser != null) {
                // Cập nhật thông tin
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setMiddleName(user.getMiddleName());
                existingUser.setEmail(user.getEmail());
                existingUser.setPhone(user.getPhone());
                existingUser.setAddress(user.getAddress());
                existingUser.setGithub(user.getGithub());
                existingUser.setFacebook(user.getFacebook());
                existingUser.setZalo(user.getZalo());
                existingUser.setAvatar(user.getAvatar());
                userRepository.save(existingUser);
            } else {
                // Tạo mới nếu chưa có
                user.setAccount(currentAccount);
                userRepository.save(user);
            }
        } catch (Exception e) {
            throw new ErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) {
        // Implementation nếu cần thiết
    }

    @Override
    public Iterator<User> findAll() {
        return null;
    }

    @Override
    public User findOne(Integer integer) {
        return null;
    }
}
