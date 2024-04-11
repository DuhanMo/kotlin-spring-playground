create table members
(
    id         serial   not null
        primary key,
    name       varchar,
    age        integer,
    created_at timestamp not null,
    updated_at timestamp not null
);