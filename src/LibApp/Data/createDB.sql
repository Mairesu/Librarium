
DROP TABLE IF EXISTS city;

CREATE TABLE IF NOT EXISTS city (
  zipcode integer PRIMARY KEY,
  city_name varchar(127) NOT NULL
);


DROP TABLE IF EXISTS address;

CREATE TABLE IF NOT EXISTS address  (
  address_id integer PRIMARY KEY,
  street_address varchar(127) NOT NULL,
  city_zipcode integer NOT NULL,
  FOREIGN KEY (city_zipcode) REFERENCES city(zipcode)
);


DROP TABLE IF EXISTS library_branch;

CREATE TABLE IF NOT EXISTS library_branch (
  branch_id integer PRIMARY KEY,
  branch_name varchar(127) NOT NULL,
  branch_address_id integer NOT NULL,
  FOREIGN KEY (branch_address_id) REFERENCES address(address_id)
);


DROP TABLE IF EXISTS publisher;

CREATE TABLE IF NOT EXISTS publisher (
  publisher_id integer PRIMARY KEY,
  publisher_name varchar(127) NOT NULL
);


DROP TABLE IF EXISTS author;

CREATE TABLE IF NOT EXISTS author (
  author_id integer PRIMARY KEY,
  author_name varchar(255) NOT NULL,
  author_birth_date date
);


DROP TABLE IF EXISTS book;

CREATE TABLE IF NOT EXISTS book (
  book_id integer PRIMARY KEY,
  book_title varchar(255) NOT NULL,
  publication_year integer NOT NULL,
  edition integer,
  publisher integer NOT NULL,
  FOREIGN KEY (publisher) REFERENCES publisher(publisher_id)
);


DROP TABLE IF EXISTS book_has_author;

CREATE TABLE IF NOT EXISTS book_has_author  (
  book_id integer NOT NULL,
  author_id integer NOT NULL,
  PRIMARY KEY (book_id, author_id),
  FOREIGN KEY (book_id) REFERENCES book(book_id),
  FOREIGN KEY (author_id) REFERENCES author(author_id)
);


DROP TABLE IF EXISTS barcode;

CREATE TABLE IF NOT EXISTS barcode  (
  barcode_number integer PRIMARY KEY,
  book_id integer NOT NULL,
  library_branch_id integer NOT NULL,
  FOREIGN KEY (book_id) REFERENCES book(book_id),
  FOREIGN KEY (library_branch_id) REFERENCES library_branch(branch_id)
);


DROP TABLE IF EXISTS borrower;

CREATE TABLE IF NOT EXISTS borrower (
  borrower_id integer PRIMARY KEY,
  borrower_name varchar(127) NOT NULL,
  borrower_phone varchar(45) NOT NULL,
  borrower_email varchar(127)
);


DROP TABLE IF EXISTS borrower_has_address;

CREATE TABLE IF NOT EXISTS borrower_has_address (
  borrower_id integer NOT NULL,
  address_id integer NOT NULL,
  PRIMARY KEY (borrower_id, address_id),
  FOREIGN KEY (borrower_id) REFERENCES borrower(borrower_id),
  FOREIGN KEY (address_id) REFERENCES  address(address_id)
);


DROP TABLE IF EXISTS loan;

CREATE TABLE IF NOT EXISTS loan (
  loan_id integer PRIMARY KEY,
  start_date date NOT NULL,
  end_date date NOT NULL,
  returned_date date,
  borrower_id integer NOT NULL,
  barcode_number integer NOT NULL,
  library_branch_id integer NOT NULL,
  FOREIGN KEY (borrower_id) REFERENCES borrower(borrower_id),
  FOREIGN KEY (barcode_number) REFERENCES barcode(barcode_number),
  FOREIGN KEY (library_branch_id) REFERENCES library_branch(branch_id)
);