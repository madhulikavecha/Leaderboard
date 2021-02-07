package com.gloot.springbootcodetest.leaderboard.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LeaderboardEntryDto {
    int position;
    String nick;
    String username;
    int score;
    String country;

}
