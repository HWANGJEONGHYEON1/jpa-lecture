package jpabook.jpashop.domain.category;

import jpabook.jpashop.domain.item.Item;

import javax.persistence.*;

@Entity
public class CategoryItem {

    @GeneratedValue @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
