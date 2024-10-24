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
    primary key (folder_id),
    unique key idx_folder_customer(customer_id, folder_id)
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
    link_code      varchar(64)  null,
    shared         int          not null default 0,
    access_code    varchar(64)  null,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (document_id),
    key idx_document_updated_date(updated_date desc),
    unique key idx_document_customer(customer_id, document_id),
    unique key idx_document_link_code(link_code)
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
    primary key (folder_id,  customer_id),
    unique key idx_folder_access(customer_id, folder_id)
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
    primary key (folder_id,  customer_id),
    unique key idx_folder_access_detail(customer_id, folder_id)
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
    primary key (document_id, customer_id),
    unique key idx_document_access(customer_id, document_id)
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
    member_type    bigint       not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (team_id, customer_id),
    unique key idx_team_member(customer_id, team_id)
);

create table if not exists document_team_access
(
    document_id    bigint       not null,
    team_id        bigint       not null,
    access_mode    bigint       not null default 0,
    effective_date timestamp    null,
    expire_date    timestamp    null,
    created_by     bigint       null,
    created_date   timestamp    null,
    updated_by     bigint       null,
    updated_date   timestamp    null,
    primary key (document_id, team_id),
    unique key idx_document_team(team_id, document_id)
);
