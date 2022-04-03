package com.studyolle.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    private String name;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // email, nickname
        SignUpForm form = (SignUpForm) target;

        if (accountRepository.existsByEmail(form.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{form.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if (accountRepository.existsByNickname(form.getEmail())) {
            errors.rejectValue("email", "invalid.nickname", new Object[]{form.getNickname()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
