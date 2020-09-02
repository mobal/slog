INSERT INTO `posts`(`id`, `author`, `body`, `title`, `visible`, `created_at`, `deleted_at`, `published_at`, `updated_at`) VALUES
    (DEFAULT, 'mobal', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Lorem ipsum', DEFAULT, NOW(), null, '1970-01-01 00:00:00', NOW()),
    (DEFAULT, 'mobal', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Dolor', 0, NOW(), null, '1970-01-01 00:00:00', NOW()),
    (DEFAULT, 'mobal', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 'Amet', DEFAULT, NOW(), null, '2100-01-01 00:00:00', NOW());

INSERT INTO `tags`(`id`, `name`, `description`, `created_at`, `deleted_at`) VALUES
    (DEFAULT, 'lorem', null, NOW(), null),
    (DEFAULT, 'ipsum', null, NOW(), null),
    (DEFAULT, 'dolor', null, NOW(), null),
    (DEFAULT, 'amet', null, NOW(), null);

INSERT INTO `metas`(`id`, `slug`, `views`, `post_id`) VALUES
    (DEFAULT, 'lorem-ipsum', 10, 1),
    (DEFAULT, 'dolor', 10, 2),
    (DEFAULT, 'amet', 0, 3);

INSERT INTO `users`(`id`, `user_id`, `display_name`, `email`, `name`, `password`, `username`, `created_at`, `deleted_at`, `activation`) VALUES
    (DEFAULT, 100, 'root', 'root@localhost', 'root', 'passwd', 'root', NOW(), null, null);

INSERT INTO `posts_tags`(`id`, `post_id`, `tag_id`) VALUES
    (DEFAULT, '1', '1'),
    (DEFAULT, '1', '2'),
    (DEFAULT, '2', '3'),
    (DEFAULT, '3', '3');
