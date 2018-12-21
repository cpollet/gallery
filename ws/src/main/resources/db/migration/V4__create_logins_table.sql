--changeset cpollet:1

create table logins
(
	id serial,
	username varchar(20) not null,
	password char(61) not null
);

create unique index logins_username_uindex
	on logins (username);

