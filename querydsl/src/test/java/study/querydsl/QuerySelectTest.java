package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static study.querydsl.entity.QMember.member;

@SpringBootTest
@Transactional
public class QuerySelectTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }
    
    
    @Test
    void simpleProjectionTest() {
        List<Member> member = queryFactory
                .select(QMember.member)
                .from(QMember.member)
                .fetch();

        for (Member member1 : member) {
            System.out.println("member1 = " + member1);
        }
    }

    @Test
    void tupleProjection() {

        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    @Test
    void tupleProjectionDto() {
        List<MemberDto> resultList = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoByField() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findUserDto() {
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();

        for (UserDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void queryProjection() {
        List<MemberDto> result = queryFactory.
                select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameParam, Integer ageParam) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (usernameParam != null) {
            booleanBuilder.and(member.username.eq(usernameParam));
        }

        if (ageParam != null) {
            booleanBuilder.and(member.age.eq(ageParam));
        }

        return queryFactory
                .selectFrom(member)
                .where(booleanBuilder)
                .fetch();
    }

    @Test
    void dynamicQuery_whereParam() {
        List<Member> members = searchMember2(null, 10);
        Assertions.assertThat(members.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameParam, Integer ageParam) {

        return queryFactory
                .selectFrom(member)
                .where(allEq(usernameParam, ageParam))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameParam) {
        if (usernameParam == null) {
            return null;
        }
        return member.username.eq(usernameParam);
    }

    private BooleanExpression ageEq(Integer ageParam) {
        if (ageParam == null) {
            return null;
        }
        return member.age.eq(ageParam);
    }

    private Predicate allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond)
                .and(ageEq(ageCond));
    }

    @Test
    void bulkUpdate() {
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.loe(28))
                .execute();

        em.flush();
        em.clear();

        List<Member> result = queryFactory.selectFrom(member)
                .fetch();

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }
    }
}
