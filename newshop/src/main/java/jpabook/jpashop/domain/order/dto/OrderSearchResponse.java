package jpabook.jpashop.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import jpabook.jpashop.domain.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
public class OrderSearchResponse {
    private Long orderId;
    private String memberName;
    private String itemName;
    private int price;
    private int count;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;

    @QueryProjection
    public OrderSearchResponse(Long orderId, String memberName, String itemName, int price, int count, OrderStatus orderStatus, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }
}
