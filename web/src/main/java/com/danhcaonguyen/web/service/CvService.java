package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.generic.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface CvService extends IService <Cv, Integer> {
    Optional<Cv> update(Integer id);
    Page<Cv> findAllPage(Pageable pageable);



}
