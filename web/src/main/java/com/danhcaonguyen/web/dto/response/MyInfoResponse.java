package com.danhcaonguyen.web.dto.response;

import com.danhcaonguyen.web.entity.User;
import lombok.Data;

@Data
public class MyInfoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String github;
    private String facebook;
    private String zalo;
    private String avatar;
}
