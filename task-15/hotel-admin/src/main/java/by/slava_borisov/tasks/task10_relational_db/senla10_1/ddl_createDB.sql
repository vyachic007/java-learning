create table product (
maker varchar(10) not null,
model varchar(50) primary key,
type varchar(50) not null check(type in ('PC', 'Laptop', 'Printer'))
);

create table laptop(
code int primary key,
model varchar(50) not null,
speed smallint not null,
ram smallint not null,
hd real not null,
price money null,
screen smallint not null,
foreign key(model) references product(model)
);


create table pc(
code int primary key,
model varchar(50) not null,
speed smallint not null,
ram smallint not null,
hd real not null,
cd varchar(10) not null,
price money null,
foreign key(model) references product(model)
);


create table printer(
code int primary key,
model varchar(50) not null,
color char(1) not null CHECK (color IN ('y', 'n')),
type varchar(10) not null CHECK (type IN ('Laser', 'Jet', 'Matrix')),
price money null,
foreign key(model) references product(model)
);


