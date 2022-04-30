package jpabook.jpashop.domain.member;

import jpabook.jpashop.domain.member.dto.MemberForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String memberForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String memberSave(@Valid MemberForm memberForm,
                             BindingResult bindingResult) {
        log.info("# save");
        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        memberService.join(memberForm);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String members(Model model) {
        final List<Member> members = memberRepository.findAll();

        if (CollectionUtils.isEmpty(members)) {
            model.addAttribute("noMembers", "사용자가없습니다.");
        }

        model.addAttribute("members", members);
        return "members/memberList";
    }
}
