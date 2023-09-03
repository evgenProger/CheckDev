CREATE TABLE category (
    category_id serial primary key,
    category_name character varying(128),
    position integer not null,
    category_desc character varying(4096)
);

CREATE TABLE subject (
    subject_id serial primary key,
    category_id integer not null references category(category_id),
    subject_name character varying(128),
    user_key varchar(2000) not null,
    create_date timestamp,
    brief varchar(2000),
    description text,
    last_user_key varchar(2000) not null,
    last_date timestamp,
    count_message integer not null default 0,
    count_view integer not null default 0
);

CREATE TABLE message (
    message_id serial primary key,
    subject_id integer not null references subject(subject_id),
    user_key varchar(2000) not null,
    message_text text,
    create_date timestamp
);