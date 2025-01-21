package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.ExperienceCompany;
import com.danhcaonguyen.web.generic.IService;
import org.hibernate.service.spi.InjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ExperienceCompanyService extends IService <ExperienceCompany,Integer>{
    Page<ExperienceCompany> findAll(Pageable pageable);

    Optional<ExperienceCompany> getById(Integer id);


}
