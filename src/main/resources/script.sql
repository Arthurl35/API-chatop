CREATE DATABASE rentals;
USE rentals;

CREATE TABLE `USERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `email` VARCHAR(255),
  `name` VARCHAR(255),
  `password` VARCHAR(255),
  `created_at` TIMESTAMP,
  `updated_at` TIMESTAMP
);

CREATE TABLE `RENTALS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(255),
  `surface` NUMERIC,
  `price` NUMERIC,
  `picture` VARCHAR(255),
  `description` VARCHAR(2000),
  `owner_id` INT NOT NULL,
  `created_at` TIMESTAMP,
  `updated_at` TIMESTAMP,
  FOREIGN KEY (`owner_id`) REFERENCES `USERS` (`id`)
);

CREATE TABLE `MESSAGES` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `rental_id` INT,
  `user_id` INT,
  `message` VARCHAR(2000),
  `created_at` TIMESTAMP,
  `updated_at` TIMESTAMP,
  FOREIGN KEY (`rental_id`) REFERENCES `RENTALS` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`)
);

CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);


