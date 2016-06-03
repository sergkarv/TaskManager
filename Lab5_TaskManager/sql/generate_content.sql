INSERT INTO tu.User (name, password) 
VALUES ('admin', 'admin');               --1
INSERT INTO tu.User (name, password) 
VALUES ('sergkarv', '123');              --2
INSERT INTO tu.User (name, password) 
VALUES ('dino', 'dino');				 --3
INSERT INTO tu.User (name, password) 
VALUES ('one', 'one');                   --4

INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)                         	--1
VALUES ('Task_for_sergkarv_1', 'skv and other description', 'sergkarv@yandex.ru', '23.03.2016', false, null, 2);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--2
VALUES ('Task_for_sergkarv_1.1', 'list_1.1', 'sergkarv@yandex1.ru', '24.03.2016', false ,1, 2);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--3
VALUES ('Task_for_sergkarv_1.2', 'list_1.2', 'sergkarv@yandex2.ru', '24.03.2016' , false ,1, 2);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--4
VALUES ('Task_for_sergkarv_1.2.1', 'list_1.2.1', 'sergkarv@yandex21.ru', '25.03.2016' , true , 3, 2);

INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--5
VALUES ('Task_for_admin_1', 'admin description', 'admin@myprogram.ru', '23.03.2016', true ,null, 1);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--6
VALUES ('Task_for_admin_1.1', 'admin_list_1.1', 'admin_1@myprogram.ru', '23.03.2016', false , 5, 1);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--7
VALUES ('Task_for_admin_1.2', 'admin_list_1.2', 'admin_2@myprogram.ru', '23.03.2016' , false , 5, 1);

INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--8
VALUES ('Task_for_dino_1', 'dino description_1', 'dino_1@myprogram.ru', '28.03.2016', false ,null, 3);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--9
VALUES ('Task_for_dino_2', 'dino description_2', 'dino_2@myprogram.ru', '28.03.2016', false ,null, 3);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--10
VALUES ('Task_for_dino_1.1', 'dino description_1_1', 'dino_1_1@myprogram.ru', '29.03.2016', true ,8, 3);
INSERT INTO tu.Task (name, description, contacts, date, highPriority, parentId, userId)        					--11
VALUES ('Task_for_dino_2.1', 'dino description_2_1', 'dino_2_1@myprogram.ru', '29.03.2016', true ,9, 3);