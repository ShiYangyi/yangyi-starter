CREATE TABLE IF NOT EXISTS `parking_space` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `is_used` TINYINT(1) NOT NULL DEFAULT 0,
    `receipt_id` BIGINT,
    `parking_lot_name` VARCHAR(45) NOT NULL DEFAULT 1,
    PRIMARY KEY(id)
);