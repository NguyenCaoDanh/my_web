package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.dto.response.CvResponse;
import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.generic.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface CvService extends IService <Cv, Integer> {
     Optional<CvResponse> update(Integer id, String newCvName);

//    Optional<CvResponse> update(Integer id, String newCvName, String link);

    Page<Cv> findAll(Pageable pageable);


}
