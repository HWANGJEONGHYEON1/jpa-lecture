package jpabook.jpashop.domain.order;

import jpabook.jpashop.domain.item.ItemRepository;
import jpabook.jpashop.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

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
        return "";
    }

}
