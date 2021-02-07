CREATE TABLE IF NOT EXISTS leaderboard
(
    pos      int,
    username varchar(20),
    nick     varchar(20) NOT NULL PRIMARY KEY,
    score    int,
    country  varchar(20)

);

INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'glooter', 'gl3us23', 9999, 'usa');
INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'johan', 'jo2us23', 100, 'usa');
INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'eric', 'er3us2334', 111, 'usa');
INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'sofia', 'so4us794', 500, 'usa');
INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'rohan', 'ro5us6389', -1, 'usa');

INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'deepak', 'de6eu232', 111, 'eu');
INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'tomas', 'to7eu231', 500, 'eu');
INSERT INTO leaderboard (pos, username, nick, score, country)
VALUES (1, 'mania', 'ma8eu546', -1, 'eu');