package com.studyolle.zone;

import com.WithAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyolle.account.AccountRepository;
import com.studyolle.account.AccountService;
import com.studyolle.domain.Account;
import com.studyolle.domain.Zone;
import com.studyolle.mail.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ZoneControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired ZoneService zoneService;

    @Autowired ZoneRepository zoneRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;

    @MockBean
    EmailService emailService;

    private Zone testZone = Zone.builder().city("test").localNameOfCity("테스트시").province("테스트주").build();

    @BeforeEach
    void before() {
        zoneRepository.save(testZone);
    }

    @AfterEach
    void after() {
        accountRepository.deleteAll();
        zoneRepository.deleteAll();
    }


    @DisplayName("Zone 폼 확인")
    @WithAccount("abc")
    @Test
    void zoneForm() throws Exception {
        mockMvc.perform(get("/settings/zones"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("zones"))
                .andExpect(view().name("settings/zones"));
    }

    @DisplayName("Zone 등록")
    @WithAccount("abc")
    @Test
    void zone_add() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post("/settings/zones/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        Account abc = accountRepository.findByNickname("abc");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        assertTrue(abc.getZones().contains(zone));
    }

    @DisplayName("Zone 제거")
    @WithAccount("abc")
    @Test
    void zone_remove() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        Account abc = accountRepository.findByNickname("abc");

        accountService.addZone(abc, testZone);

        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        assertTrue(abc.getZones().contains(zone));

        mockMvc.perform(post("/settings/zones/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        Zone targetZone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        assertFalse(abc.getZones().contains(targetZone));
    }

}