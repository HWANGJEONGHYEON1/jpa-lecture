package com.studyolle.account;

import com.studyolle.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @Test
    @DisplayName("인증 메일 확인 - 입력값 오류")
    void checkMailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
                .param("token", "123123")
                .param("email", "deert202@naver.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("인증 메일 확인 - 입력값정상")
    void checkMailToken() throws Exception {
        Account account = Account.builder()
                .email("test@email.com")
                .password("12341234")
                .nickname("hwaang")
                .build();
        Account savedAccount = accountRepository.save(account);
        savedAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                        .param("token", savedAccount.getEmailCheckToken())
                        .param("email", savedAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated().withUsername("hwaang"));
    }

    @Test
    @DisplayName("회원 가입화면 보이는지 테스트")
    void signForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated()); // 해당 모델이 없을 수 있으니.
    }

    @Test
    @DisplayName("회원가입 처리 - 입력값 오류")
    void singUpSubmit_exception() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "jhhwang")
                .param("email", "email")
                .param("password", "abcdef")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("회원가입 처리 - 입력값 정상")
    void singUpSubmit_success() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "jhhwang")
                        .param("email", "email@email.com")
                        .param("password", "abcdef1234")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("jhhwang"));

        Account findEmail = accountRepository.findByEmail("email@email.com");
        assertNotNull(findEmail);
        assertNotEquals(findEmail.getPassword(), "abcdef1234");

        assertTrue(accountRepository.existsByEmail("email@email.com"));
        assertNotNull(findEmail.getEmailCheckToken()); // 토큰이 생성되었는지.
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}