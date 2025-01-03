package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.Post;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends IRepository<Post,Integer> {
}
