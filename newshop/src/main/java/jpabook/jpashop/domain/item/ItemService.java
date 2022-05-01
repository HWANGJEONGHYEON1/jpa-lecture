package jpabook.jpashop.domain.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long add(ItemForm itemForm) {
        Item item = new Book();
        item.createItem(itemForm);
        final Item savedItem = itemRepository.save(item.createItem(itemForm));
        return savedItem.getId();
    }
}
