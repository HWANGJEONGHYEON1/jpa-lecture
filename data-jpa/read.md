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

## JPA Hint 
- JPA 쿼리 힌트
- `@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))`

## 확장기능
- 사용자 정의 리포지토리 구현
  - JPA 직접 사용
  - 스프링 jdbc template
  - mybatis
  - query dsl
- MemberRepositoryCustom => MemberRepositoryCustomImpl
  - impl 관례를 따르자.

## Auditing
- 엔티티를 생성, 변경할 때 사람과 시간을 추적
  - 등록일, 수정일, 등록자, 수정자

## page
- 설정

```
yml
  data:
    web:
      pageable:
        default-page-size: 10
        max-page-size: 2000

java 

    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }        
```

## 스프링 데이터 jpa
- @Repository : JPA 예외를 스프링이 추상화한 예외로 변환
- @Transactional : 트랜잭션 적용
  - JPA의 모든 변경은 트랜잭션 안에서 동작
  - 스프링 데이터 JPA 변경 메서드를 트랜잭션 처리
  - 서비스 계층에 @Transactional 있다면 서비스 계층부터 시작
  - 서비스 계층에서 트랜잭션이 없다면 레파지토리에서 시작
  - 트랜잭션이 없어도 등록 변경 가능(JpaRepository의 구현체가 @Transactional을 선언)
  - @Transactional(readOnly = true)
    - 플러시를 생략하기 때문에 성능향상 
- `save()` 메서드
  - 새로운 엔티티면 persist
  - 이미 있으면 merge
    - select를 한번 하고 저장
  - 새로운 엔티티 구별하는 방법
    - 새로운 엔티티를 판단하는 기본 전략
    - 식별자가 객체일 때 null 로 판단
    - 식별자가 자바 기본 타입일 때 0 으로 판단
    - Persistable 인터페이스를 구현해서 판단 로직 변경 가능