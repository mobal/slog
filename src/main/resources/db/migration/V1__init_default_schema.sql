CREATE TABLE IF NOT EXISTS `posts`(
    `id` INT AUTO_INCREMENT,
    `author` VARCHAR(255) NOT NULL,
    `body` CLOB,
    `title` VARCHAR(255) NOT NULL,
    `visible` TINYINT(1) DEFAULT(1) NOT NULL,
    `created_at` TIMESTAMP,
    `deleted_at` TIMESTAMP,
    `published_at` TIMESTAMP,
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
    `post_id` INT,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `posts_tags`(
    `id` INT NOT NULL AUTO_INCREMENT,
    `post_id` INT NOT NULL,
    `tag_id` INT NOT NULL,
    FOREIGN KEY(`post_id`) REFERENCES `posts`(`id`),
    FOREIGN KEY(`tag_id`) REFERENCES `tags`(`id`)
);

CREATE TABLE IF NOT EXISTS `users`(
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(36) NOT NULL DEFAULT uuid(),
    `display_name` VARCHAR(255),
    `email` VARCHAR(255),
    `name` VARCHAR(255),
    `password` VARCHAR(255),
    `username` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP,
    `deleted_at` TIMESTAMP
);

ALTER TABLE `metas` ADD CONSTRAINT `fk_post_id` FOREIGN KEY(`post_id`) REFERENCES `posts`(`id`);

CREATE INDEX `idx_posts_author` ON `posts`(`author`);
CREATE INDEX `idx_posts_title` ON `posts`(`title`);
CREATE INDEX `idx_tags_name` ON `tags`(`name`);
CREATE INDEX `idx_users_email` ON `users`(`email`);
CREATE INDEX `idx_users_username` ON `users`(`username`);
CREATE INDEX `idx_users_display_name` ON `users`(`display_name`);
