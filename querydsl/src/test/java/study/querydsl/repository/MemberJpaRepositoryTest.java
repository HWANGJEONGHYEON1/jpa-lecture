package study.querydsl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Optional<Member> findMember = memberJpaRepository.findById(member.getId());
        assertThat(findMember.get()).isEqualTo(member);

        List<Member> members = memberJpaRepository.findAll();
        assertThat(members).contains(member);
    }

    @Test
    void basicQueryDsl() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findByUsernameQueryDsl(member.getUsername()).get(0);
        assertThat(findMember).isEqualTo(member);

        List<Member> members = memberJpaRepository.findAllQueryDsl();
        assertThat(members).contains(member);
    }

}