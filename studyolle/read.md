# 백기선님 강의(스프링과 JAP 기반 웹 개발)


## 회원가입 정석 가입 절차 (시큐리티)

```java
    // 시큐리티에 빈으로 정의되어있어 주입을 받은 후
    private final AuthenticationManager authenticationManager;
    // 메서드영역
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
    // 매니저를 통해 인증을 거친다.
    Authentication authenticate = authenticationManager.authenticate(token);

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authenticate);
```

## 테스트에서 final 사용하여 생성자 주입 못쓰는이유
- Junit이 먼저 개입하여 생성자로 받을 수 없어 @Autowired를 써야함.

## 현재 안전한 쿠키 보관 방법
- username, 토큰(랜덤, 매번바뀜), 시리즈(랜덤, 고정) => 쿠키
- 쿠키 탈취 -> 희생자는 유효하지않은 토큰과 유효한 시리즈와 username으로 접속
- 이렇게 되면 모든 토큰을 삭제하여 해커가 더이상 탈취한 쿠키를 사용하지 못하도록 방지한다.