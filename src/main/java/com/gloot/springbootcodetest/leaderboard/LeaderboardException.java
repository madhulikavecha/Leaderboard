package com.gloot.springbootcodetest.leaderboard;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class LeaderboardException extends RuntimeException{

    public LeaderboardException(String message){
        super(message);
    }
}
