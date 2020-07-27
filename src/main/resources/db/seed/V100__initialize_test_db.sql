INSERT INTO `posts`(`id`, `author`, `body`, `created_at`, `deleted_at`) VALUES
    (DEFAULT, 'mobal', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', NOW(), null),
    (DEFAULT, 'mobal', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', NOW(), null);

INSERT INTO `tags`(`id`, `name`, `description`, `created_at`, `deleted_at`) VALUES
    (DEFAULT, 'lorem', null, NOW(), null),
    (DEFAULT, 'ipsum', null, NOW(), null),
    (DEFAULT, 'dolor', null, NOW(), null);

INSERT INTO `metas`(`id`, `slug`, `views`) VALUES
    (DEFAULT, 'lorem-ipsum', 10),
    (DEFAULT, 'dolore', 10);
