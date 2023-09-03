create table template (
  id serial primary key,
  template int not null,
  subject varchar(2000),
  body text
);

create table setting (
  id serial primary key,
  template int not null,
  value varchar(2000)
);