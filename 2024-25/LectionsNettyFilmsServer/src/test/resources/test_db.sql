drop table if exists films cascade;
drop table if exists actors cascade;
drop table if exists awards cascade;
drop table if exists actors_films cascade;

create table films (
	id bigint auto_increment primary key,
	title text not null,
	duration int2 not null check (duration > 0)
);

create table actors (
	id bigserial primary key,
	name text
);

create table awards (
    id bigserial primary key,
    title text
);

create table actors_films (
	id_film bigint references films,
	id_actor bigint references actors,
	primary key (id_film, id_actor)
);

create table films_awards (
    id_film bigint references films,
    id_award bigint references awards,
    primary key (id_film, id_award)
);

insert into films (id, title, duration) values (1, 'film a', 120);
insert into films (id, title, duration) values (2, 'film b', 140);
insert into films (id, title, duration) values (3, 'film c', 160);
insert into films (id, title, duration) values (4, 'qwerty', 180);

insert into actors (id, name) values (1, 'actor 1');
insert into actors (id, name) values (2, 'actor 2');
insert into actors (id, name) values (3, 'actor 3');
insert into actors (id, name) values (4, 'actor 4');
insert into actors (id, name) values (5, 'actor 5');

insert into awards (id, title) values (1, 'award 1');
insert into awards (id, title) values (2, 'award 2');
insert into awards (id, title) values (3, 'award 3');

insert into actors_films (id_film, id_actor) values (1, 1);
insert into actors_films (id_film, id_actor) values (1, 2);
insert into actors_films (id_film, id_actor) values (1, 4);
insert into actors_films (id_film, id_actor) values (2, 2);
insert into actors_films (id_film, id_actor) values (3, 1);
insert into actors_films (id_film, id_actor) values (3, 2);
insert into actors_films (id_film, id_actor) values (3, 3);
insert into actors_films (id_film, id_actor) values (3, 4);

insert into films_awards (id_film, id_award) values (1, 1);
insert into films_awards (id_film, id_award) values (1, 2);
insert into films_awards (id_film, id_award) values (1, 3);
insert into films_awards (id_film, id_award) values (3, 1);
insert into films_awards (id_film, id_award) values (3, 3);




















