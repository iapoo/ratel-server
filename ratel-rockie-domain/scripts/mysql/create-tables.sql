create table if not exists folder
(
    folder_id      bigint auto_increment,
    folder_name    varchar(64)  not null,
    parent_id      bigint       null,
    path           varchar(128) null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (folder_id)
);


create table if not exists document
(
    document_id    bigint auto_increment,
    document_name  varchar(64)  not null,
    folder_id      bigint       not null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (document_id)
);

create table if not exists content
(
    content_id     bigint auto_increment,
    content_name   varchar(64)  not null,
    content        text null,
    remark         varchar(512) null,
    is_enabled     int          not null default 1,
    is_deleted     int          not null default 0,
    effective_date bigint       null,
    expire_date    bigint       null,
    created_by     bigint       null,
    created_date   bigint       null,
    updated_by     bigint       null,
    updated_date   bigint       null,
    primary key (content_id)
);
