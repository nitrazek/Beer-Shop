DELETE FROM account;
DELETE FROM product;
DELETE FROM "order";

INSERT INTO account(id, login, password, email, role) VALUES(1, 'admin', 'admin', 'admin', 'ADMIN');
INSERT INTO account(id, login, password, email, role) VALUES(2, 'client1', 'client1', 'client1', 'CLIENT');
INSERT INTO account(id, login, password, email, role) VALUES(3, 'client2', 'client2', 'client2', 'CLIENT');

INSERT INTO product(id, name, category, price) VALUES(1, 'piwko1', 'BEER', 5.0);
INSERT INTO product(id, name, category, price) VALUES(2, 'piwko2', 'BEER', 10.0);
INSERT INTO product(id, name, category, price) VALUES(3, 'piwko3', 'BEER', 15.0);