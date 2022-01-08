-- MYSQL script for creating the record store database
-- drop tables (if necessary)
SET foreign_key_checks = 0;
drop table item cascade;
drop table artist cascade;
SET foreign_key_checks = 1;

-- create tables and primary keys
create table item (
    id                             int not null auto_increment,
    title                          varchar(50) not null,
    artistid                       int not null,
    format                         varchar(20) not null,
    genre                          varchar(50) not null,
    year                           int not null,
    nounits                        int not null,
	constraint item_pk primary key (id)
)
;

create table artist (
    id                             int auto_increment,
    name                           varchar(50) not null,
	constraint artist_pk primary key (id)
)
;


-- create foreign keys
alter table item add constraint item_artist_id_fk foreign key (artistid) references artist (id);
