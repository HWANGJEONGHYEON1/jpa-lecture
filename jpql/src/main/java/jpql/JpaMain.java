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
            team.setName("team1");
            em.persist(team);

            Member member = new Member();
            member.setUsername("Member " + 1);
            member.setAge(1);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            List<Member> resultList = em.createQuery("select m, t from Member m join m.team t ", Member.class)
                    .getResultList();

            System.out.println("result.size : " + resultList.size());
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        emf.close();
    }

}
