package jpabook.jpashop.domain.item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItemService {


    public Long add(ItemForm itemForm) {
        return null;

    }
}
