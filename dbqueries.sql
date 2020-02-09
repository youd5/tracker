CREATE DATABASE testDB;
use testDB;
show tables;
CREATE TABLE symbols (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    symbol varchar(255) NOT NULL,
    PRIMARY KEY (id)
);
INSERT INTO symbols (name,symbol) VALUES ('ICICIBANK','ICICIBANK');

CREATE TABLE historical_prices_daily (
    id int NOT NULL AUTO_INCREMENT,
    symbol varchar(255) NOT NULL,
    open_price float4,
    close_price float4,
    prev_close_price float4,
    high_price float4,
    low_price float4,
    ltp_price float4,
    high52 float4,
    low52 float4, 
    vol BIGINT,
    mTIMESTAMP DATE,
    created_on DATETIME NOT NULL DEFAULT NOW(),
    updated_on DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);


