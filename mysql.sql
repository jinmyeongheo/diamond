-- schema-mysql.sql
-- tables_mysql_innodb.sql

create table order_info (
                            id bigint not null auto_increment,
                            amount decimal(19,2),
                            payment_method varchar(255),
                            status varchar(255),
                            order_date_time datetime,
                            primary key (id)
) engine=InnoDB default charset=utf8mb4;

insert into order_info (amount, payment_method, status, order_date_time)
values
    (10000.00, 'CARD', 'PENDING', now()),
    (25000.50, 'CASH', 'COMPLETED', now() - interval 1 day),
    (5000.00, 'KAKAOPAY', 'CANCELLED', now() - interval 2 day);


DELIMITER $$

CREATE PROCEDURE insert_order_info()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 900000 DO
            INSERT INTO order_info (amount, payment_method, status, order_date_time)
            VALUES (
                       ROUND(RAND() * 100000, 2),                      -- 랜덤 금액
                       ELT(FLOOR(1 + (RAND() * 3)), 'CARD', 'CASH', 'BANK_TRANSFER'), -- 랜덤 결제수단
                       ELT(FLOOR(1 + (RAND() * 3)), 'PENDING', 'PAID', 'CANCELLED'), -- 랜덤 상태
                       NOW() - INTERVAL FLOOR(RAND() * 365) DAY        -- 최근 1년 내 랜덤 날짜
                   );
            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

-- 실행
CALL insert_order_info();




create table payment_base_info
(
    id     bigint auto_increment comment '거래id'
        primary key,
    amount decimal(15, 2) null comment '거래금액',
        reg_date_time datetime
);


-- 현재 세션의 트랜잭션 격리 수준 확인
SELECT @@transaction_isolation;

-- 글로벌 기본 격리 수준 확인
SELECT @@global.transaction_isolation;

-- 현재 세션의 autocommit 설정 확인 (1 = ON, 0 = OFF)
SELECT @@autocommit;

-- 글로벌 기본 autocommit 설정 확인
SELECT @@global.autocommit;
