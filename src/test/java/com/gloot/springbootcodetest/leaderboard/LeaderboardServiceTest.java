package com.gloot.springbootcodetest.leaderboard;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardServiceTest extends SpringBootComponentTest {

  @Autowired LeaderboardRepository repository;
  @Autowired LeaderboardService service;

  @Test
  void getLeaderboard() throws LeaderboardException {
    List<LeaderboardEntryEntity> entities = List
        .of(new LeaderboardEntryEntity(1,"g-looter", "g-looter-1", 100,"USA"),
            new LeaderboardEntryEntity(2, "g-looter2","g-looter-2", 90,"USA"));
    repository.saveAll(entities);

    List<LeaderboardEntryDto> leaderboard = service
        .getListOfTopLeaderboardEntriesAsDTO();
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

  @Test
  void getLeaderboardByCountry() throws Exception {
    repository.deleteAll();
    List<LeaderboardEntryEntity> entities = List
            .of(new LeaderboardEntryEntity(1,"g-looter", "g-looter-1", 100,"USA"),
            new LeaderboardEntryEntity(1,"g-looter2", "g-looter-1", 100,"USA"),
            new LeaderboardEntryEntity(1,"g-looter3", "g-looter-1", 100,"EU"),
                    new LeaderboardEntryEntity(1, "g-looter4","g-looter-2", 90,"EU"));
    repository.saveAll(entities);
    List<LeaderboardEntryDto> leaderboardByCountry = service.getListOfAllUsersByCountry("EU");
    assertEquals(2, leaderboardByCountry.size());

    assertEquals(1,leaderboardByCountry.get(0).getPosition());
    assertEquals(2,leaderboardByCountry.get(1).getPosition());

  }
/*
  @Test
  void createUserTest(){
    repository.deleteAll();
    service.createUser("usercheck","sweden");
    List<LeaderboardEntryEntity> newUser = repository.findByUsername("usercheck");
    assertEquals("usercheck",newUser.get(0).getUsername());
    assertEquals("sweden",newUser.get(0).getCountry());
  }


  @Test
  void deleteUserTest() throws LeaderboardException {
    repository.deleteAll();
    service.createUser("usercheck","sweden");
    List<LeaderboardEntryEntity> newUserBefore = repository.findByUsername("usercheck");
    assertEquals(1,newUserBefore.size());
    service.deleteUser("usercheck");
    List<LeaderboardEntryEntity> newUserAfter = repository.findByUsername("usercheck");
    assertNotEquals(newUserBefore,newUserAfter);
    assertEquals(0,newUserAfter.size());

  }

  @Test
  void updateScoreTest() throws LeaderboardException {
    repository.deleteAll();
    service.createUser("usercheck","sweden");
    service.updateScore("usercheck",500);
    List<LeaderboardEntryEntity> newUser = repository.findByUsername("usercheck");
    assertEquals(500,newUser.get(0).getScore());
    service.updateScore("usercheck",200);
    List<LeaderboardEntryEntity> newUserUpdated = repository.findByUsername("usercheck");
    assertEquals(700,newUserUpdated.get(0).getScore());

  }*/
}
