-- schema-mysql.sql

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


-- 현재 세션의 트랜잭션 격리 수준 확인
SELECT @@transaction_isolation;

-- 글로벌 기본 격리 수준 확인
SELECT @@global.transaction_isolation;

-- 현재 세션의 autocommit 설정 확인 (1 = ON, 0 = OFF)
SELECT @@autocommit;

-- 글로벌 기본 autocommit 설정 확인
SELECT @@global.autocommit;