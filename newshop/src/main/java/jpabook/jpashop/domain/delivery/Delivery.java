package jpabook.jpashop.domain.delivery;

import jpabook.jpashop.domain.member.Address;

import javax.persistence.*;

@Entity
public class Delivery {

    @GeneratedValue
    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;
}
