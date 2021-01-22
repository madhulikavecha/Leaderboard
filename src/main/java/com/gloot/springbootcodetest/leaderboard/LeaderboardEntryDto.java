package com.gloot.springbootcodetest.leaderboard;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LeaderboardEntryDto {
  int position;
  String nick;
  int score;
}
