CREATE TABLE IF NOT EXISTS `posts`(
    `id` INT AUTO_INCREMENT,
    `author` VARCHAR(255) NOT NULL,
    `body` CLOB,
    `created_at` TIMESTAMP,
    `deleted_at` TIMESTAMP,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `tags`(
    `id` INT AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255),
    `created_at` TIMESTAMP,
    `deleted_at` TIMESTAMP,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `metas`(
    `id` INT AUTO_INCREMENT,
    `slug` VARCHAR(255) NOT NULL,
    `views` INT DEFAULT 0,
    PRIMARY KEY(`id`)
);
