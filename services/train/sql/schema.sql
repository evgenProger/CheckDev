create table project (
 id serial primary key,
 name varchar(2000),
 url varchar(2000),
 status int not null
);

create table job (
 id serial primary key,
 name varchar(2000),
 cron varchar(2000)
);

create table project_job (
 id serial primary key,
 project_id int not null references project(id),
 job_id int not null references job(id)
);

create table task (
 id serial primary key,
 name varchar(2000),
 command text,
 type int not null,
 pos int not null default 1,
 job_id int not null references job(id)
);

create table project_task (
 id serial primary key,
 project_id int not null references project(id),
 task_id int not null references task(id),
 log text,
 result int not null,
 time timestamp without time zone not null,
 value text
);

create table setting (
 id serial primary key,
 value varchar(2000),
 type int not null
);
