use ratel;

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
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (user_id)
);

create table if not exists role
(
    role_id        bigint auto_increment,
    role_name      varchar(64)  not null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
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
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
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
