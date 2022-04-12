package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {


    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());
        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        assertThat(saveMember).isEqualTo(findMember);
    }

    @Test
    void basicCRUD() {
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        Assertions.assertThat(memberJpaRepository.findById(memberA.getId()).get()).isEqualTo(memberA);
        Assertions.assertThat(memberJpaRepository.findById(memberB.getId()).get()).isEqualTo(memberB);

        Assertions.assertThat(memberJpaRepository.findAll().size()).isEqualTo(2);
        Assertions.assertThat(memberJpaRepository.count()).isEqualTo(2);

        // 삭제
        memberJpaRepository.delete(memberA);
        memberJpaRepository.delete(memberB);
        Assertions.assertThat(memberJpaRepository.count()).isEqualTo(0);

    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member member = new Member("aaa", 10);
        Member member1 = new Member("aaa", 20);
        memberJpaRepository.save(member);
        memberJpaRepository.save(member1);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("aaa", 15);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void paging() {
        memberJpaRepository.save(new Member("aaa1", 10));
        memberJpaRepository.save(new Member("aaa2", 10));
        memberJpaRepository.save(new Member("aaa3", 10));
        memberJpaRepository.save(new Member("aaa4", 10));
        memberJpaRepository.save(new Member("aaa5", 10));
        memberJpaRepository.save(new Member("aaa6", 10));
        memberJpaRepository.save(new Member("aaa7", 10));
        memberJpaRepository.save(new Member("aaa8", 10));
        memberJpaRepository.save(new Member("aaa9", 10));
        memberJpaRepository.save(new Member("aaa10", 10));
        memberJpaRepository.save(new Member("aaa11", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(11);
    }

    @Test
    void bulkUpdate() {
        memberJpaRepository.save(new Member("aaa1", 10));
        memberJpaRepository.save(new Member("aaa2", 19));
        memberJpaRepository.save(new Member("aaa3", 20));
        memberJpaRepository.save(new Member("aaa4", 30));
        memberJpaRepository.save(new Member("aaa5", 40));

        int result = memberJpaRepository.bulkAgePlus(20);
        assertThat(result).isEqualTo(3);
    }
}