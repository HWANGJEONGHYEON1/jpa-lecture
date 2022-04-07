package jpabook.jpashop.repository.order.simplequery;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    //  v3는 엔티티를 조회하는 것이고,
    //
    //v4는 엔티티가 아닌 DTO로 바로 조회하는 방식입니다.
    //
    //fetch join은 JPA에서 지원하는 문법이고, 엔티티를 조회할 때만 사용할 수 있습니다. DTO를 조회할 때는 사용할 수 없습니다.
    //
    //fetch join을 사용하더라도 결국 관계형 데이터베이스에서 연관된 데이터를 조회할 때는 JOIN 구문을 사용하게 됩니다.
    //
    //감사합니다.
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
