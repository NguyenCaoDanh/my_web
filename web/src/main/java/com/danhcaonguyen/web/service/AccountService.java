package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.generic.IRepository;
import com.danhcaonguyen.web.generic.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account,Integer>, UserDetailsService {
    IRepository<Account, Integer> getRepository();
}
