package jpabook.jpashop.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchItems {
    private Long itemId;
    private int price;
    private int count;
}
