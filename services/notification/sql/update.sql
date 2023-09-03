-- REG, FORGOT, HR, FORUM_MESSAGE
alter table template add column type varchar(2000) unique;

update template set type = 'REG' where key = 0;
update template set type = 'FORGOT' where key = 1;
update template set type = 'HR' where key = 2;
update template set type = 'FORUM_MESSAGE' where key = 3;

alter table template drop column key;