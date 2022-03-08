package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Movie movie = new Movie();
            movie.setDirector("A");
            movie.setActor("ACTOR");
            movie.setName("바람과함께사라지다");
            movie.setPrice(10000);
            em.persist(movie);

            em.flush(); // flush를 하여 영속성 컨텍스트에 쿼리를날려 싱크를 맞춤
            em.clear(); // 영속성 컨텍스트를 완전 초기화

            em.find(Movie.class, movie.getId());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        emf.close();
    }
}
