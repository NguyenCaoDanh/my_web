package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.generic.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface CvService extends IService<Cv, Integer> {

    //    Optional<CvResponse> update(Integer id, String newCvName, String link);
    Optional<Cv> update(Integer id);

    Page<Cv> findAll(Pageable pageable);

    Optional<Cv> getById(Integer id);
}
