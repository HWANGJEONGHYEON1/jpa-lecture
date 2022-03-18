package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member = new Member();
            member.setUsername("회원1");
            member.setAge(1);
            member.setTeam(team);
            em.persist(member);

            Member member1 = new Member();
            member1.setUsername("회원2");
            member1.setAge(2);
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원3");
            member2.setAge(3);
            member2.setTeam(teamB);
            em.persist(member2);

            em.flush();
            em.clear();

            // 패치 조인으로 회원과 팀을 함께 조회해서 지연로딩 X

            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        emf.close();
    }

}
