package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.ExperienceCompany;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceCompanyRepository extends IRepository<ExperienceCompany,Integer> {
    Page<ExperienceCompany> findByUser(User user, Pageable pageable);

}
