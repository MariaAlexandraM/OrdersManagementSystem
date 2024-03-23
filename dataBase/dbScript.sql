create database if not exists assignment3DataBase;
use assignment3DataBase;

create table if not exists Clients (
	clientId int primary key auto_increment unique not null,
    clientName char(50) not null, 
    address varchar(200) not null,
    email varchar(200) unique not null);
    
create table if not exists Products (
	productId int primary key auto_increment unique not null,
	productName varchar(50) not null,
    quantity int not null, # poate fi la un moment dat pe 0 da nu pot introduce direct 0
    price double not null);

create table if not exists Orders (
	orderId int primary key unique auto_increment not null,
	clientId int not null,
    productId int not null,
    orderedQuantity int not null);
     
alter table Orders add foreign key(clientId) 
						references Clients(clientId) on delete cascade on update cascade, 
				   add foreign key(productId) 
						references Products(productId) on delete cascade on update cascade;

insert into Clients (clientName, address, email) values ('Maria Moldovan', 'Str. Observatorului, Nr. 64', 'mariamoldovan@email.com');
insert into Clients (clientName, address, email) values ('Iulia Moldovan', 'Str. Fabricii de zahar, Nr. 58', 'iuliamoldovan@email.com');
insert into Clients (clientName, address, email) values ('Catalin Robotin', 'Str. Orastie, Nr. 24', 'catalinrobotin@email.com');

insert into Products (productName, quantity, price) values ('Mere', 150, 4);
insert into Products (productName, quantity, price) values ('Pere', 150, 5.5);
insert into Products (productName, quantity, price) values ('Banane', 170, 6);
insert into Products (productName, quantity, price) values ('Capsuni', 75, 12);
insert into Products (productName, quantity, price) values ('Paine', 100, 7);
insert into Products (productName, quantity, price) values ('Ciocolata', 300, 11);
