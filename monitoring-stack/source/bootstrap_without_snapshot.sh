#!/usr/bin/env bash
set -euo pipefail

SRC=mysql-source
R1=mysql-replica1
#R2=mysql-replica2

ROOTPWD=rootpwd
REPLUSER=repl
REPLPWD=replpwd

# 데이터가 거의 없거나, 초기 구축이라 맞출 것이 없다는 가정
# 실무에서는 "반드시" 스냅샷 맞춘 뒤 시작하세요.

# binlog 현재 좌표 획득
FILE=$(docker exec -i $SRC bash -lc "mysql -u root -p $ROOTPWD -e 'SHOW MASTER STATUS\G' | awk -F: '/File/ {print \$2}' | xargs")
POS=$(docker exec -i $SRC bash -lc "mysql -u root -p $ROOTPWD -e 'SHOW MASTER STATUS\G' | awk -F: '/Position/ {print \$2}' | xargs")

echo "Using FILE=$FILE POS=$POS"

#for REPL in $R1 $R2; do
for REPL in $R1; do
  docker exec -i $REPL bash -lc "
    mysql -uroot -p$ROOTPWD -e \"
      STOP REPLICA;
      RESET REPLICA ALL;
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
done

docker exec -i $R1 mysql -uroot -p$ROOTPWD -e "SHOW REPLICA STATUS\G" | sed -n '1,60p'
docker exec -i $R2 mysql -uroot -p$ROOTPWD -e "SHOW REPLICA STATUS\G" | sed -n '1,60p'

echo "Done (WARNING: 실제 운영 데이터 있으면 스냅샷 없이 시작 금지)."
