
# JPA

## ORM
    Object-relational mapping  
    객체는 객체대로
    관계형 디비는 관계형 디비대로 설계
    ORM 프레임워크가 중간에서 매핑
    대중적인 언어에는 대부분 ORM 기술이 존재
    
## JPA는 모든 트랜잭션 안에서 실행된다.

## JPQL
- `em.createQuery("select m from Member as m", Member.class); // Member 객체를 대상으로 쿼리를 수행`
- 앤티티 객체를 중심으로 개발
- 문제는 검색 쿼리
- 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
- 모든 DB 데이터를 개체로 변환해서 검색하는 것은 불가능
- 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요


    JPA는 SQL을 추상화한 JPQL 객체 지향 쿼리 언어 제공
    SQL 문법과 유사, SELECT, From, WHERE, GROUP BY, HAVING, JOIN 지원
    앤티티 객체를 대상으로 쿼리

## 영속성 관리

### 영속성 컨텍스트
- 엔티티를 영구 저장하는 환경
- `EntityManager.persist(entity);`
- 논리적인 개념
- 앤티티 매니저를 통해 영속성 컨텍스트를 접근
- 엔티티 생명주기
  - 비영속
    - 영속성 컨텍스트와 전혀 관계없는 새로우 상태
  - 영속
    - 영속성 컨텍스트에 관리되는 상태
  - 준영속
    - 영속성 컨텍스트에 저장되었다가 분리된 상태 
    - em.detach(객체) -> 영속성 컨텍스트에서 관리하기 때문에 jpa가 관리하지 않는다.
    - em.clear(); -> 영속성 컨텍스트를 비움 -> 1차캐시지우고싶을 때, 테스트케이스 눈으로 보고 싶을때
  - 삭제
- 이점
  - 1차 캐시
    - 성능의 이점을 큰 장점은 없다 => 같은 트랜잭션 내에서만 공유하기 때문
    ```java
            em.persist(member);
            Member findMember = em.find(Member.class, 100L);
            System.out.println("findMember = " + findMember.getName()); // 조회용 Sql이 나가나? => 1차캐시에 저장되어 조회쿼리는 안나옴
    ```
  - 동일성 보장
    - 위의 코드에서 em.find를 두번해도 같은 객체로 본다.
  - 트랜잭션을 지원하는 쓰기 지연
  - 변경감지
    - 마치 자바컬렉션처럼, 이루어진다
    1. 내부적으로 flush() 실행
    2. 엔티티와 기존 일차캐시 스냅샷 값 비교
    3. 쓰기 지연 update sql 생성
    4. flush
  - 지연로딩

### flush
> 영속성 컨텍스트의 변경내용을 DB에 반영  
> 영속성 컨텍스트를 비우는 것은 아님  
- 변경 감지
- 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
- 쓰기 지연 SQL 쿼리를 DB에 전송(등록, 수정, 삭제)
- 플러시 하는 방법
  - em.flush() 직접 호출
  - 트랜잭션 커밋 시
  - JPQL 쿼리 실행 시

## 객체와 테이블 매핑
> @Entity
- JPA가 관리, 엔티티
- 테이블과 매핑할 클래스는 @Entity필수
- **주의**
  - 기본 생성자 필수
  - final 클래스, enum, interface, inner 클래스 사용 X
  - 저장할 필드에 final 사용 x

## 데이터 베이스 스키마 자동생성
```<property name="hibernate.hbm2ddl.auto" value="create" />```
- DDL을 애플리케이션 실행 시점에 자동생성
- 테이블 중심 -> 객체 중심
- DB 방언을 활용해서 적절한 DB에 맞는 DDL 생성
- 생성된 DDL은 개발 환경에서만 적용
- 생성된 DDL은 운영서버에서 사용하면 안된다.
- 주의점
  - 운영에는 create, create-drop, update는 절대 안된다.
  - 개발 초기단계는 update, create
  - 테스트 서버는 update 또는 validate
  - 스테이징과 운영서버는 validate or none

### @Column
- insertable, updatable : 등록 변경 가능여부
- nullable : NOTNULL 제약조건
- unique : 간단히 유니크 제약조건을 만드는대 잘 사용하지 않음 => 이름이 랜덤으로 생성됨

### @Enumerated
- ORDINAL : Enum이 자리가 바뀌거나 데이터가 바뀌게 되면 디비에는 순서만으로 들어가기 때문에 보기 어렵다
- STRING : 문자 자체로 디비에 들어간다.

## 기본 키 매핑
- Identity 전략
  - 기본 키 생성을 데이터베이스에 위임
  - 주로 auto_increment 전략
  - JPA는 보통 트랜잭션 커밋시점에 insert sql 실행
  - auto_increment는 DB에 insert 후 알 수 있음
  - 이 전략은 em.persist() 시점에 즉시 insert 후 DB에서 식별자 조회