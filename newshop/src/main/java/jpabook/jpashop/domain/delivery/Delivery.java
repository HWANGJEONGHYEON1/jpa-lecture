package jpabook.jpashop.domain.delivery;

import jpabook.jpashop.domain.member.Address;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Delivery {

    @GeneratedValue
    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    public Delivery(Address address) {
        this.address = address;
    }
}
