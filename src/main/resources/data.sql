INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last', '{noop}admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME, ADDRESS)
VALUES ('Kuvshin', 'Fedorova Street, 10, Kyiv (Kiev) 03150 Ukraine'),
       ('BEEF Meat & Wine', 'Shota Rustaveli Street, 11, Kyiv (Kiev) 01001 Ukraine'),
       ('Genatsvale&Hinkali', 'Dragomanova Street, 17, Kyiv (Kiev) 02068 Ukraine'),
       ('Musafir', 'Saksaganskogo Street, 57А, Kyiv (Kiev) 02000 Ukraine'),
       ('Red Meat', 'Shota Rustaveli Street, 18, Kyiv (Kiev) 01001 Ukraine');

INSERT INTO MENU (ADDED, RESTAURANT_ID)
VALUES (CURRENT_TIMESTAMP(), 1),
       (CURRENT_TIMESTAMP(), 2),
       ('2021-12-15', 3),
       (CURRENT_TIMESTAMP(), 4);

INSERT INTO DISH (NAME, PRICE, MENU_ID)
VALUES ('Wagyu beef steak', 400, 1),
       ('English marbled beef pie', 2000, 1),
       ('Fritatta with lobster and caviar', 1000, 1),
       ('Pizza "Louis XIII"', 12000, 1),
       ('Fleurburger', 5000, 2),
       ('Dessert "Fisherman on stilts"', 14500, 2),
       ('Simply the most expensive sushi', 4500, 2),
       ('Diamond caviar', 34500, 2),
       ('Sunday Frrrozen Haute Chocolate', 25000, 2),
       ('Faberge chocolate pudding', 34500, 3),
       ('Capitol Doug California Hot Dog', 145, 3),
       ('Jamon Iberico de Beyota "Albarragena"', 180, 3),
       ('Buddha Jump Over the Wall Soup', 190, 3),
       ('Puffer fish', 190, 4),
       ('Von Essen Platinum Club Sandwich', 197, 4);

INSERT INTO VOTE (VOTED, RESTAURANT_ID, USER_ID)
VALUES ('2021-12-15', 3, 1),
       ('2021-12-16', 3, 1),
       ('2021-12-17', 3, 2),
       ('2021-12-19', 3, 1),
       (CURRENT_TIMESTAMP(), 1, 2);