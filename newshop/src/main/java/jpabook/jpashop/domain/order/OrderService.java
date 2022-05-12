package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.delivery.Delivery;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.ItemRepository;
import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(OrderForm orderForm) {
        final Item item = itemRepository.findById(orderForm.getItemId())
                .orElseThrow(() -> new RuntimeException("일치하는 itemID가 없습니다."));
        final Member member = memberRepository.findById(orderForm.getMemberId())
                .orElseThrow(() -> new RuntimeException("일치하는 memberID가 없습니다."));

        // 상품주문
        final OrderItem orderItem = OrderItem.createOrderItem(item, orderForm.getCount());

        // 주문
        Order order = Order.createOrder(member, orderItem);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void cancel(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일치하는 itemID가 없습니다."))
                .change(OrderStatus.CANCEL);
    }
}
