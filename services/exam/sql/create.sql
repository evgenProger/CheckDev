create table exam (
  id serial not null primary key,
  name varchar(2000),
  description text,
  active boolean not null default false,
  intro varchar(2000)
);

create table question (
  id serial not null primary key,
  name varchar(2000),
  description text,
  pos int,
  hint text,
  exam_id int not null references exam(id)
);

create table question_opt (
  id serial not null primary key,
  description text,
  question_id int not null references question(id),
  correct boolean default false,
  pos int
);

create table exam_user (
  id serial not null primary key,
  exam_id int not null references exam(id) UNIQUE,
  result int default 0,
  start timestamp not null default now(),
  finish timestamp,
  key varchar(2000) not null
);

create table answer (
  id serial not null primary key,
  question_id int not null references question(id),
  exam_user_id int not null references exam_user(id)
);

create table answer_opt (
  id serial not null primary key,
  answer_id int not null references answer(id),
  question_opt_id int not null references question_opt(id)
);

alter table exam_user add constraint exam_user_exam_id UNIQUE(exam_id, key);