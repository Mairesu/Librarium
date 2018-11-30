
INSERT INTO city(zipcode, city_name)
VALUES (7050, 'Trondheim'),
       (7550, 'Hommelvik'),
       (6009, 'Ålesund'),
       (2817, 'Gjøvik'),
       (1337, 'Nexus'),
       (1234, 'Langtvekkistan'),
       (0999, 'Nexus'),
       (7010, 'Trondheim'),
       (7030, 'Trondheim');

INSERT INTO address(address_id, street_address, city_zipcode)
VALUES (1, 'Tyholdtvegen 4', 7050),
       (2, 'Danielstrøa 6', 7550),
       (3, 'Nørvevegen 10', 6009),
       (4, 'Granstien 21', 2917),
       (5, 'Tyholdtvegen 6', 7050),
       (6, 'Academy corner 4', 1337),
       (7, 'Surreal Avenue', 1234),
       (8, 'Victory rd. 124', 1234),
       (9, 'Granstien 21', 7010),
       (10, 'Heimdalsmyra 463', 7030),
       (11, 'Illium Avenue 8774', 1337);

INSERT INTO library_branch(branch_id, branch_name, branch_address_id)
VALUES (1, 'Studentbibliotek', 3),
       (2, 'Studentbibliotek', 1),
       (3, 'Studentbibliotek', 5),
       (4, 'Dronning Mauds bibliotek for syke barn', 1),
       (5, 'Kommunebibliotek', 1),
       (6, 'Kommunebibliotek', 3);


INSERT INTO borrower(borrower_id, borrower_name, borrower_phone, borrower_email)
VALUES (1, 'Artemis Sin', '+46 465 666 845', NULL),
       (2, 'John J. Johnson', '+1-735-534-2209', 'JohnJJ@John.com'),
       (3, 'John J. Johnson', '+1-567-234-3222', 'JohnJohnson@Gmail.com'),
       (4, 'Bjørn S. Bjørnson', '+47 975 45 445', NULL ),
       (5, 'Karl Jespersen', '+47 979 97 771', 'Karl@Jespersen.no');

INSERT INTO borrower_has_address(borrower_id, address_id)
VALUES (1, 11),
       (2, 2),
       (2, 10),
       (3, 9),
       (4, 9),
       (5, 7);

INSERT INTO author(author_id, author_name, author_birth_date)
VALUES (1, 'J.K. Rowling', '1965-7-31'),
       (2, 'Sun Tzu', NULL),
       (3, 'Knut Knutson', '1969-7-24'),
       (4, 'Knut Knutson', '1934-2-3'),
       (5, 'Artemis Sin', NULL),
       (6, 'Joseph Crust', '1989-5-16'),
       (7, 'Ross Bobson', '1989-5-16');

INSERT INTO publisher(publisher_id, publisher_name)
VALUES (1, 'Domminion publishing'),
       (2, 'Bloomsbury'),
       (3, 'Gyldendal'),
       (4, 'Cappelen Damm'),
       (5, 'Filiquarian'),
       (6, 'Raincoast');

INSERT INTO book(book_id, book_title, publication_year, edition, publisher_id)
VALUES (1, 'The Art of War', 2007, NULL, 5),
       (2, 'Harry Potter and the Philosopher''s Stone', 1997, 'First UK edition', 2),
       (3, 'Harry Potter and the Philosopher''s Stone', 1997, 'First CA edition', 6),
       (4, 'A history of Nexus', 1337, NULL, 1),
       (5, 'En bok med veldig langt navn og uten noe som heldst innhold', 1996, NULL, 3),
       (6, 'En bok med veldig langt navn og uten noe som heldst innhold', 1996, NULL, 4),
       (7, '100 Reasons why Exiles suck', 1419, 'Second edition', 1);

INSERT INTO book_has_author(book_id, author_id)
VALUES (1, 2),
       (2, 1),
       (3, 1),
       (4, 5),
       (5, 3),
       (5, 6),
       (6, 3),
       (6, 6),
       (7, 5);

INSERT INTO barcode(barcode_number, book_id, library_branch_id)
VALUES (173623, 1, 2),
       (170934, 1, 2),
       (177536, 1, 3),
       (174562, 2, 1),
       (5323, 2, 1),
       (64639, 5, 4),
       (123876, 7, 3),
       (127634, 7, 5),
       (122635, 7, 6);

INSERT INTO loan(loan_id, start_date, end_date, returned_date, borrower_id, barcode_number, library_branch_id)
VALUES (1, '2017-12-2', '2018-2-2', NULL, 2, 173623, 2),
       (2, '2018-11-16', '2019-1-16', NULL, 3, 5323, 1),
       (3, '2018-11-16', '2019-1-16', '2018-12-9', 3, 64639, 1),
       (4, '2018-10-29', '2019-1-7', NULL, 1, 170934, 2),
       (5, '2018-10-29', '2019-1-7', '2018-12-1', 4, 127634, 5);