truncate table phone;
truncate table address restart identity cascade;
truncate table client restart identity cascade;

insert into address (street)
values ('Unnamed Street');

insert into client(name, address_id)
values ('Ivan', 1);

insert into phone (number, client_id)
values ('791111111122', 1);

insert into phone (number, client_id)
values ('791111111123', 1);
