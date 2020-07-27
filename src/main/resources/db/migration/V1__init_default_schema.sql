CREATE TABLE IF NOT EXISTS `posts`(
    `id` INT AUTO_INCREMENT,
    `author` VARCHAR(255) NOT NULL,
    `body` CLOB,
    `title` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP,
    `deleted_at` TIMESTAMP,
    `meta_id` INT,
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

ALTER TABLE `posts` ADD CONSTRAINT `fk_meta_id` FOREIGN KEY(`meta_id`) REFERENCES `metas`(`id`);
ALTER TABLE `metas` ADD CONSTRAINT `fk_post_id` FOREIGN KEY(`post_id`) REFERENCES `posts`(`id`);
