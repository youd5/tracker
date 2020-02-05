CREATE DATABASE testDB;
use testDB;
show tables;
CREATE TABLE symbols (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    symbol varchar(255) NOT NULL,
    PRIMARY KEY (id)
);
