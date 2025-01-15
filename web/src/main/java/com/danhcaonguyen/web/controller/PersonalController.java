package com.danhcaonguyen.web.controller;

import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.dto.response.MyInfoResponse;
import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.exception.ExceptionResponse;
import com.danhcaonguyen.web.generic.GenericController;
import com.danhcaonguyen.web.generic.IService;
import com.danhcaonguyen.web.service.GeneralService;
import com.danhcaonguyen.web.service.PersonalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/personal")
public class PersonalController extends GenericController<User, Integer> {

    @Autowired
    private PersonalService personalService; // Dịch vụ quản lý thông tin cá nhân

    @Autowired
    private GeneralService generalService; // Dịch vụ hỗ trợ chung

    @Override
    public IService<User, Integer> getService() {
        // Trả về dịch vụ được sử dụng trong lớp GenericController
        return personalService;
    }

    /**
     * API lưu hoặc cập nhật thông tin cá nhân
     *
     * @param userJson JSON chứa thông tin của người dùng
     * @param avatar   File avatar (nếu có)
     * @return ResponseEntity chứa thông tin kết quả xử lý
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdatePersonalInfo(
            @RequestParam("user") String userJson,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            // Chuyển đổi JSON sang đối tượng User
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            // Lấy tài khoản hiện tại và User liên kết
            Account currentAccount = generalService.getCurrentAccount();
            User associatedUser = generalService.getAssociatedUser(currentAccount);

            // Nếu có file avatar, lưu vào thư mục và cập nhật đường dẫn vào User
            if (avatar != null && !avatar.isEmpty()) {
                String avatarPath = generalService.saveFile(avatar, associatedUser.getIdUser() + "/images/");
                user.setAvatar(avatarPath);
            }

            // Lưu hoặc cập nhật thông tin cá nhân
            personalService.save(user);

            // Trả về phản hồi thành công
            return ResponseEntity.ok(new RequestResponse("Personal information saved/updated successfully."));
        } catch (ErrorHandler e) {
            // Xử lý ngoại lệ do người dùng hoặc hệ thống ném ra
            return ResponseEntity.status(e.getStatus())
                    .body(new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            // Xử lý lỗi khác
            return ResponseEntity.internalServerError()
                    .body(new ExceptionResponse("An error occurred: " + e.getMessage()));
        }
    }

    /**
     * API lấy thông tin cá nhân hiện tại
     *
     * @return ResponseEntity chứa thông tin cá nhân hoặc thông báo lỗi
     */
    @GetMapping("/my-information")
    public ResponseEntity<?> getCurrentPersonalInfo() {
        try {
            // Lấy thông tin User của tài khoản hiện tại
            User currentUser = personalService.findOne(null);

            // Chuyển đổi User sang MyInfoResponse
            MyInfoResponse response = getMyInfoResponse(currentUser);

            // Trả về DTO chứa thông tin người dùng
            return ResponseEntity.ok(response);
        } catch (ErrorHandler e) {
            // Xử lý ngoại lệ tùy chỉnh
            return ResponseEntity.status(e.getStatus())
                    .body(new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            // Xử lý lỗi không mong muốn
            return ResponseEntity.internalServerError()
                    .body(new ExceptionResponse("An error occurred: " + e.getMessage()));
        }
    }

    private static MyInfoResponse getMyInfoResponse(User currentUser) {
        MyInfoResponse response = new MyInfoResponse();
        response.setFirstName(currentUser.getFirstName());
        response.setLastName(currentUser.getLastName());
        response.setEmail(currentUser.getEmail());
        response.setPhone(currentUser.getPhone());
        response.setAddress(currentUser.getAddress());
        response.setGithub(currentUser.getGithub());
        response.setFacebook(currentUser.getFacebook());
        response.setZalo(currentUser.getZalo());
        response.setAvatar(currentUser.getAvatar());
        return response;
    }


}
