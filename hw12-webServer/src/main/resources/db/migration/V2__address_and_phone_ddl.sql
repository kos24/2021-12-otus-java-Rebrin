create table Address
(
    id   bigserial not null primary key,
    street varchar(50)
);

create table Phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint references client(id)
);

alter table Client
add constraint client_address_fk
foreign key (address_id) references Address(id)