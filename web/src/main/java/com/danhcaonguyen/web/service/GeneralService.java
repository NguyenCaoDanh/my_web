package com.danhcaonguyen.web.service;

import com.danhcaonguyen.web.exception.ErrorHandler;
import org.passay.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneralService {

    public void validatePassword(String password) {
        PasswordValidator validator = new PasswordValidator(
                List.of(
                        new LengthRule(6, 128), // Độ dài tối thiểu 6 ký tự
                        new CharacterRule(EnglishCharacterData.UpperCase, 1), // Ít nhất 1 chữ in hoa
                        new CharacterRule(EnglishCharacterData.LowerCase, 1), // Ít nhất 1 chữ thường
                        new CharacterRule(EnglishCharacterData.Digit, 1),    // Ít nhất 1 chữ số
                        new CharacterRule(EnglishCharacterData.Special, 1),  // Ít nhất 1 ký tự đặc biệt
                        new WhitespaceRule() // Không chứa khoảng trắng
                )
        );

        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            throw new ErrorHandler(HttpStatus.BAD_REQUEST,
                    String.join(", ", validator.getMessages(result)));
        }
    }
}
