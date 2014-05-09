
--
-- База данных: `RoomBooking`
--

-- --------------------------------------------------------

--
-- Структура таблицы `Booking`
--

CREATE TABLE IF NOT EXISTS `Booking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `room_id` int(11) DEFAULT NULL,
  `timetable_id` int(11) DEFAULT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_BookedAudience_User_Id_idx` (`user_id`),
  KEY `fk_BookedAudience_Audience_idx` (`room_id`),
  KEY `fk_BookedAudience_Timetable_idx` (`timetable_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Дамп данных таблицы `Booking`
--

INSERT INTO `Booking` (`id`, `user_id`, `room_id`, `timetable_id`, `date`) VALUES
(1, 1, 1, 2, '2014-05-05');

-- --------------------------------------------------------

--
-- Структура таблицы `Position`
--

CREATE TABLE IF NOT EXISTS `Position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Дамп данных таблицы `Position`
--

INSERT INTO `Position` (`id`, `title`) VALUES
(0, 'Admin'),
(1, 'Assistant'),
(2, 'Professor'),
(3, 'Senior Lecturer');

-- --------------------------------------------------------

--
-- Структура таблицы `Rights`
--

CREATE TABLE IF NOT EXISTS `Rights` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `position_id` int(11) DEFAULT NULL,
  `room_id` int(11) DEFAULT NULL,
  `can_book_room` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_Right_Position_idx` (`position_id`),
  KEY `fk_Right_Audience_idx` (`room_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=16 ;

--
-- Дамп данных таблицы `Rights`
--

INSERT INTO `Rights` (`id`, `position_id`, `room_id`, `can_book_room`) VALUES
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

-- --------------------------------------------------------

--
-- Структура таблицы `Role`
--

CREATE TABLE IF NOT EXISTS `Role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Дамп данных таблицы `Role`
--

INSERT INTO `Role` (`id`, `role`) VALUES
(0, 'Admin'),
(1, 'User');

-- --------------------------------------------------------

--
-- Структура таблицы `Room`
--

CREATE TABLE IF NOT EXISTS `Room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_name` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `floor` int(11) NOT NULL,
  `places` int(11) NOT NULL,
  `computers` int(11) DEFAULT '0',
  `board` tinyint(1) DEFAULT '1',
  `projector` tinyint(1) DEFAULT '0',
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `audience_name_UNIQUE` (`room_name`),
  KEY `fk_Audience_type_idx` (`type`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `Room`
--

INSERT INTO `Room` (`id`, `room_name`, `floor`, `places`, `computers`, `board`, `projector`, `type`) VALUES
(1, '365', 3, 50, 20, 1, 1, 3),
(2, '140', 1, 20, 0, 1, 0, 1),
(3, '104i', 1, 140, 0, 1, 1, 2),
(4, '106i', 2, 140, 0, 1, 1, 2),
(5, '166-1', 1, 25, 13, 0, 0, 3),
(6, '150-1', 1, 25, 13, 0, 0, 3),
(7, '304i', 3, 25, 0, 1, 0, 1),
(8, '407', 4, 25, 0, 0, 0, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `RoomType`
--

CREATE TABLE IF NOT EXISTS `RoomType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `audience_type_UNIQUE` (`room_type`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Дамп данных таблицы `RoomType`
--

INSERT INTO `RoomType` (`id`, `room_type`) VALUES
(2, 'Auditorium'),
(1, 'Classroom'),
(3, 'Computer Class');

-- --------------------------------------------------------

--
-- Структура таблицы `Timetable`
--

CREATE TABLE IF NOT EXISTS `Timetable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start` time NOT NULL,
  `end` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=8 ;

--
-- Дамп данных таблицы `Timetable`
--

INSERT INTO `Timetable` (`id`, `start`, `end`) VALUES
(1, '07:45:00', '09:20:00'),
(2, '09:30:00', '11:05:00'),
(3, '11:15:00', '12:50:00'),
(4, '13:10:00', '14:45:00'),
(5, '14:55:00', '16:30:00'),
(6, '16:40:00', '18:15:00'),
(7, '18:25:00', '20:00:00');

-- --------------------------------------------------------

--
-- Структура таблицы `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `second_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `position` int(11) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  KEY `fk_User_post_idx` (`position`),
  KEY `fk_User_Role_idx` (`role`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `User`
--

INSERT INTO `User` (`id`, `login`, `password`, `email`, `first_name`, `second_name`, `position`, `role`) VALUES
(1, 'meli', '12345', 'melentev.igor@gmail.com', 'Igor', 'Melentev', 0, 0),
(2, 'vaha', '123456', 'vaha@gmail.com', 'Ivan', 'Nikolayenko', 2, 1),
(3, 'evgesha', '1234', 'evgen@mail.ru', 'Zhenya', 'Melnik', 1, 1),
(5, 'dimasik', '1234', 'dimasik@mail.ru', 'dimasik', 'dimasik', 3, 1),
(6, 'ads', '123', 'asd', 'sdf', 'asdf', 3, 1),
(7, 'w', 'w', 'w', 'w', 'w', 1, 1),
(8, 's', 's', 'ss', 's', 's', 3, 1);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `Booking`
--
ALTER TABLE `Booking`
  ADD CONSTRAINT `fk_BookedAudience_Timetable` FOREIGN KEY (`timetable_id`) REFERENCES `Timetable` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_Booking_Audience` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Booking_User_Id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `Rights`
--
ALTER TABLE `Rights`
  ADD CONSTRAINT `fk_Right_Post` FOREIGN KEY (`position_id`) REFERENCES `Position` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_Right_Room` FOREIGN KEY (`room_id`) REFERENCES `RoomType` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `Room`
--
ALTER TABLE `Room`
  ADD CONSTRAINT `fk_Room_type` FOREIGN KEY (`type`) REFERENCES `RoomType` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `User`
--
ALTER TABLE `User`
  ADD CONSTRAINT `fk_User_post` FOREIGN KEY (`position`) REFERENCES `Position` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_User_Role` FOREIGN KEY (`role`) REFERENCES `Role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
