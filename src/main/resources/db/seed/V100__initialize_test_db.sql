INSERT INTO `posts`(`id`, `author`, `body`, `title`, `created_at`, `deleted_at`) VALUES
    (DEFAULT, 'mobal', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 'Lorem ipsum', NOW(), null),
    (DEFAULT, 'mobal', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Dolor', NOW(), null);

INSERT INTO `tags`(`id`, `name`, `description`, `created_at`, `deleted_at`) VALUES
    (DEFAULT, 'lorem', null, NOW(), null),
    (DEFAULT, 'ipsum', null, NOW(), null),
    (DEFAULT, 'dolor', null, NOW(), null);

INSERT INTO `metas`(`id`, `slug`, `views`, `post_id`) VALUES
    (DEFAULT, 'lorem-ipsum', 10, 1),
    (DEFAULT, 'dolor', 10, 2);

INSERT INTO `posts_tags`(`id`, `post_id`, `tag_id`) VALUES
    (DEFAULT, '1', '1'),
    (DEFAULT, '1', '2'),
    (DEFAULT, '2', '3');
