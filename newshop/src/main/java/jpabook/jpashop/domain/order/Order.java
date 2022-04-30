package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.delivery.Delivery;
import jpabook.jpashop.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}
