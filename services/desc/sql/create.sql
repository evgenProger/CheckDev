create table person (
 id serial primary key,
 username varchar(2000),
 email varchar(2000) unique,
 password varchar(2000),
 key varchar(2000),
 active boolean default false
);

create table role (
 id serial primary key,
 value varchar(200)
);

create table person_role (
 id serial primary key,
 person_id int not null references person(id),
 role_id int not null references role(id)
);