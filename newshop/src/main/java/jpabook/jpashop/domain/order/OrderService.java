package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.ItemRepository;
import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.domain.member.MemberRepository;
import jpabook.jpashop.domain.order.dto.OrderForm;
import jpabook.jpashop.domain.order.domain.Order;
import jpabook.jpashop.domain.order.domain.OrderItem;
import jpabook.jpashop.domain.order.dto.OrderSearch;
import jpabook.jpashop.domain.order.dto.OrderSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(OrderForm orderForm) {
        List<OrderItem> orderItems = orderForm.getItemId()
                .stream()
                .map(id -> itemRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("일치하는 itemID가 없습니다.")))
                .map(item -> OrderItem.createOrderItem(item, orderForm.getCount())) // 상품주문
                .collect(Collectors.toList());
        final Member member = memberRepository.findById(orderForm.getMemberId())
                .orElseThrow(() -> new RuntimeException("일치하는 memberID가 없습니다."));

        // 상품주문

        // 주문
        Order order = Order.createOrder(member, orderItems);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    public List<OrderSearchResponse> findOrders(OrderSearch orderSearch) {
        return orderRepository.orderSearchList(orderSearch);
    }

    @Transactional
    public void cancel(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일치하는 itemID가 없습니다."))
                .change(OrderStatus.CANCEL);
    }
}
