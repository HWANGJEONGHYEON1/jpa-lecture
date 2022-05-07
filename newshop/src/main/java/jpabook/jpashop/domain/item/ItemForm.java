package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ItemForm {
    @NotBlank(message = "상품명은 필수 잆니다.")
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
