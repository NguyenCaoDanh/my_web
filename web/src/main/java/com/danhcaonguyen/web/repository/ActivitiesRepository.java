package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.Activities;
import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitiesRepository extends IRepository<Activities,Integer> {
    Page<Activities> findByUser(User user, Pageable pageable);
}
