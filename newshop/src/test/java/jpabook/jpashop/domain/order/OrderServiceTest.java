
package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.item.*;
import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.domain.member.MemberRepository;
import jpabook.jpashop.domain.member.MemberService;
import jpabook.jpashop.domain.member.dto.MemberForm;
import jpabook.jpashop.domain.order.dto.OrderForm;
import jpabook.jpashop.domain.order.domain.Order;
import jpabook.jpashop.exception.NotEnoughStockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    MemberService memberService;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setup() {
        createItem();

        createMember();
    }

    private void createMember() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName("JPA");
        memberForm.setCity("city");
        memberForm.setStreet("street");
        memberForm.setZipcode("zipcode");
        memberService.join(memberForm);
    }

    private void createItem() {
        ItemForm itemForm = new ItemForm();
        itemForm.setAuthor("aa");
        itemForm.setIsbn("bb");
        itemForm.setPrice(10000);
        itemForm.setStockQuantity(10);
        itemForm.setName("JPA 연습중인 책");
        itemService.add(itemForm);
    }

    @Test
    @DisplayName("상품 주문")
    void order() {
        Item item = itemRepository.findByName("JPA 연습중인 책");
        Member member = memberRepository.findByName("JPA");

        OrderForm orderForm = getOrderForm(item, member);

        Long orderId = orderService.order(orderForm);

        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(NotEnoughStockException::new);

        Assertions.assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        Assertions.assertThat(findOrder.getMember()).isEqualTo(member);
        Assertions.assertThat(findOrder.getOrderItems().get(0).getCount()).isEqualTo(5);
    }

    private OrderForm getOrderForm(Item item, Member member) {
        OrderForm orderForm = new OrderForm();
        orderForm.setCount(5);
        orderForm.setItemId(item.getId());
        orderForm.setMemberId(member.getId());
        return orderForm;
    }

    @Test
    @DisplayName("주문 취소")
    void order_cancel() {
        Item item = itemRepository.findByName("JPA 연습중인 책");
        Member member = memberRepository.findByName("JPA");

        OrderForm orderForm = getOrderForm(item, member);

        Long orderId = orderService.order(orderForm);
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(NotEnoughStockException::new);

        Assertions.assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);

        orderService.cancel(orderId);
        Assertions.assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }
}
