package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int orderCount;

    public static OrderItem createOrderItem(Item item, int count) {
        item.removeStock(count);
        return OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .orderCount(count)
                .build();
    }
}
