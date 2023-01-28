create table if not exists user
(
    user_id        bigint auto_increment,
    user_name      varchar(64)  not null,
    password       varchar(256) not null,
    nick_name      varchar(128) null,
    id_card        varchar(64)  null,
    telephone      varchar(64)  null,
    mobile         varchar(64)  null,
    email          varchar(64)  null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (user_id)
);

create table if not exists role
(
    role_id        bigint auto_increment,
    role_name      varchar(64)  not null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (role_id)
);

create table if not exists user_role
(
    user_id      bigint not null,
    role_id      bigint not null,
    created_by   bigint null,
    created_date bigint null,
    updated_by   bigint null,
    updated_date bigint null,
    primary key (user_id, role_id),
    index ix_user_role (role_id, user_id)
);

create table if not exists resource
(
    resource_id    bigint auto_increment,
    resource_name  varchar(64)  not null,
    path           varchar(64)  null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (resource_id)
);

create table if not exists role_resource
(
    role_id      bigint not null,
    resource_id  bigint not null,
    created_by   bigint null,
    created_date bigint null,
    updated_by   bigint null,
    updated_date bigint null,
    primary key (role_id, resource_id),
    index ix_role_resource (resource_id, role_id)
);

create table if not exists product
(
    product_id     bigint auto_increment,
    product_name   varchar(64)  not null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (product_id)
);

create table if not exists customer
(
    customer_id    bigint auto_increment,
    customer_name  varchar(64)  not null,
    password       varchar(256) not null,
    nick_name      varchar(128) null,
    id_card        varchar(64)  null,
    telephone      varchar(64)  null,
    mobile         varchar(64)  null,
    email          varchar(64)  null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (customer_id)
);

create table if not exists license
(
    license_id     bigint auto_increment,
    license_name   varchar(64)  not null,
    customer_id    bigint       not null,
    product_id     bigint       not null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (license_id)
);

create table if not exists customer_license
(
    customer_id  bigint not null,
    license_id   bigint not null,
    created_by   bigint null,
    created_date bigint null,
    updated_by   bigint null,
    updated_date bigint null,
    primary key (customer_id, license_id),
    index ix_customer_license (license_id, customer_id)
);

create table if not exists customer_product
(
    customer_id  bigint not null,
    product_id   bigint not null,
    created_by   bigint null,
    created_date bigint null,
    updated_by   bigint null,
    updated_date bigint null,
    primary key (customer_id, product_id),
    index ix_customer_license (product_id, customer_id)
);


create table if not exists license_product
(
    license_id   bigint not null,
    product_id   bigint not null,
    created_by   bigint null,
    created_date bigint null,
    updated_by   bigint null,
    updated_date bigint null,
    primary key (license_id, product_id),
    index ix_license_product (product_id, license_id)
);
