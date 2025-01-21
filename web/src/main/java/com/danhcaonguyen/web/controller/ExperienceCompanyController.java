package com.danhcaonguyen.web.controller;

import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.dto.response.ExperienceCompanyRespones;
import com.danhcaonguyen.web.dto.response.MyInfoResponse;
import com.danhcaonguyen.web.entity.ExperienceCompany;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.exception.ExceptionResponse;
import com.danhcaonguyen.web.service.ExperienceCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ex_company")
public class ExperienceCompanyController {
    @Autowired
    private ExperienceCompanyService experienceCompanyService;
    @PostMapping("/save")
    public ResponseEntity<String> saveExperienceCompany(@RequestBody ExperienceCompany experienceCompany) {
        try {
            // Gọi phương thức save từ service
            experienceCompanyService.save(experienceCompany);
            return ResponseEntity.ok("ExperienceCompany saved successfully.");
        } catch (Exception e) {
            // Trả về lỗi nếu xảy ra exception
            return ResponseEntity.status(500).body("Error while saving ExperienceCompany: " + e.getMessage());
        }
    }
    @GetMapping("find/{id}")
    public ResponseEntity<ExperienceCompanyRespones> getMyInfoResponse(@PathVariable Integer id) {
        try {
            // Lấy dữ liệu từ cơ sở dữ liệu
            ExperienceCompany experienceCompany = experienceCompanyService.findOne(id);

            // Chuyển đổi từ entity sang DTO
            ExperienceCompanyRespones response = new ExperienceCompanyRespones();
            response.setIdExperienceCompany(experienceCompany.getIdExperienceCompany());
            response.setCompanyName(experienceCompany.getCompanyName());
            response.setMst(experienceCompany.getMst());
            response.setCompanyUrl(experienceCompany.getCompanyUrl());
            response.setCompanyImg(experienceCompany.getCompanyImg());
            response.setDescription(experienceCompany.getDescription());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null); // Trả về lỗi nếu không tìm thấy
        }
    }

}
