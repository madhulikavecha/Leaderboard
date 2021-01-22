CREATE TABLE IF NOT EXISTS `leaderboard` (
    `pos` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `nick` varchar(20),
    `score` int
);