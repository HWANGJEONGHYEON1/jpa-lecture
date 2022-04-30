package jpabook.jpashop.domain.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String form(Model model) {
        model.addAttribute(new ItemForm());
        // 영화, 책, 앨범을 셀렉트 박스로 넣어야함. 각 타입과 같이? 아니면 아이디?
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String save(@Valid ItemForm itemForm, BindingResult result) {
        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        itemService.add(itemForm);
        return "redirect:/";
    }
}
