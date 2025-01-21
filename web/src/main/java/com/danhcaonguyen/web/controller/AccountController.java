package com.danhcaonguyen.web.controller;

import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.dto.request.LoginDTO;
import com.danhcaonguyen.web.dto.response.Token;
import com.danhcaonguyen.web.entity.Account;
import com.danhcaonguyen.web.exception.ExceptionResponse;
import com.danhcaonguyen.web.generic.GenericController;
import com.danhcaonguyen.web.generic.IService;
import com.danhcaonguyen.web.jwt.JwtService;
import com.danhcaonguyen.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account")
@CrossOrigin

public class AccountController extends GenericController<Account, Integer> {

    @Autowired
    private AccountService accountService; // Service để xử lý logic liên quan đến tài khoản

    @Autowired
    private AuthenticationManager authenticationManager; // Quản lý xác thực

    @Autowired
    private JwtService jwtService; // Dịch vụ tạo và xử lý JWT token

    /**
     * Cung cấp service cho lớp cha GenericController.
     *
     * @return Service của Account.
     */
    @Override
    public IService<Account, Integer> getService() {
        return accountService;
    }

    /**
     * Xử lý đăng nhập người dùng.
     *
     * @param loginDTO Thông tin đăng nhập bao gồm username và password.
     * @return Phản hồi chứa JWT token nếu đăng nhập thành công, hoặc thông báo lỗi.
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            // Xác thực thông tin đăng nhập
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()));

            // Nếu xác thực thành công, tạo token và trả về
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(loginDTO.getUsername());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new RequestResponse(new Token(token)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ExceptionResponse("Invalid username or password"));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse("Username not found"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ExceptionResponse("Incorrect password"));
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ExceptionResponse("Account is locked"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ExceptionResponse("Account is disabled"));
        } catch (AccountExpiredException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ExceptionResponse("Account has expired"));
        } catch (CredentialsExpiredException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ExceptionResponse("Credentials have expired"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ExceptionResponse("An error occurred: " + e.getMessage()));
        }
    }

    /**
     * Xử lý đăng ký tài khoản mới.
     *
     * @param account Thông tin tài khoản cần đăng ký.
     * @return Phản hồi xác nhận đăng ký thành công hoặc thông báo lỗi.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            // Lưu tài khoản mới
            accountService.save(account);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RequestResponse("Account registered successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ExceptionResponse("An error occurred: " + e.getMessage()));
        }
    }
}
