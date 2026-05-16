CREATE TABLE `user`(
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `password` VARCHAR(50) NOT NULL,
    `alerting` TINYINT(1) NOT NULL DEFAULT 0,
    `threshold` DOUBLE NOT NULL DEFAULT 0,
    UNIQUE KEY  `uk_user_email` (`email`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;