CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(45) NOT NULL DEFAULT 1,
    `password` longtext NOT NULL,
    CONSTRAINT `USER_PK` PRIMARY KEY (`id`)
);