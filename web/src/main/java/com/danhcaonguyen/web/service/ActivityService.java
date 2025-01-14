package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.dto.response.ActivityResponse;
import com.danhcaonguyen.web.entity.Activities;
import com.danhcaonguyen.web.generic.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ActivityService extends IService<Activities,Integer> {
    Optional<Activities> update(Integer id);
    Page<Activities> findAll(Pageable pageable);
    public ActivityResponse findById(Integer id);
}
