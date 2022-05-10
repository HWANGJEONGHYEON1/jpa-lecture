package com.studyolle.settings;

import com.WithAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyolle.account.AccountRepository;
import com.studyolle.account.AccountService;
import com.studyolle.domain.Account;
import com.studyolle.domain.Tag;
import com.studyolle.mail.EmailService;
import com.studyolle.settings.form.TagForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired TagRepository tagRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountService accountService;
    @MockBean
    EmailService emailService;

    @WithAccount("abc")
    @DisplayName("태그 수정 폼")
    @Test
    void tagForm() throws Exception {
        mockMvc.perform(get(TagController.SETTINGS_TAG_URL))
                .andExpect(view().name(TagController.SETTINGS_TAG_VIEW_NAME))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("whitelist"));
    }

    @WithAccount("abc")
    @DisplayName("태그 수정 추가")
    @Test
    void tagFormAdd() throws Exception {

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("HIHI");
        mockMvc.perform(post(TagController.SETTINGS_TAG_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle(tagForm.getTagTitle());
        assertNotNull(newTag);
        assertTrue(accountRepository.findByNickname("abc").getTags().contains(newTag));
    }

    @WithAccount("abc")
    @DisplayName("태그 수정 삭제")
    @Test
    void tagFormRemove() throws Exception {
        Account abc = accountRepository.findByNickname("abc");
        Tag newTag = tagRepository.save(Tag.builder().title("aa").build());
        accountService.addTag(abc, newTag);

        assertTrue(abc.getTags().contains(newTag));

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("aa");

        mockMvc.perform(post(TagController.SETTINGS_TAG_URL + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(abc.getTags().contains(newTag));
    }
}