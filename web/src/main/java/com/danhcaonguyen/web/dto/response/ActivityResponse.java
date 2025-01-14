package com.danhcaonguyen.web.dto.response;

import lombok.Data;

@Data
public class ActivityResponse {
    private String title;
    private String description;
    private String path; // URL for getById
}
