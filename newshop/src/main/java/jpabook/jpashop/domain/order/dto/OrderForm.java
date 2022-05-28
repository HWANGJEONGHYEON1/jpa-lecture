package jpabook.jpashop.domain.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderForm {

    private Long memberId;
    private Long itemId;
    private int count;

    public List<Long> getItemId() {
        return Arrays.asList(itemId);
    }
}
