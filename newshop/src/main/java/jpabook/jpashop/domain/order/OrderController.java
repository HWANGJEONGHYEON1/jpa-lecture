package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.item.ItemRepository;
import jpabook.jpashop.domain.member.MemberRepository;
import jpabook.jpashop.domain.order.dto.OrderForm;
import jpabook.jpashop.domain.order.dto.OrderSearch;
import jpabook.jpashop.domain.order.domain.Order;
import jpabook.jpashop.domain.order.dto.OrderSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/order")
    public String orderForm(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(Model model,
                        OrderForm orderForm) {
        orderService.order(orderForm);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orders(Model model, @ModelAttribute OrderSearch orderSearch) {
        List<OrderSearchResponse> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancel(Model model, @PathVariable("id") Long id) {
        orderService.cancel(id);
        return "redirect:/orders";
    }
}
