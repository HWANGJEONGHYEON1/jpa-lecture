package jpabook.jpashop.domain.member.dto;

import jpabook.jpashop.domain.member.Address;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberDto {
    private String name;

    private Address address;

    public MemberDto(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
