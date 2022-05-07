package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M")
public class Movie extends Item {
    private String director;
    private String actor;

    @Override
    protected void updateItemSub(ItemForm itemForm) {

    }

    @Override
    public Item createItem(ItemForm itemForm) {
        return null;
    }
}
