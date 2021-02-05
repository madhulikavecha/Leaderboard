CREATE TABLE IF NOT EXISTS leaderboard (
    pos int ,
    username varchar(20),
    nick varchar(20)NOT NULL  PRIMARY KEY,
    score int,
    country varchar(20)

);

INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'asd','glooter',9999 ,'usa');
INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'johan','madhu',100 ,'usa');
INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'eric','madhu2',111 ,'usa');
INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'sofia','madhu3',500 ,'usa');
INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'rohan','madhu5',-1 ,'usa');

INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'deepal','madhuEU',111 ,'eu');
INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'tomas','madhEU',500 ,'eu');
INSERT INTO leaderboard (pos,username,nick, score,country) VALUES (1,'mania','maEU4',-1 ,'eu');