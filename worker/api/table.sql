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
  id int not null,
  name varchar(200) not null,
  date varchar(200) not null,
  primary key(id)
)DEFAULT CHARSET=UTF8;

insert roles(id,name,date) values(0,"注册用户","2017-07-06 12:12:12");
insert roles(id,name,date) values(1,"管理员","2017-07-06 12:12:12");
insert roles(id,name,date) values(2,"超级管理员","2017-07-06 12:12:12");