CREATE TABLE IF NOT EXISTS leaderboard (
    pos int ,
    nick varchar(20)NOT NULL  PRIMARY KEY,
    score int,
    country varchar(20)

);

INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'xyz',9999 ,'USA');
INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'madhu',100 ,'USA');
INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'madhu2',111 ,'USA');
INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'madhu3',500 ,'USA');
INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'madhu5',-1 ,'USA');

INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'madhuEU',111 ,'EU');
INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'madhEU',500 ,'EU');
INSERT INTO leaderboard (pos,nick, score,country) VALUES (1,'maEU4',-1 ,'EU');