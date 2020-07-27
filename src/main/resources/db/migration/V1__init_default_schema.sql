CREATE TABLE IF NOT EXISTS `posts`(
    `id` INT AUTO_INCREMENT,
    `author` VARCHAR(255) NOT NULL,
    `body` CLOB,
    `created_at` TIMESTAMP,
    `deleted_at` TIMESTAMP,
    PRIMARY KEY(`id`)
)