CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL,
    `username` varchar(45) NOT NULL,
    `password` varchar(45) NOT NULL,
    `created` timestamp(3) NOT NULL,
    `updated` timestamp(3) NOT NULL,
    CONSTRAINT `USER_PK` PRIMARY KEY (`id`)
);