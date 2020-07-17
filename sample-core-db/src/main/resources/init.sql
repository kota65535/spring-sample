DROP DATABASE pet_store;
CREATE DATABASE pet_store;
use pet_store;

CREATE TABLE pets
(
    id   bigint PRIMARY KEY,
    name varchar(255),
    tag  varchar(255)
);

