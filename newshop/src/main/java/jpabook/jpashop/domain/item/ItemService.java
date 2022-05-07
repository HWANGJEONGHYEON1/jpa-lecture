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
        final Item savedItem = itemRepository.save(item.createItem(itemForm));
        return savedItem.getId();
    }

    @Transactional
    public void edit(ItemForm itemForm, Long id) {
        final Item item = itemRepository.findById(id)
                .get();
        item.updateItem(itemForm);
    }
}
