package com.danhcaonguyen.web.repository;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.generic.IRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends IRepository<Account,Integer> {
    Optional<Account> findByUsername(String username);
}
