
use ratel;

create table if not exists product
(
    product_id     bigint auto_increment,
    product_name   varchar(64)  not null,
    remark         varchar(512) null,
    enabled        bit          not null default 1,
    deleted        bit          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (product_id)
);

create table if not exists customer
(
    customer_id    bigint auto_increment,
    customer_name  varchar(64)  not null,
    customer_code  varchar(64)  not null,
    password       varchar(256) not null,
    nick_name      varchar(128) null,
    id_card        varchar(64)  null,
    telephone      varchar(64)  null,
    mobile         varchar(64)  null,
    email          varchar(64)  not null,
    remark         varchar(512) null,
    settings       longtext         null,
    enabled        bit          not null default 1,
    deleted        bit          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (customer_id),
    unique key idx_customer_customer_name(customer_name),
    unique key idx_customer_email(email)
);

create table if not exists license
(
    license_id     bigint auto_increment,
    customer_id    bigint       not null,
    product_id     bigint       not null,
    remark         varchar(512) null,
    enabled        bit          not null default 1,
    deleted        bit          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (license_id)
);
