package com.studyolle.settings.validator;

import com.studyolle.account.AccountRepository;
import com.studyolle.settings.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nickNameForm = (NicknameForm) target;

        if (accountRepository.existsByNickname(nickNameForm.getNickname())) {
            errors.rejectValue("ㅈ", "wrong value", "이미 존재하는 닉네임입니다.");
        }
    }
}
