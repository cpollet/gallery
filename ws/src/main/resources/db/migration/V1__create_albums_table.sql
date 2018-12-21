--changeset cpollet:1

create table albums
(
  id          serial not null,
  name        varchar(50) not null,
  description varchar(1000) not null,
  published   boolean not null
);

alter table albums
  owner to postgres;

