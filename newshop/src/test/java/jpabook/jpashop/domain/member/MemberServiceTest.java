package jpabook.jpashop.domain.member;

import jpabook.jpashop.domain.member.dto.MemberDto;
import jpabook.jpashop.domain.member.dto.MemberForm;
import jpabook.jpashop.exception.AlreadyExistMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired EntityManager entityManager;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName("AA");
        Member member = Member.createMember(memberForm);

        entityManager.persist(member);

        MemberForm memberForm1 = new MemberForm();
        memberForm1.setName("BB");
        Member member2 = Member.createMember(memberForm1);
        entityManager.persist(member2);
    }

    @Test
    @DisplayName("회원저장")
    void save() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName("member1");
        memberForm.setCity("suwon");
        memberForm.setStreet("gyeonggi");
        memberForm.setZipcode("16205");

        final Long savedId = memberService.join(memberForm);

        Assertions.assertThat(memberRepository.existsById(savedId)).isTrue();
    }

    @Test
    @DisplayName("중복된 멤버가 있을 경우 에러 발생")
    void save_duplicate_exception() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName("AA");
        Assertions.assertThatThrownBy(() -> memberService.join(memberForm))
                .isInstanceOf(AlreadyExistMemberException.class);
    }

    @Test
    @DisplayName("전체 멤버 조회")
    void findAll() {
        final List<MemberDto> allMembers = memberService.findAllMembers();

        Assertions.assertThat(allMembers.size()).isEqualTo(2);
    }
}