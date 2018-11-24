
DROP TABLE IF EXISTS publisher;

CREATE TABLE IF NOT EXISTS publisher (
  publisher_id integer NOT NULL PRIMARY KEY,
  publisher_name varchar(127) NOT NULL
);


DROP TABLE IF EXISTS city;

CREATE TABLE IF NOT EXISTS city (
  zipcode varchar(32) NOT NULL PRIMARY KEY,
  city_name varchar(127) NOT NULL
);


DROP TABLE IF EXISTS address;

CREATE TABLE IF NOT EXISTS address  (
  address_id integer NOT NULL PRIMARY KEY,
  street_address varchar(127) NOT NULL,
  city_zipcode varchar(127) NOT NULL,
  FOREIGN KEY (city_zipcode) REFERENCES city(zipcode)
);


DROP TABLE IF EXISTS library_branch;

CREATE TABLE IF NOT EXISTS library_branch (
  branch_id integer NOT NULL PRIMARY KEY,
  branch_name varchar(127) NOT NULL,
  branch_address_id integer NOT NULL,
  FOREIGN KEY (branch_address_id) REFERENCES address(address_id)
);