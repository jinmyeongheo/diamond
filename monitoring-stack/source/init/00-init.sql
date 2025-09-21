-- gtid, rbr, 하나의 source(읽기,쓰기), 하나의 replic(백업용), semi async
-- semi async는 plugin 이슈
-- 도커 첫 기동 시 자동 실행됨.
-- 목적: 복제 전용 사용자 (최소 권한) 사전 생성
-- 원칙: 운영에서는 반드시 네트워크 범위/호스트 제한, TLS(REQUIRE SSL) 를 병행 권장

CREATE USER IF NOT EXISTS 'repl'@'%' IDENTIFIED BY 'replpwd' REQUIRE NONE; -- 데모. 운영은 REQUIRE SSL 권장

CREATE USER IF NOT EXISTS 'exporter'@'%' IDENTIFIED BY 'exporterpwd' WITH MAX_USER_CONNECTIONS 3;
GRANT PROCESS, REPLICATION CLIENT, SELECT ON *.* TO 'exporter'@'%';

-- 2) 복제 권한 부여
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';

-- (선택) 모니터링용
GRANT REPLICATION CLIENT ON *.* TO 'repl'@'%';

FLUSH PRIVILEGES;




