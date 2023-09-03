create table images (
  id serial primary key,
  filename varchar(45) NOT NULL,
  imagedata bytea
);