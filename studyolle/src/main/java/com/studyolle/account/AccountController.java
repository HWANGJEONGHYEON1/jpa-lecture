package com.studyolle.account;


import com.studyolle.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator validator;

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        // 회원가입처리
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if (account == null) {
            model.addAttribute("error", "wrong email");
            return view;
        }

        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong email");
            return view;
        }

        accountService.completeSignUp(account);

        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());

        return view;
    }

    @GetMapping("/check-email")
    public String checkEmail(Model model, @CurrentUser Account account) {
        String view = "account/check-email";
        model.addAttribute("email", account.getEmail());
        return view;
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(Model model, @CurrentUser Account account) {
        String view = "account/check-email";
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 10분에 한번 전송할 수 있습니다.");
            model.addAttribute("account", account);
            model.addAttribute("email", account.getEmail());
            return view;
        }

        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable("nickname") String nickname, Model model, @CurrentUser Account account) {
        Account byNickname = accountRepository.findByNickname(nickname);
        if (byNickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }

        model.addAttribute("account", byNickname);
        model.addAttribute("isOwner", byNickname.equals(account));

        return "account/profile";
    }
}