# JPA 살펴보기

## 라이브러리
- 의존관계 확인
  - ./gradlew dependencies
- 핵심 라이브러리
  - 스프링 MVC
  - 스프링 ORM
  - JPA, 하이버네이트
  - 스프링 데이터 JPA
- 기타라이브러리
  - H2
  - HikariCP : 커넥션 풀
  - 타임리프
  - 로깅
  - 테스트
  - implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6' 
    - ? ? 파라미터 확인 (쿼리)

## java jar 사용하여 확인하기
1. ./gradlew clean build
2. cd bulid
3. java -jar ~~.jar

## 요구사항 분석
> 기능목록

- 회원 기능
  - 등록
  - 조회
- 상품 기능
  - 등록
  - 수정
  - 조회
- 주문 기능
  - 상품 주문
  - 주문내역 조회
  - 주문 취소
- 기타
  - 상품은 제고 관리가 필요
  - 상품 종류 도서 음반 영화
  - 상품 카테고리로 구분
  - 상품 주문 시 배송 정보 입력