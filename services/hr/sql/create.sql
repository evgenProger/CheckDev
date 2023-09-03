create table vacancy (
  id serial primary key,
  name varchar(2000),
  location varchar(2000),
  rate varchar(2000),
  company varchar(2000),
  experience varchar(2000),
  published timestamp,
  created timestamp not null default now(),
  active boolean not null default false,
  description text,
  key varchar(2000)
);

create table stage (
  id serial primary key,
  name varchar(2000),
  description text,
  pos int,
  vacancy_id int not null references vacancy(id),
  congratulate text,
  failure text
);

create table task (
  id serial primary key,
  name varchar(2000),
  type int not null,
  stage_id int not null references stage(id),
  pos int,
  description text
);

create table task_value (
  id serial primary key,
  task_id int not null references task(id),
  value text,
  info varchar(2000)
);

create table interview (
 id serial not null primary key,
 key varchar(2000) not null,
 start timestamp not null,
 finish timestamp,
 vacancy_id int not null references vacancy(id),
 result int not null default 0
);

alter table interview add constraint interview_vacancy_id UNIQUE(vacancy_id, key);

create table interview_task (
  id serial not null primary key,
  interview_id int not null references interview(id),
  task_id int not null references task(id),
  start timestamp not null,
  finish timestamp
);

alter table interview_task add constraint
  interview_task_id UNIQUE(interview_id, task_id);

create table interview_value (
  id serial not null primary key,
  interview_task_id int not null references interview_task(id),
  key int not null,
  value text,
  attach bytea
);

create table task_predict (
  id serial not null primary key,
  task_id int not null references task(id),
  key varchar(2000),
  value text
);

create table report (
  id serial not null primary key,
  vacancy_id int not null references vacancy(id),
  name varchar(2000),
  type int not null,
  period int not null,
  sorted_id int not null references task(id),
  sorted_by int not null
);

create table report_field (
 id serial not null primary key,
 field_id int not null references task(id),
 position int not null
);

create table task_algo (
 id serial not null primary key,
 source text,
 test text,
 name varchar(2000),
 description text,
 sname varchar(2000),
 tname varchar(2000),
 published boolean not null default false,
 level int not null
);

create table track (
  id serial not null primary key,
  interview_id int not null references interview(id),
  stage_id int not null references stage(id),
  task_id int,
  position int not null,
  passed boolean not null default false,
  key int not null default 0,
  created timestamp not null default now()
);

alter table track add constraint track_interview UNIQUE(interview_id, stage_id, task_id);

CREATE TABLE template_vacancy (
  id SERIAL PRIMARY KEY,
  name VARCHAR(20),
  description VARCHAR(2000),
  template TEXT NOT NULL
);

create table team (
  id serial primary key,
  name varchar(2000),
  owner_key varchar(2000) not null
);

create table member (
  id serial primary key,
  team_id int not null references team(id),
  person_key varchar(2000) not null
);

create table permission (
 id serial primary key,
 team_id int not null references team(id),
 vacancy_id int not null references vacancy(id),
 created boolean,
 read boolean,
 write boolean
);

create table comment (
  id serial primary key,
  interview_id int not null references interview(id),
  person_key  varchar(2000) not null,
  text text,
  created timestamp not null default now()
);