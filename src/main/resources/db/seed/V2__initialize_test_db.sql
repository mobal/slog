INSERT INTO `posts`(`id`, `author`, `body`, `title`, `visible`, `created_at`, `deleted_at`, `published_at`) VALUES
    (DEFAULT, 'mobal', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Lorem ipsum', DEFAULT, NOW(), null, null),
    (DEFAULT, 'mobal', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Dolor', DEFAULT, NOW(), null, null),
    (DEFAULT, 'mobal', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 'Amet', 0, NOW(), null, null);

INSERT INTO `tags`(`id`, `name`, `description`, `created_at`, `deleted_at`) VALUES
    (DEFAULT, 'lorem', null, NOW(), null),
    (DEFAULT, 'ipsum', null, NOW(), null),
    (DEFAULT, 'dolor', null, NOW(), null),
    (DEFAULT, 'amet', null, NOW(), null);

INSERT INTO `metas`(`id`, `slug`, `views`, `post_id`) VALUES
    (DEFAULT, 'lorem-ipsum', 10, 1),
    (DEFAULT, 'dolor', 10, 2),
    (DEFAULT, 'amet', 0, 3);

INSERT INTO `users`(`id`, `user_id`, `display_name`, `email`, `name`, `password`, `username`, `created_at`, `deleted_at`) VALUES
    (DEFAULT, 100, 'dummy', 'user@localhost', 'dummy', 'passwd', 'dummy', NOW(), null);

INSERT INTO `posts_tags`(`id`, `post_id`, `tag_id`) VALUES
    (DEFAULT, '1', '1'),
    (DEFAULT, '1', '2'),
    (DEFAULT, '2', '3'),
    (DEFAULT, '3', '3');
