drop table if exists books cascade;
drop table if exists authors cascade;
drop table if exists books_authors cascade;

create table books (
	id bigserial primary key,
	title text not null,
	year int check (year > 0)
);

create table authors (
	id bigserial primary key,
	name text not null unique
);

create table books_authors (
	book_id bigint references books,
	author_id bigint references authors,
	primary key (book_id, author_id)
);

insert into books values
	(1, 'book 1', 1999),
	(2, 'book 2', 1999),
	(3, 'book 3', 2000),
	(4, 'book 4', 2000);
select setval('books_id_seq', 5);


insert into authors values
	(1, 'author 1'),
	(2, 'author 2'),
	(3, 'author 3'),
	(4, 'author 4');
select setval('authors_id_seq', 5);

insert into books_authors values
	(1, 1),
	(1, 2),
	(2, 1),
	(2, 2),
	(3, 3),
	(3, 4),
	(4, 3),
	(4, 4);


