package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.dto.response.ActivityResponse;
import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Activities;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.repository.ActivitiesRepository;
import com.danhcaonguyen.web.service.ActivityService;
import com.danhcaonguyen.web.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivitiesRepository activitiesRepository;
    @Autowired
    private GeneralService generalService; // Dịch vụ chung cung cấp các chức năng hỗ trợ

    @Override
    public void save(Activities activities) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Liên kết User hiện tại với CV và lưu vào database
            activities.setUser(currentUser);
            activitiesRepository.save(activities);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình lưu CV
            throw new RuntimeException("Error while saving Activity: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer integer) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Tìm CV theo ID
            Activities activities = activitiesRepository.findById(integer)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "Activity not found"));

            // Kiểm tra CV có thuộc về User hiện tại không
            if (!activities.getUser().equals(currentUser)) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            // Xóa CV
            activitiesRepository.delete(activities);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình xóa CV
            throw new RuntimeException("Error while deleting Activity: " + e.getMessage(), e);
        }
    }

    @Override
    public Iterator<Activities> findAll() {
        return null;
    }

    @Override
    public Activities findOne(Integer integer) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Tìm CV theo ID
            Activities activities = activitiesRepository.findById(integer)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "Activity not found"));

            // Kiểm tra CV có thuộc về User hiện tại không
            if (!activities.getUser().equals(currentUser)) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }
            ActivityResponse response = new ActivityResponse();
            response.setTitle(activities.getTitle());
            response.setDescription(activities.getDescription());

            return activities; // Trả về CV tìm được
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình tìm CV
            throw new RuntimeException("Error while fetching Activity: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Activities> update(Integer id) {
        try {
            // Tìm CV theo ID
            Activities activities = activitiesRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "Activity not found"));

            // Lưu thay đổi vào database
            activitiesRepository.save(activities);

            // Trả về CV sau khi cập nhật
            return Optional.of(activities);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình cập nhật CV
            throw new RuntimeException("Error while updating Activity: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Activities> findAll(Pageable pageable) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Lấy danh sách CV thuộc về User hiện tại với phân trang
            return activitiesRepository.findByUser(currentUser, pageable);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình lấy danh sách CV
            throw new RuntimeException("Error while fetching Activities: " + e.getMessage(), e);
        }
    }

    @Override
    public ActivityResponse findById(Integer id) {
        try {
            // Lấy tài khoản hiện tại từ SecurityContext
            Account currentAccount = generalService.getCurrentAccount();
            // Kiểm tra tài khoản có liên kết với User hay không
            User currentUser = generalService.getAssociatedUser(currentAccount);

            // Tìm CV theo ID
            Activities activities = activitiesRepository.findById(id)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.NOT_FOUND, "Activity not found"));

            // Kiểm tra CV có thuộc về User hiện tại không
            if (!activities.getUser().equals(currentUser)) {
                throw new ErrorHandler(HttpStatus.FORBIDDEN, "Access denied");
            }

            // Map Activities to ActivityResponse
            ActivityResponse response = new ActivityResponse();
            response.setTitle(activities.getTitle());
            response.setDescription(activities.getDescription());
            return response; // Return the DTO
        } catch (Exception e) {
            // Handle any exceptions
            throw new RuntimeException("Error while fetching Activity: " + e.getMessage(), e);
        }
    }
}
