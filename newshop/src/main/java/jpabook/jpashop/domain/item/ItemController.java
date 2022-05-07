package jpabook.jpashop.domain.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping("/items/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

        final Item updateItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정해야할 ID가 없습니다."));

        model.addAttribute("form", updateItem);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{id}/edit")
    public String edit(ItemForm itemForm,
                       @PathVariable Long id,
                       Model model,
                       RedirectAttributes attributes) {
        itemService.edit(itemForm, id);
        model.addAttribute("items", itemRepository.findAll());
        return "items/itemList";

    }

    @GetMapping("/items")
    public String items(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "items/itemList";
    }

    @GetMapping("/items/new")
    public String form(Model model) {
        model.addAttribute("form", new ItemForm());
        // 영화, 책, 앨범을 셀렉트 박스로 넣어야함. 각 타입과 같이? 아니면 아이디? => 나중으로 일단 기능 구현
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
