create table hibernate_sequence (
    next_val bigint
);

insert into hibernate_sequence values ( 100 );

create table users (
    id          bigint primary key,
    username    varchar(255) unique not null,
    password    varchar(255) not null,
    enabled bit not null default 1
);

insert into users (id, username, password) values (1, 'admin', 'abcd');
insert into users (id, username, password) values (2, 'cysun', 'abcd');
