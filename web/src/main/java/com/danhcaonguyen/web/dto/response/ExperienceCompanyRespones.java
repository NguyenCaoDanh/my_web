package com.danhcaonguyen.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ExperienceCompanyRespones
{
    private int idExperienceCompany;
    private String companyName;
    private String mst;
    private String companyUrl;
    private String companyImg;
    private String role;
    private String description;
}
