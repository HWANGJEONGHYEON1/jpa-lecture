package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.order.dto.OrderSearch;
import jpabook.jpashop.domain.order.dto.OrderSearchResponse;

import java.util.List;

public interface OrderRepositorySearch {

    List<OrderSearchResponse> orderSearchList(OrderSearch orderSearch);
}
