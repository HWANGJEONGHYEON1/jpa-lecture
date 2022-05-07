package jpabook.jpashop.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setup() {
        ItemForm itemForm = new ItemForm();
        itemForm.setName("상품");
        itemForm.setAuthor("저자");
        itemForm.setIsbn("12");
        itemForm.setPrice(10000);
        itemForm.setStockQuantity(100);

        itemService.add(itemForm);
    }

    @Test
    @DisplayName("상품 등록")
    void save() {
        ItemForm itemForm = new ItemForm();
        itemForm.setName("상품1");
        itemForm.setAuthor("저자1");
        itemForm.setIsbn("12");
        itemForm.setPrice(10000);
        itemForm.setStockQuantity(100);

        final Long savedId = itemService.add(itemForm);

        Assertions.assertThat(savedId).isEqualTo(itemRepository.findById(savedId).get().getId());
    }

    @Test
    @DisplayName("상품 수정")
    void edit() {

        ItemForm itemForm = new ItemForm();
        itemForm.setName("상품");
        itemForm.setAuthor("저자1");
        itemForm.setIsbn("12");
        itemForm.setPrice(20000);
        itemForm.setStockQuantity(200);

        Item item = itemRepository.findByName("상품");

        itemService.edit(itemForm, item.getId());

        Item updateItem = itemRepository.findByName("상품");

        Assertions.assertThat(updateItem.getPrice()).isEqualTo(20000);
        Assertions.assertThat(updateItem.getStockQuantity()).isEqualTo(200);
    }
}