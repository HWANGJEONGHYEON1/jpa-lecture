package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();
        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        assertThat(saveMember).isEqualTo(findMember);

    }

    @Test
    void basicCRUD() {
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        Assertions.assertThat(memberRepository.findById(memberA.getId()).get()).isEqualTo(memberA);
        Assertions.assertThat(memberRepository.findById(memberB.getId()).get()).isEqualTo(memberB);

        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(2);
        Assertions.assertThat(memberRepository.count()).isEqualTo(2);

        // 삭제
        memberRepository.delete(memberA);
        memberRepository.delete(memberB);
        Assertions.assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member member = new Member("aaa", 10);
        Member member1 = new Member("aaa", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findUser() {
        Member member = new Member("aaa", 10);
        Member member1 = new Member("aaa", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        Assertions.assertThat(memberRepository.findUser("aaa", 10).get(0).getUsername()).isEqualTo("aaa");
    }

    @Test
    void findUserNames() {
        Member member = new Member("aaa", 10);
        Member member1 = new Member("aaa", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        Assertions.assertThat(memberRepository.findUsernameList().size()).isEqualTo(2);
    }

    @Test
    void findUserDto() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member = new Member("aaa", 10);
        member.setTeam(team1);
        Member member1 = new Member("aaa", 20);
        member1.setTeam(team2);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto );
        }
    }

    @Test
    void findUserNameList() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member = new Member("aaa", 10);
        member.setTeam(team1);
        Member member1 = new Member("aaa", 20);
        member1.setTeam(team2);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("aaa", "aaa"));
        for (Member name : byNames) {
            System.out.println("name = " + name );
        }
    }

    @Test
    void paging() {
        memberRepository.save(new Member("aaa1", 10));
        memberRepository.save(new Member("aaa2", 10));
        memberRepository.save(new Member("aaa3", 10));
        memberRepository.save(new Member("aaa4", 10));
        memberRepository.save(new Member("aaa5", 10));
        memberRepository.save(new Member("aaa6", 10));
        memberRepository.save(new Member("aaa7", 10));
        memberRepository.save(new Member("aaa8", 10));
        memberRepository.save(new Member("aaa9", 10));
        memberRepository.save(new Member("aaa10", 10));
        memberRepository.save(new Member("aaa11", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
//        assertThat(totalElements).isEqualTo(11);
//        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void findByUsername() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member = new Member("aaa", 10);
        member.setTeam(team1);
        Member member1 = new Member("aaa", 20);
        member1.setTeam(team2);
        memberRepository.save(member);
        memberRepository.save(member1);

       memberRepository.findByUsername("aaa");

        Member ccc = memberRepository.findMemberByUsername("ccc");
        System.out.println("ccc = " + ccc);
    }

    @Test
    void bulkUpdate() {
        memberRepository.save(new Member("aaa1", 10));
        memberRepository.save(new Member("aaa2", 19));
        memberRepository.save(new Member("aaa3", 20));
        memberRepository.save(new Member("aaa4", 30));
        memberRepository.save(new Member("aaa5", 40));

        int result = memberRepository.bulkAgePlus(20);

        List<Member> resultList = memberRepository.findByUsername("aaa5");

        System.out.println("member40 " + resultList.get(0));
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10);
        member1.setTeam(teamA);
        Member member2 = new Member("member2", 20);
        member2.setTeam(teamB);
        
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();
        List<Member> member11 = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

    @Test
    void queryHint() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("멤바");
        em.flush();
    }

    @Test
    void queryLock() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> lockByUsername = memberRepository.findLockByUsername(member1.getUsername());

        em.flush();
    }

    @Test
    void customCall() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    void jpaEventBaseEntity() throws InterruptedException {
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        Optional<Member> byId = memberRepository.findById(member.getId());

    }
}
