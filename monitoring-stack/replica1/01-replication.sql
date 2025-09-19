-- 세미싱크 플러그인 로드
INSTALL PLUGIN rpl_semi_sync_replica SONAME 'semisync_replica.so';
SET GLOBAL rpl_semi_sync_replica_enabled = ON;

-- GTID 기반 복제 연결 (AUTO_POSITION=1)
CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='mysql-source',
  SOURCE_PORT=3306,
  SOURCE_USER='repl',
  SOURCE_PASSWORD='replpwd',
  SOURCE_AUTO_POSITION=1;

-- 복제 시작
START REPLICA;

-- 확인용
SELECT @@gtid_mode, @@enforce_gtid_consistency, @@read_only, @@super_read_only,
       @@rpl_semi_sync_replica_enabled;
