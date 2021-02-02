package com.gloot.springbootcodetest.leaderboard;

public class LeaderboardEntryMapper {

  public static LeaderboardEntryDto mapToDto(LeaderboardEntryEntity entity) {
    return LeaderboardEntryDto.builder()
        .position(entity.getPos())
        .nick(entity.getNick())
         .username(entity.getUsername())
        .score(entity.getScore())
         .country(entity.getCountry())
        .build();
  }
}
