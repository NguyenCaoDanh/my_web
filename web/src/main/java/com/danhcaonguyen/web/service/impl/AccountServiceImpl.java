package com.danhcaonguyen.web.service.impl;

import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.entity.Role;
import com.danhcaonguyen.web.exception.ErrorHandler;
import com.danhcaonguyen.web.generic.IRepository;
import com.danhcaonguyen.web.repository.AccountRepository;
import com.danhcaonguyen.web.repository.RoleRepository;
import com.danhcaonguyen.web.service.AccountService;
import com.danhcaonguyen.web.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(Account account) {
        try {
            if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
                throw new ErrorHandler(HttpStatus.BAD_REQUEST, "Username already exists.");
            }
            // Gọi validate password từ GeneralService
            generalService.validatePassword(account.getPassword());

            Role role = roleRepository.findById(1)
                    .orElseThrow(() -> new ErrorHandler(HttpStatus.BAD_REQUEST, "Role with ID 1 not found"));

            account.setRole(role);
            account.setPassword(passwordEncoder.encode(account.getPassword()));

            accountRepository.save(account);
        } catch (Exception e) {
            throw new ErrorHandler(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @Override
    public void delete(Integer integer) {

    }


    @Override
    public Iterator<Account> findAll() {
        return null;
    }

    @Override
    public Account findOne(Integer integer) {
        return null;
    }


    @Override
    public IRepository<Account, Integer> getRepository() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.orElseThrow(() -> new ErrorHandler(HttpStatus.UNAUTHORIZED, "Account not exist"));
    }
}
