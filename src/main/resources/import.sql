
insert into roles (authority) values ('ROLE_ADMIN'),('ROLE_USER');
insert into users (age,email,password,last_name,first_name) values (25,'admin@email.ru','$2a$10$nCAmgQcMNZn.duRCj1sV9O71J3mxapmkusGwF/1458S4QM1cmbndS','admin','Sergeev');
insert into users (age,email,password,last_name,first_name) values (28,'admin1@email.ru','$2a$10$nCAmgQcMNZn.duRCj1sV9O71J3mxapmkusGwF/1458S4QM1cmbndS','admin2','Pavlov');
INSERT INTO `users_role` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `users_role` (`user_id`, `role_id`) VALUES ('2', '2');