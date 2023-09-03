ALTER TABLE public.vacancy ADD COLUMN limit_time bigint;

alter table vacancy add column archive boolean default false;