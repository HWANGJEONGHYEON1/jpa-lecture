package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("TeamA");
            em.persist(team2);

            Member member = new Member();
            member.setUsername("Hello");
            member.setTeam(team);

            Member member1 = new Member();
            member1.setUsername("Hello");
            member1.setTeam(team2);

            em.persist(member);
            em.persist(member1);

            em.flush();
            em.clear();

            List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        emf.close();
    }

    private static void printMemberAndTeam(Member findMember) {
        String username = findMember.getUsername();
        Team team = findMember.getTeam();
        System.out.println("username = " + username);
        System.out.println("team = " + team.getName());
    }
}
