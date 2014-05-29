SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `RoomBooking` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `RoomBooking` ;

-- -----------------------------------------------------
-- Table `RoomBooking`.`Timetable`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`Timetable` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `start` TIME NOT NULL ,
  `end` TIME NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `RoomBooking`.`RoomType`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`RoomType` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `room_type` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `audience_type_UNIQUE` (`room_type` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `RoomBooking`.`Room`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`Room` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `room_name` VARCHAR(15) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `floor` INT(11) NOT NULL ,
  `places` INT(11) NOT NULL ,
  `computers` INT(11) NULL DEFAULT '0' ,
  `board` TINYINT(1) NULL DEFAULT '1' ,
  `projector` TINYINT(1) NULL DEFAULT '0' ,
  `type` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `audience_name_UNIQUE` (`room_name` ASC) ,
  INDEX `fk_Audience_type_idx` (`type` ASC) ,
  CONSTRAINT `fk_Room_type`
    FOREIGN KEY (`type` )
    REFERENCES `RoomBooking`.`RoomType` (`id` )
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `RoomBooking`.`Position`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`Position` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `title_UNIQUE` (`title` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `RoomBooking`.`Role`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`Role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `role` VARCHAR(40) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RoomBooking`.`User`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`User` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `login` VARCHAR(16) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `password` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `email` VARCHAR(60) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ,
  `first_name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ,
  `second_name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ,
  `position` INT(11) NOT NULL ,
  `role` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) ,
  INDEX `fk_User_post_idx` (`position` ASC) ,
  INDEX `fk_User_Role_idx` (`role` ASC) ,
  CONSTRAINT `fk_User_post`
    FOREIGN KEY (`position` )
    REFERENCES `RoomBooking`.`Position` (`id` )
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_User_Role`
    FOREIGN KEY (`role` )
    REFERENCES `RoomBooking`.`Role` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 27
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `RoomBooking`.`Booking`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`Booking` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `user_id` INT(11) NULL DEFAULT NULL ,
  `room_id` INT(11) NULL DEFAULT NULL ,
  `timetable_id` INT(11) NULL DEFAULT NULL ,
  `date` DATE NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_BookedAudience_User_Id_idx` (`user_id` ASC) ,
  INDEX `fk_BookedAudience_Audience_idx` (`room_id` ASC) ,
  INDEX `fk_BookedAudience_Timetable_idx` (`timetable_id` ASC) ,
  CONSTRAINT `fk_BookedAudience_Timetable`
    FOREIGN KEY (`timetable_id` )
    REFERENCES `RoomBooking`.`Timetable` (`id` )
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Booking_Audience`
    FOREIGN KEY (`room_id` )
    REFERENCES `RoomBooking`.`Room` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Booking_User_Id`
    FOREIGN KEY (`user_id` )
    REFERENCES `RoomBooking`.`User` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 57
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `RoomBooking`.`Rights`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `RoomBooking`.`Rights` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `position_id` INT(11) NULL DEFAULT NULL ,
  `room_id` INT(11) NULL DEFAULT NULL ,
  `can_book_room` TINYINT(1) NULL DEFAULT '0' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Right_Position_idx` (`position_id` ASC) ,
  INDEX `fk_Right_Audience_idx` (`room_id` ASC) ,
  CONSTRAINT `fk_Right_Post`
    FOREIGN KEY (`position_id` )
    REFERENCES `RoomBooking`.`Position` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Right_Room`
    FOREIGN KEY (`room_id` )
    REFERENCES `RoomBooking`.`RoomType` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

USE `RoomBooking` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

