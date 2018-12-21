--changeset cpollet:1

create table tag_targets
(
  target_type varchar(30) not null,
  description varchar(250) not null
)
;

alter table tag_targets owner to postgres
;

create unique index tag_targets_target_type_uindex
on tag_targets (target_type)
;

create table tags
(
  tag varchar(30) not null,
  target_type varchar(30) not null
    constraint tags_tag_targets_target_type_fk
      references tag_targets (target_type),
  target_id integer not null,
  constraint tags_pk
    primary key (tag, target_type, target_id)
)
;

alter table tags owner to postgres
;

