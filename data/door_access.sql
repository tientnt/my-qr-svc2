-- MySQL Workbench Synchronization
-- Generated: 2019-01-09 14:59
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: Dennis

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE TABLE IF NOT EXISTS `door_access`.`user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `phone_no` VARCHAR(20) NULL DEFAULT NULL,
  `parent_id` BIGINT(20) NULL DEFAULT 0,
  `role` VARCHAR(20) NULL DEFAULT NULL,
  `external_id` VARCHAR(20) NULL DEFAULT NULL COMMENT 'map to gkk account_id',
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` BIGINT(20) NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_user_idx` (`parent_id` ASC),
  CONSTRAINT `fk_user_user`
    FOREIGN KEY (`parent_id`)
    REFERENCES `door_access`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`place` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(2048) NULL DEFAULT NULL,
  `external_id` VARCHAR(20) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` BIGINT(20) NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`gate` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(512) NULL DEFAULT NULL,
  `external_id` VARCHAR(20) NULL DEFAULT NULL COMMENT 'map to choice_id (card order)',
  `external_id2` VARCHAR(20) NULL DEFAULT NULL COMMENT 'map to choice_id(user_order)',
  `place_id` BIGINT(20) NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` BIGINT(20) NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_gate_place1_idx` (`place_id` ASC),
  CONSTRAINT `fk_gate_place1`
    FOREIGN KEY (`place_id`)
    REFERENCES `door_access`.`place` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`device` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `username` VARCHAR(40) NULL DEFAULT NULL,
  `version` VARCHAR(20) NULL DEFAULT NULL,
  `is_activated` TINYINT(1) NULL DEFAULT 0,
  `external_id` VARCHAR(20) NULL DEFAULT NULL,
  `uid` VARCHAR(40) NULL DEFAULT NULL COMMENT 'unique id per device',
  `gate_id` BIGINT(20) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_device_gate1_idx` (`gate_id` ASC),
  UNIQUE INDEX `uid_UNIQUE` (`username` ASC),
  CONSTRAINT `fk_device_gate1`
    FOREIGN KEY (`gate_id`)
    REFERENCES `door_access`.`gate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`user_place_map` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `status` INT(11) NOT NULL DEFAULT 1,
  `user_id` BIGINT(20) NOT NULL,
  `place_id` BIGINT(20) NOT NULL,
  `external_id` VARCHAR(20) NULL DEFAULT NULL COMMENT 'map to gkk purchase_order.id',
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_user_place_map_user1_idx` (`user_id` ASC),
  INDEX `fk_user_place_map_place1_idx` (`place_id` ASC),
  CONSTRAINT `fk_user_place_map_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `door_access`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_place_map_place1`
    FOREIGN KEY (`place_id`)
    REFERENCES `door_access`.`place` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`device_history` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `activation_code` VARCHAR(255) NULL DEFAULT NULL,
  `device_id` BIGINT(20) NULL DEFAULT NULL,
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `remark` VARCHAR(1024) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_device_history_device1_idx` (`device_id` ASC),
  INDEX `fk_device_history_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_device_history_device1`
    FOREIGN KEY (`device_id`)
    REFERENCES `door_access`.`device` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_device_history_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `door_access`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`user_gate_map` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `is_activated` TINYINT(1) NOT NULL DEFAULT 0,
  `from_date` DATETIME NULL DEFAULT NULL,
  `to_date` DATETIME NULL DEFAULT NULL,
  `user_id` BIGINT(20) NOT NULL,
  `gate_id` BIGINT(20) NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_user_gate_map_user1_idx` (`user_id` ASC),
  INDEX `fk_user_gate_map_gate1_idx` (`gate_id` ASC),
  CONSTRAINT `fk_user_gate_map_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `door_access`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_gate_map_gate1`
    FOREIGN KEY (`gate_id`)
    REFERENCES `door_access`.`gate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`card` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `is_activated` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'not in used',
  `hash` VARCHAR(128) NULL DEFAULT NULL COMMENT 'sha256',
  `user_id` BIGINT(20) NULL DEFAULT NULL,
  `purchased_date` DATETIME NULL DEFAULT NULL COMMENT 'purchase_order.created_at',
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_card_user1_idx` (`user_id` ASC),
  UNIQUE INDEX `hash_UNIQUE` (`hash` ASC),
  CONSTRAINT `fk_card_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `door_access`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`card_gate_map` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `status` INT(11) NOT NULL DEFAULT 0,
  `from_date` DATETIME NULL DEFAULT NULL,
  `to_date` DATETIME NULL DEFAULT NULL,
  `hash` VARCHAR(128) NULL DEFAULT NULL COMMENT 'bcrypt + shaX(cardno + gate_id)',
  `remark` VARCHAR(255) NULL DEFAULT NULL COMMENT 'card tap at this gate or activate via ticket info',
  `card_id` BIGINT(20) NOT NULL,
  `gate_id` BIGINT(20) NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_card_gate_map_card1_idx` (`card_id` ASC),
  INDEX `fk_card_gate_map_gate1_idx` (`gate_id` ASC),
  CONSTRAINT `fk_card_gate_map_card1`
    FOREIGN KEY (`card_id`)
    REFERENCES `door_access`.`card` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_card_gate_map_gate1`
    FOREIGN KEY (`gate_id`)
    REFERENCES `door_access`.`gate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`card_device_activation` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `otp` VARCHAR(20) NULL DEFAULT NULL,
  `expires_at` DATETIME NULL DEFAULT NULL,
  `confirmed_at` DATETIME NULL DEFAULT NULL,
  `confirmed_otp` VARCHAR(20) NULL DEFAULT NULL,
  `activation_type` VARCHAR(20) NULL DEFAULT NULL COMMENT 'device-gate activation or card-gate activation ',
  `card_id` BIGINT(20) NOT NULL,
  `device_id` BIGINT(20) NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `otp_UNIQUE` (`otp` ASC),
  INDEX `fk_card_device_activation_card1_idx` (`card_id` ASC),
  INDEX `fk_card_device_activation_device1_idx` (`device_id` ASC),
  CONSTRAINT `fk_card_device_activation_card1`
    FOREIGN KEY (`card_id`)
    REFERENCES `door_access`.`card` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_card_device_activation_device1`
    FOREIGN KEY (`device_id`)
    REFERENCES `door_access`.`device` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`tx_history` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `card_id` BIGINT(20) NULL DEFAULT NULL,
  `device_id` BIGINT(20) NULL DEFAULT NULL,
  `gate_id` BIGINT(20) NULL DEFAULT NULL,
  `status` INT(11) NULL DEFAULT NULL COMMENT '0: failed, 1: success',
  `message` VARCHAR(1024) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `door_access`.`client` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `key` VARCHAR(255) NULL DEFAULT NULL COMMENT 'for oauth2',
  `secret` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `key_UNIQUE` (`key` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE TABLE IF NOT EXISTS `door_access`.`po_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `external_id` VARCHAR(20) NOT NULL COMMENT 'map to gkk purchase_order.id',
  `external_place_id` VARCHAR(20) NOT NULL COMMENT 'map to gkk outlet_id',
  `external_sub_id` VARCHAR(20) NULL,
  `external_choice_ids` VARCHAR(100) NOT NULL COMMENT 'map to gkk choice_id',
  `external_user_id` VARCHAR(20) NOT NULL COMMENT 'map to gkk user id',
  `external_account_id` VARCHAR(20) NULL DEFAULT NULL COMMENT 'map to gkk account id',
  `purchase_date` DATETIME NULL DEFAULT NULL,
  `from_date` DATETIME NULL DEFAULT NULL,
  `to_date` DATETIME NULL DEFAULT NULL,
  `card_name` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_external_detail` (`external_sub_id` ASC, `external_id` ASC))
ENGINE = InnoDB


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
