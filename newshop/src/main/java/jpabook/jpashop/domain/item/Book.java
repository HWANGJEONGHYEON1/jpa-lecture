package jpabook.jpashop.domain.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {
    private String author;
    private String isbn;

    private Book(ItemForm itemForm) {
        super(itemForm);
        this.author = itemForm.getAuthor();
        this.isbn = itemForm.getIsbn();
    }

    @Override
    protected void updateItemSub(ItemForm itemForm) {
        this.author = itemForm.getAuthor();
        this.isbn = itemForm.getIsbn();
    }

    @Override
    public Item createItem(ItemForm itemForm) {
        return new Book(itemForm);
    }
}
