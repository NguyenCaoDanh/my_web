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
    private UserRepository userRepository; // Repository thao tác với bảng User

    @Autowired
    private GeneralService generalService; // Dịch vụ chung hỗ trợ các tác vụ khác

    @Override
    public void save(User user) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();

            // Kiểm tra xem tài khoản hiện tại đã liên kết với User chưa
            User existingUser = userRepository.findByAccount_IdAccount(currentAccount.getIdAccount());

            if (existingUser != null) {
                // Nếu User đã tồn tại, cập nhật thông tin
                existingUser.setFirstName(user.getFirstName()); // Cập nhật tên
                existingUser.setLastName(user.getLastName());   // Cập nhật họ
                existingUser.setMiddleName(user.getMiddleName()); // Cập nhật tên đệm
                existingUser.setEmail(user.getEmail());         // Cập nhật email
                existingUser.setPhone(user.getPhone());         // Cập nhật số điện thoại
                existingUser.setAddress(user.getAddress());     // Cập nhật địa chỉ
                existingUser.setGithub(user.getGithub());       // Cập nhật GitHub
                existingUser.setFacebook(user.getFacebook());   // Cập nhật Facebook
                existingUser.setZalo(user.getZalo());           // Cập nhật Zalo
                existingUser.setAvatar(user.getAvatar());       // Cập nhật avatar

                // Lưu User đã được cập nhật vào cơ sở dữ liệu
                userRepository.save(existingUser);
            } else {
                // Nếu User chưa tồn tại, liên kết với tài khoản hiện tại và tạo mới
                user.setAccount(currentAccount);
                userRepository.save(user); // Lưu User mới vào cơ sở dữ liệu
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ và ném ra thông báo lỗi
            throw new ErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving User: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) {
        // Chưa triển khai, có thể thêm logic xóa User nếu cần
    }

    @Override
    public Iterator<User> findAll() {
        // Chưa triển khai, có thể thêm logic lấy tất cả User nếu cần
        return null;
    }

    @Override
    public User findOne(Integer integer) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();

            // Tìm kiếm User liên kết với tài khoản hiện tại
            User user = userRepository.findByAccount_IdAccount(currentAccount.getIdAccount());

            // Nếu không tìm thấy User, ném ngoại lệ
            if (user == null) {
                throw new ErrorHandler(HttpStatus.NOT_FOUND, "User not found for the current account.");
            }

            return user;
        } catch (Exception e) {
            // Xử lý ngoại lệ và ném ra thông báo lỗi
            throw new ErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching User: " + e.getMessage());
        }
    }
}
