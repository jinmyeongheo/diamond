-- 세미싱크 플러그인 로드
-- INSTALL PLUGIN rpl_semi_sync_replica SONAME 'semisync_replica.so';
-- SET GLOBAL rpl_semi_sync_replica_enabled = ON;

-- GTID 기반 복제 연결 (AUTO_POSITION=1)
CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='source',
  SOURCE_PORT=3306,
  SOURCE_USER='repl',
  SOURCE_PASSWORD='replpwd',
  SOURCE_AUTO_POSITION=1,
  SOURCE_SSL=1,                         -- ★ TLS로 연결
  SOURCE_SSL_VERIFY_SERVER_CERT=0;      -- ★ 데모: 서버 인증서 검증 생략 (자체서명/CA 미배포 시 편의)
--  테스트 환경이 기때문에 ca검증,
-- 권장: START REPLICA에서 USER/PASSWORD 넘기면 메타데이터 경고 사라짐
START REPLICA USER='repl' PASSWORD='replpwd';