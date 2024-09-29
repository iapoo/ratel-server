use ratel;

create table if not exists folder
(
    folder_id      bigint auto_increment,
    folder_name    varchar(64)  not null,
    parent_id      bigint       null,
    customer_id    bigint       null,
    path           varchar(128) null,
    remark         varchar(512) null,
    enabled        int          not null default 1,
    deleted        int          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (folder_id)
);


create table if not exists document
(
    document_id    bigint auto_increment,
    document_name  varchar(64)  not null,
    customer_id    bigint       not null,
    content_id     bigint       not null,
    folder_id      bigint       null,
    remark         varchar(512) null,
    enabled        int          not null default 1,
    deleted        int          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (document_id)
);

create table if not exists content
(
    content_id     bigint auto_increment,
    content_name   varchar(64)  not null,
    content        longtext null,
    remark         varchar(512) null,
    enabled        int          not null default 1,
    deleted        int          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (content_id)
);

create table if not exists folder_access
(
    folder_id      bigint       not null,
    customer_id    bigint       not null,
    access_mode    bigint       not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (folder_id,  customer_id)
);

create table if not exists folder_access_detail
(
    folder_id      bigint       not null,
    customer_id    bigint       not null,
    access_mode    bigint       not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (folder_id,  customer_id)
);

create table if not exists document_access
(
    document_id    bigint       not null,
    customer_id    bigint       not null,
    access_mode    bigint       not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (document_id, customer_id)
);

create table if not exists team
(
    team_id        bigint auto_increment,
    team_name      varchar(64)  not null,
    customer_id    bigint       not null,
    remark         varchar(512) null,
    enabled        int          not null default 1,
    deleted        int          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (team_id),
    unique key idx_team_customer(customer_id, team_id),
    key idx_team_team_name(customer_id, team_name)
);

create table if not exists team_member
(
    team_id        bigint       not null,
    customer_id    bigint       not null,
    member_type  bigint       not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (team_id, customer_id),
    unique key idx_team_member(customer_id, team_id)
);


create table if not exists operator
(
    operator_id    bigint       auto_increment,
    customer_id    bigint       not null,
    operator_type  bigint       not null default 0,
    deleted        int          not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (operator_id),
    key idx_team_member(customer_id)
);

