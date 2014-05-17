INSERT INTO Position (id, title) VALUES
(0, 'Admin'),
(1, 'Assistant'),
(2, 'Professor'),
(3, 'Senior Lecturer');

INSERT INTO Role (id, role) VALUES
(0, 'Admin'),
(1, 'User');

INSERT INTO RoomType (id, room_type) VALUES
(2, 'Auditorium'),
(1, 'Classroom'),
(3, 'Computer Class');

INSERT INTO Timetable (id, start, end) VALUES
(1, '07:45:00', '09:20:00'),
(2, '09:30:00', '11:05:00'),
(3, '11:15:00', '12:50:00'),
(4, '13:10:00', '14:45:00'),
(5, '14:55:00', '16:30:00'),
(6, '16:40:00', '18:15:00'),
(7, '18:25:00', '20:00:00');


INSERT INTO User (id, login, password, email, first_name, second_name, position, role) VALUES
(1, 'meli', '12345', 'melentev.igor@gmail.com', 'Igor', 'Melentev', 0, 0),
(2, 'vaha', '123456', 'vaha@gmail.com', 'Ivan', 'Nikolayenko', 2, 1),
(3, 'evgesha', '1234', 'evgen@mail.ru', 'Zhenya', 'Melnik', 1, 1),
(5, 'dimasik', '1234', 'dimasik@mail.ru', 'dimasik', 'dimasik', 3, 1),
(6, 'ads', '123', 'asd', 'sdf', 'asdf', 3, 1),
(7, 'w', 'w', 'w', 'w', 'w', 1, 1),
(8, 's', 's', 'ss', 's', 's', 3, 1);


INSERT INTO Room (id, room_name, floor, places, computers, board, projector, type) VALUES
(1, '365', 3, 50, 20, 1, 1, 3),
(2, '140', 1, 20, 0, 1, 0, 1),
(3, '104i', 1, 140, 0, 1, 1, 2),
(4, '106i', 2, 140, 0, 1, 1, 2),
(5, '166-1', 1, 25, 13, 0, 0, 3),
(6, '150-1', 1, 25, 13, 0, 0, 3),
(7, '304i', 3, 25, 0, 1, 0, 1),
(8, '407', 4, 25, 0, 0, 0, 1);





INSERT INTO Rights (id, position_id, room_id, can_book_room) VALUES
(1, 0, 1, 1),
(2, 0, 2, 1),
(3, 0, 3, 1),
(4, 1, 1, 1),
(5, 1, 2, 0),
(6, 1, 3, 0),
(7, 2, 1, 1),
(8, 2, 2, 1),
(9, 2, 3, 1),
(13, 3, 1, 1),
(14, 3, 2, 0),
(15, 3, 3, 1);

INSERT INTO Booking (id, user_id, room_id, timetable_id, date) VALUES
(1, 1, 1, 2, '2014-05-05');







