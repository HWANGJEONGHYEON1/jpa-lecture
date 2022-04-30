package jpabook.jpashop.domain.member;

import jpabook.jpashop.domain.member.dto.MemberForm;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Member {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    @Embedded
    private Address address;

    public static Member createMember(MemberForm memberForm) {
        return Member.builder()
                .name(memberForm.getName())
                .address(new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode()))
                .build();
    }

//    private List<Order> orders = new ArrayList<>();
}
