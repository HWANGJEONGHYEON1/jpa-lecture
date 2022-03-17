# JPQL


## JPQL 문법
- 엔티티와 속성은 대소문자 구분 (Member, age)
- JPQL 키워드는 대소문자 구분 x (select, from where)
- 엔티티 이름 사용 => 테이블이 아님
- 별칭인 필수
- TypeQuery => 반환타입이 명확할 때
- Query => 불명확
- 결과 조회 API
  - query.getResultList() : 결과가 하나 이상일때, 비었다면 빈 리스트 반환
  - query.getSingleResult() : 결과가 하나일 때, 없거나 많다면 예외 발생

## 프로젝션
- select 절에 조회할 대상을 지정하는 것
- 엔티티, 임베디드 타입, 숫자, 문자 등
- select m from Member m -> 엔티티 프로젝션
- select m.team from Member m -> 엔티티 프로젝션
- select m.address from Member m -> 임베디드
- select m.username, m.age from Member m -> 스칼라 프로젝션

## 페이징
- JPA는 페이징을 두 API 추상화
  - setFirstResult : 조회 시작 위치(0부터 시작)
  - setMaxResults : 조회할 데이터 수 

## 조인
- on
  - 조인 대상 필터링
  - 연관관계 없는 엔티티 외부 조인

## 서브쿼리
- JPA는 WHERE, HAVING 절에서만 서브쿼리 사용가능
- SELECT도 지원(하이버네이트에서 지원)
- FROM 절에서 서브쿼리는 불가능
  - `조인으로 해결` 
  
