create table if not exists person (
	id bigserial primary key,
	first_name varchar(100) not null,
	last_name varchar(100) not null,
	patronymic varchar(100),
	date_of_birth date not null,
	inn_state varchar(10) not null default 'None',
	inn varchar(12)
);
