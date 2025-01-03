package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.User;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends IRepository<User,Integer> {
    User findByAccount_IdAccount(int idAccount);
}
