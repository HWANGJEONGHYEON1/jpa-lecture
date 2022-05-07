package jpabook.jpashop.domain.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    public Item(ItemForm itemForm) {
        this.name = itemForm.getName();
        this.price = itemForm.getPrice();
        this.stockQuantity = itemForm.getStockQuantity();
    }

    public void updateItem(ItemForm itemForm) {
        this.name = itemForm.getName();
        this.price = itemForm.getPrice();
        this.stockQuantity = itemForm.getStockQuantity();
        updateItemSub(itemForm);
    }

    protected abstract void updateItemSub(ItemForm itemForm);

    public abstract Item createItem(ItemForm itemForm);
}
