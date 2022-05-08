package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.delivery.Delivery;
import jpabook.jpashop.domain.member.Address;
import jpabook.jpashop.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static Order createOrder(Member member, OrderItem... orderItem) {
        Delivery delivery = new Delivery(member.getAddress());

        final Order order = Order.builder()
                .delivery(delivery)
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .orderDate(LocalDateTime.now())
                .build();



        return order;
    }
}
