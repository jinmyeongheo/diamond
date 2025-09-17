-- 도커 첫 기동 시 자동 실행됨.
-- 목적: 복제 전용 사용자 (최소 권한) 사전 생성
-- 원칙: 운영에서는 반드시 네트워크 범위/호스트 제한, TLS(REQUIRE SSL) 를 병행 권장

CREATE USER IF NOT EXISTS 'repl'@'localhost' IDENTIFIED BY 'replpwd'
  REQUIRE NONE; -- 데모. 운영은 REQUIRE SSL 권장

-- MySQL 8+: REPLICATION SLAVE -> REPLICATION REPLICA로 용어 변경
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'localhost';
-- GRANT REPLICATION REPLICA ON *.* TO 'repl'@'localhost';

-- 연결 품질/복구용 관측을 위해서는 Performance Schema/Information Schema는 자동 접근됨
FLUSH PRIVILEGES;

USE mall;