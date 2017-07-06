create database demo;
use demo;

drop table if exists user;
create table user(
  id int not null auto_increment,
  nickname varchar(200)  null,
  account varchar(200)  null,
  password text not null,
  roleId int not null,
  date varchar(200) not null,
  primary key(id)
)DEFAULT CHARSET=UTF8;

drop table if exists roles;
create table roles(
  id int not null auto_increment,
  name varchar(200)  null,
  date varchar(200)  null,
  primary key(id)
)DEFAULT CHARSET=UTF8;