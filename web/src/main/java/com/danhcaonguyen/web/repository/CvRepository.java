package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.Cv;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvRepository extends IRepository<Cv,Integer> {
}
