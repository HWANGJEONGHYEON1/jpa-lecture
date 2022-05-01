package jpabook.jpashop.domain.item;

import org.assertj.core.api.Assertions;
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

    @Test
    @DisplayName("상품등록")
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

}