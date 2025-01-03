package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.Projects;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends IRepository<Projects,Integer> {
}
