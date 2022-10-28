create table if not exists customer
(
    customer_id             bigint          auto_increment,
    customer_name           varchar(64)     not null,
    password                varchar(256)    not null,
    nick_name               varchar(128)    null,
    id_card                 varchar(64)     null,
    telephone               varchar(64)     null,
    mobile                  varchar(64)     null,
    email                   varchar(64)     null,
    remark                  varchar(512)    null,
    is_enabled              int             not null default 1,
    is_deleted              int             not null default 0,
    effective_date          bigint          null,
    expire_date             bigint          null,
    created_by              bigint          null,
    created_date            bigint          null,
    updated_by              bigint          null,
    updated_date            bigint          null,
    primary key (customer_id)
);

