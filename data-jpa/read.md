# Spring jpa

## JpaRepository

`memberRepository.getClass() => com.sun.proxy.$ProxyXXX`

- @Repository 어노테이션 생략 가능
  - JpaRepository<T, type>
- 쿼리 메서드기능 3가지
  - 메소드 이름으로 쿼리생성
    - 조회 : count, query, get, .. By 
    - count : count..By => long
    - exists : exists..By  => boolean
    - 삭제 : deleteBy, removeBy => long
    - distinct : findDistinct, findMemberDistinctBy
    - limit : findFirst3, findTop3
- @Query
  - `@Query("select m from Member m where m.username = :username and m.age = :age")`
  - 어플리케이션 로딩 시점에 잘못된 문자가 있으면 에러 내뱉어줌  
  - DTO, 값 조회
    - @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")

## 페이징
- 페이징과 정렬 파라미터
  - org.springframework.data.domain.Sort : 정렬
  - org.springframework.data.domain.Pageable : 페이징 기능
- 특별한 반환타입
  - Page : 추가 count 쿼리 결과를 포함하는 페이징
  - Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능
  - List : 추가 count 쿼리없이 결과만 반환

```java
        assertThat(content.size()).isEqualTo(3);
        assertThat(totalElements).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
```