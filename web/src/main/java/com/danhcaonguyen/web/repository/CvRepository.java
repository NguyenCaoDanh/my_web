package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CvRepository extends IRepository<Cv,Integer> {
    Page<Cv> findAll(Pageable pageable);
}
