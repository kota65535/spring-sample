DROP DATABASE pet_store;
CREATE DATABASE pet_store;
use pet_store;

CREATE TABLE pets
(
    id   bigint AUTO_INCREMENT,
    name varchar(255),
    tag  varchar(255),
    PRIMARY KEY (id)
);

