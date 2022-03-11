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

            Member member = new Member();
            member.setUsername("Hello");
            em.persist(member);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member.getId());
            System.out.println("members.getClass() = " + refMember.getClass());

            Member findMember = em.find(Member.class, refMember.getId());
            System.out.println("findMember.getClass() = " + findMember.getClass());

            System.out.println("a == a : " + (refMember == findMember));

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
