package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {

    private String name;
    private int price;
    private int stockQuantity;

}
