package com.studyolle.study;

import com.WithAccount;
import com.studyolle.account.AccountRepository;
import com.studyolle.mail.EmailService;
import com.studyolle.study.validator.StudyForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.UriEncoder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StudyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    StudyRepository studyRepository;

    @MockBean
    EmailService emailService;

    @DisplayName("스터디폼 확인")
    @Test
    @WithAccount("abc")
    void studyForm() throws Exception {
        mockMvc.perform(get("/new-study"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("account"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("studyForm"));
    }

    @DisplayName("스터디 폼 등록")
    @Test
    @WithAccount("abc")
    void registerStudy() throws Exception {
        String path = "test";
        mockMvc.perform(post("/new-study")
                .param("path", path)
                .param("title", "test")
                .param("fullDescription", "test")
                .param("shortDescription", "test")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/study/" + UriEncoder.encode(path)));

        boolean existsByPath = studyRepository.existsByPath(path);
        Assertions.assertThat(existsByPath).isTrue();
    }
}