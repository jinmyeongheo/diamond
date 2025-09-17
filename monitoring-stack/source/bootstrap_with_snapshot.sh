#!/usr/bin/env bash
set -euo pipefail

SRC=mysql-source
R1=mysql-replica1
R2=mysql-replica2

ROOTPWD=rootpwd
REPLUSER=repl
REPLPWD=repl_pwd

# 1) 소스 데이터 잠금 + binlog 좌표 확보
echo "[1] Lock & capture binlog coordinates on source..."
docker exec -i $SRC bash -lc "
  mysql -uroot -p$ROOTPWD -e 'FLUSH TABLES WITH READ LOCK;'
  mysql -uroot -p$ROOTPWD -e 'SHOW MASTER STATUS\G' | tee /tmp/master_status.txt
"

# 2) 동일 시점 덤프(일관성) 생성
# --single-transaction 은 InnoDB에서 FTWRL 대체 가능하지만, 여기선 개념 전달 위해 FTWRL 사용
echo "[2] Take consistent dump from source..."
docker exec -i $SRC bash -lc "
  mysqldump -uroot -p$ROOTPWD --all-databases --routines --triggers --events --hex-blob > /tmp/full_dump.sql
"

# 3) 잠금 해제 (덤프 완료 후)
echo "[3] Unlock tables..."
docker exec -i $SRC bash -lc "mysql -uroot -p$ROOTPWD -e 'UNLOCK TABLES;'"

# 4) 레플리카에 덤프 반영(초기 동기화)
echo "[4] Apply dump to replicas..."
docker exec -i $SRC bash -lc "sed -n '1,200p' /tmp/master_status.txt"
docker cp $SRC:/tmp/full_dump.sql ./full_dump.sql

for REPL in $R1 $R2; do
  docker cp ./full_dump.sql $REPL:/tmp/full_dump.sql
  docker exec -i $REPL bash -lc "mysql -uroot -p$ROOTPWD < /tmp/full_dump.sql"
done
rm -f ./full_dump.sql

# 5) binlog 좌표 파싱
echo "[5] Parse binlog file/pos from master_status..."
FILE=$(docker exec -i $SRC bash -lc "awk -F: '/File/ {print \$2}' /tmp/master_status.txt | xargs")
POS=$(docker exec -i $SRC bash -lc "awk -F: '/Position/ {print \$2}' /tmp/master_status.txt | xargs")

echo "  -> FILE: $FILE"
echo "  -> POS : $POS"

# 6) replica1: 소스에 붙이기
echo "[6] Configure replica1 -> source..."
docker exec -i $R1 bash -lc "
  mysql -uroot -p$ROOTPWD -e \"
    STOP REPLICA;
    CHANGE REPLICATION SOURCE TO
      SOURCE_HOST='mysql-source',
      SOURCE_PORT=3306,
      SOURCE_USER='${REPLUSER}',
      SOURCE_PASSWORD='${REPLPWD}',
      SOURCE_LOG_FILE='${FILE}',
      SOURCE_LOG_POS=${POS};
    START REPLICA;
  \"
"

# 7) replica2: 체인 토폴로지(레플리카1을 소스로)
echo "[7] Configure replica2 -> replica1 (chain topology example)..."
docker exec -i $R2 bash -lc "
  mysql -uroot -p$ROOTPWD -e \"
    STOP REPLICA;
    CHANGE REPLICATION SOURCE TO
      SOURCE_HOST='mysql-replica1',
      SOURCE_PORT=3306,
      SOURCE_USER='${REPLUSER}',
      SOURCE_PASSWORD='${REPLPWD}',
      SOURCE_AUTO_POSITION=0,
      SOURCE_LOG_FILE='${FILE}',      -- 데모: 동일 시점부터 시작 (replica1과 시점 맞춘 경우)
      SOURCE_LOG_POS=${POS};
    START REPLICA;
  \"
"

# 8) 상태 확인
echo "[8] Replica status:"
docker exec -i $R1 mysql -uroot -p$ROOTPWD -e "SHOW REPLICA STATUS\G" | sed -n '1,60p'
docker exec -i $R2 mysql -uroot -p$ROOTPWD -e "SHOW REPLICA STATUS\G" | sed -n '1,60p'

echo "Done."
