alter table person add column privacy boolean not null default true;

-- 25.10.2017
CREATE TABLE IF NOT EXISTS orders (
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  email VARCHAR(2000),
  username VARCHAR(2000),
  msg text,
  created timestamp default now(),
  archive boolean not null default false
);

--25.05.2018
ALTER TABLE person ADD COLUMN experience INTEGER default 0;
ALTER TABLE person ADD COLUMN about_short TEXT;
ALTER TABLE person ADD COLUMN about TEXT;

CREATE TABLE photos(
  id SERIAL PRIMARY KEY ,
  name VARCHAR(50),
  photo bytea
);

ALTER TABLE person ADD COLUMN id_photo int REFERENCES photos(id);
