create table Address
(
    id   bigserial not null primary key,
    street varchar(50)
);

create table Phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint
);