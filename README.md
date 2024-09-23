# spring 2.6.2
# java 1.8
# security 5.2.2

- https://blog.gangnamunni.com/post/isolate-transaction-from-domain-model/?utm_source=oneoneone
- https://oneoneone.kr/content/fa9dff53
- https://sup2is.github.io/2020/01/29/java-immutable-object-with-string.html
- https://vladmihalcea.com/spring-transactional-annotation/
- https://velog.io/@ur2e/String%EC%9D%80-%EC%99%9C-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%B6%88%EB%B3%80-%EA%B0%9D%EC%B2%B4%EC%9D%B8%EA%B0%80%EC%9A%94-String-Constant-Pool
- https://vladmihalcea.com/spring-transactional-annotation/
- https://jangjjolkit.tistory.com/41
- https://findstar.pe.kr/2023/04/17/java-virtual-threads-1/
- https://techblog.woowahan.com/15398/
- https://velog.io/@sontulip/web-performance-budget
- https://d2.naver.com/helloworld/1286587
- https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%ED%85%9C%ED%94%8C%EB%A6%BF-%EB%A9%94%EC%86%8C%EB%93%9CTemplate-Method-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90
- https://velog.io/@sihyung92/how-does-springboot-handle-multiple-requests
- https://heowc.dev/en/2019/02/09/using-mysql-jdbc-to-handle-large-table-1/
- https://mariadb.com/kb/en/update-vs-delete-and-insert/
- https://serce.me/posts/18-11-2020-allocate-direct
- https://brewagebear.github.io/fundamental-jvm-classloader/
- https://dd-developer.tistory.com/110
- https://wonit.tistory.com/589
- https://medium.com/@unmeshvjoshi/how-java-thread-maps-to-os-thread-e280a9fb2e06
- https://stackoverflow.com/questions/15983872/difference-between-user-level-and-kernel-supported-threads
- https://stackoverflow.com/questions/16264118/how-jvm-stack-heap-and-threads-are-mapped-to-physical-memory-or-operation-syste
- https://github.com/YAPP-Github
- https://velog.io/@wisepine/JPA-%EC%82%AC%EC%9A%A9-%EC%8B%9C-19%EA%B0%80%EC%A7%80-Tip
- https://ppaksang.tistory.com/13
- https://data-make.tistory.com/714
- https://velog.io/@kasania/API-%EC%9E%90%EB%8F%99%ED%99%94-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EB%8F%84%EC%9E%85%EA%B8%B0
- https://velog.io/@ggong/Github-Action%EC%97%90-%EB%8C%80%ED%95%9C-%EC%86%8C%EA%B0%9C%EC%99%80-%EC%82%AC%EC%9A%A9%EB%B2%95



1. 데이터 암호화
   전송 중 암호화:
   HTTPS를 사용하여 클라이언트와 서버 간의 데이터를 암호화하여 전송합니다.
   SSL/TLS 인증서 설정 (Spring Boot 예시)
   Spring RestTemplate이나 WebClient와 같은 HTTP 클라이언트를 사용할 때에도 HTTPS를 통해 통신이 이루어져야 하며, SSL 인증서를 검증하도록 설정   
   저장 시 암호화: 데이터베이스에 민감한 정보를 저장할 때 암호화된 형태로 저장합니다. 예를 들어, 사용자 비밀번호는 일반적으로 해시(Hash) 함수(SHA-256, bcrypt)를 사용해 저장합니다.
2. 액세스 제어
   권한 관리: 어드민 사용자 계정에 따라 역할 기반 권한을 설정해, 필요한 최소한의 정보만 접근할 수 있도록 제한합니다.
   로그인 보안 강화: 어드민 페이지 접근 시 강력한 인증 방식을 사용하고, 이중 인증(2FA)을 도입하는 것이 좋습니다.
   이중 인증(sms, email, otp)
   CAPTCHA (봇에 의한 로그인 시도 차단)
   비정상 로그인 감지 및 알림
   로그인 실패 횟수 제한: 일정 횟수 이상 비밀번호를 잘못 입력하면 계정을 잠그거나, 추가 인증(예: CAPTCHA)을 요구합니다.
   비정상적인 로그인 시도 탐지: 동일한 IP 주소에서 다수의 로그인 시도가 발생하거나, 짧은 시간 내에 여러 번 로그인에 실패하면 관리자에게 경고를 보내거나, 사용자의 계정을 일시적으로 잠급니다.
   로그인 알림: 사용자가 새로운 장치나 위치에서 로그인할 경우 이를 감지하고 사용자에게 알림을 보내 보안 침해를 방지할 수 있습니다.
   세션 관리
   세션 만료: 사용자가 일정 시간 동안 활동하지 않으면 세션을 만료시키고 재로그인을 요구합니다.
   동시 세션 제한: 한 계정에서 여러 곳에서 동시 접속하는 것을 제한하거나 관리할 수 있습니다.
   Session Fixation 방지: 로그인 성공 후 세션을 새로 발급하여 공격자가 이미 발급받은 세션 ID로 인증을 시도하는 것을 방지합니다. Spring Security에서는 기본적으로 이 기능이 활성화되어 있습니다.
   서버 사이드 입력 검증 및 XSS/CSRF 방어
   CSRF 방어: 로그인 요청 시 CSRF 토큰을 사용해 사이트 간 요청 위조(Cross-Site Request Forgery) 공격을 방지합니다. Spring Security에서는 기본적으로 CSRF 보호가 활성화되어 있습니다.
   XSS 방어: 로그인 폼의 입력 필드를 철저히 검증하고, 서버에서 스크립트 주입을 막기 위한 보안 조치를 적용합니다.
   사용자 계정 잠금 및 재활성화
   비밀번호 입력 실패 시 계정 잠금 : 로그인 실패 횟수가 많으면 계정을 일정 시간 동안 잠그거나, 사용자가 비밀번호를 재설정하도록 강제합니다
   관리자 승인 후 계정 재활성화: 계정 잠금 해제를 위해 관리자의 승인을 요구하거나 이중 인증을 통해 재활성화하는 절차를 추가할 수 있습니다.
3. 로깅 및 모니터링
   접근 로그 기록: 민감한 데이터에 접근하는 모든 행위를 로그로 남기고, 이상 징후가 발견될 경우 이를 모니터링할 수 있는 시스템을 구축합니다.
   실시간 알림: 데이터 유출이나 비정상적인 접근 시 실시간으로 관리자에게 알림이 가도록 설정합니다.
4. 민감 데이터 노출 방지
   마스킹: 예를 들어, 주민등록번호나 신용카드 번호와 같은 데이터는 전부 보여주지 않고, 일부만 표시되도록 합니다. (1234-****-****-5678).
   불필요한 데이터 요청 방지: 어드민 페이지에서 절대 불필요하게 민감한 데이터를 요청하지 않도록 하고, 필요한 경우에만 조회 권한을 부여합니다.
5. CSRF, XSS 방지
   CSRF (Cross-Site Request Forgery) 방지: CSRF 토큰을 사용하여 요청의 정당성을 검증합니다.
   XSS (Cross-Site Scripting) 방지: 입력 데이터를 철저히 검증하고 인코딩해, 사용자 입력으로부터 악성 스크립트가 실행되지 않도록 합니다.
6. 데이터 최소화 원칙
   최소한의 정보만 수집: 어드민 페이지에서도 필요한 정보만 수집하고, 더 이상 필요하지 않다면 데이터를 삭제합니다.
   민감 정보 캐싱 금지: 브라우저나 서버에서 민감 정보가 캐시되지 않도록 설정합니다.
7. 검색기능(페이지네이션) 개선, 대량 엑셀다운로드, 데이터 일괄 업데이트
8. 파일 업로드: CSV, 이미지 등의 파일을 서버에 업로드할 수 있는 기능.
   파일 검증: 파일 업로드 시 파일 형식, 크기 제한 등을 검증하여 악성 파일 업로드 방지.
   S3, Cloud Storage 연동: 파일을 외부 저장소(S3, Azure Blob 등)에 저장하는 기능.
9. 보안 취약성 테스트
   OWASP의 보안 가이드라인을 준수하며, 침투 테스트(Penetration Test)로 취약점을 사전에 파악
10. 유닛 테스트 및 통합 테스트
    로그인 및 인증 관련 기능에 대해 철저한 유닛 테스트와 통합 테스트
11. 서버 부하 관리(?)