package com.gloot.springbootcodetest.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LeaderboardServiceTest extends SpringBootComponentTest {

  @Autowired LeaderboardRepository repository;
  @Autowired LeaderboardService service;

  @Test
  void getLeaderboard() {
    List<LeaderboardEntryEntity> entities = List
        .of(new LeaderboardEntryEntity(1,"g-looter", "g-looter-1", 100,"USA"),
            new LeaderboardEntryEntity(2, "g-looter2","g-looter-2", 90,"USA"));
    repository.saveAll(entities);

    List<LeaderboardEntryDto> leaderboard = service
        .getListOfAllLeaderboardEntriesAsDTO();
    assertEquals(entities.size(), leaderboard.size());
    for(int i=0;i<entities.size();i++){
      assertEqual(entities.get(i), leaderboard.get(i));
    }
  }

  private void assertEqual(LeaderboardEntryEntity entity, LeaderboardEntryDto dto) {
    assertEquals(entity.getPos(), dto.getPosition());
    assertEquals(entity.getNick(), dto.getNick());
    assertEquals(entity.getScore(), dto.getScore());
  }
}
