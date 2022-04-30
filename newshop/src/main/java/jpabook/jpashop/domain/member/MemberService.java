package jpabook.jpashop.domain.member;

import jpabook.jpashop.domain.member.dto.MemberDto;
import jpabook.jpashop.domain.member.dto.MemberForm;
import jpabook.jpashop.exception.AlreadyExistMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(MemberForm memberForm) {
        validateMember(memberForm);
        Member member = Member.createMember(memberForm);
        return memberRepository.save(member).getId();
    }

    private void validateMember(MemberForm memberForm) {
        final Member findMember = memberRepository.findByName(memberForm.getName());
        if (findMember != null) {
            throw new AlreadyExistMemberException("이미 존재하는 회원입니다.");
        }
    }

    public List<MemberDto> findAllMembers() {
        final List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> new MemberDto(member.getName(), member.getAddress()))
                .collect(Collectors.toList());
    }
}
