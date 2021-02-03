CREATE TABLE IF NOT EXISTS leaderboard (
    pos int ,
    username varchar(20),
    nick varchar(20)NOT NULL  PRIMARY KEY,
    score int,
    country varchar(20)

);

