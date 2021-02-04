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
    repository.deleteAll();
    List<LeaderboardEntryEntity> entities = List
        .of(new LeaderboardEntryEntity(1,"g-looter", "g-looter-1", 100,"usa"),
                new LeaderboardEntryEntity(1,"g-looter2", "g-looter-1", 100,"usa"),
                new LeaderboardEntryEntity(1,"g-looter3", "g-looter-1", -1,"usa"),
                new LeaderboardEntryEntity(1,"use1sw32e", "g-looter-1", 999,"sweden"),
                new LeaderboardEntryEntity(1,"g-use3swe12", "g-looter-1", 100,"sweden"),
                new LeaderboardEntryEntity(1,"us32eu234", "europeuser1", 100,"eu"),
                new LeaderboardEntryEntity(1,"eu32eu554", "euuser2", 68294,"eu"),
            new LeaderboardEntryEntity(2, "lr2usa2323","euuser3", 123,"usa"));
    repository.saveAll(entities);

    List<LeaderboardEntryEntity> expectedEntities = List
            .of(new LeaderboardEntryEntity(1,"eu32eu554", "euuser2", 68294,"eu"),
                    new LeaderboardEntryEntity(2,"use1sw32e", "g-looter-1", 999,"sweden"),
                    new LeaderboardEntryEntity(3,"lr2usa2323", "euuser3", 123,"usa"));
    List<LeaderboardEntryDto> actualLeaderBoardList = service
        .getListOfTopLeaderboardEntriesAsDTO();
    assertEquals(expectedEntities.size(), actualLeaderBoardList.size());
    for(int i=0;i<actualLeaderBoardList.size();i++){
      assertEqual(expectedEntities.get(i), actualLeaderBoardList.get(i));
    }
  }

  @Test
  void getLeaderboardAllusersTest() throws LeaderboardException {
    repository.deleteAll();
    List<LeaderboardEntryEntity> entities = List
            .of(new LeaderboardEntryEntity(1,"g-loo23us23", "g-looter", 100,"USA"),
                    new LeaderboardEntryEntity(2, "g-lo3us79","g-looter", 90,"USA"));
    repository.saveAll(entities);

    List<LeaderboardEntryDto> leaderboard = service
            .getAllUsers();
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
            .of(new LeaderboardEntryEntity(1,"g-looter", "g-looter-1", 100,"usa"),
                    new LeaderboardEntryEntity(1,"g-looter2", "g-looter-1", 100,"usa"),
                    new LeaderboardEntryEntity(1,"g-looter3", "g-looter-1", -1,"usa"),
                    new LeaderboardEntryEntity(1,"use1sw32e", "g-looter-1", 999,"sweden"),
                    new LeaderboardEntryEntity(1,"g-use3swe12", "g-looter-1", 100,"sweden"),
                    new LeaderboardEntryEntity(1,"us32eu234", "europeuser1", 100,"eu"),
                    new LeaderboardEntryEntity(1,"eu32eu554", "euuser2", 68294,"eu"),
                    new LeaderboardEntryEntity(2, "lr2usa2323","euuser3", 123,"usa"));
    repository.saveAll(entities);

    List<LeaderboardEntryEntity> expectedEntities = List
            .of(new LeaderboardEntryEntity(1,"eu32eu554", "euuser2", 68294,"eu"),
                    new LeaderboardEntryEntity(2,"us32eu234", "europeuser1", 100,"eu"));

    List<LeaderboardEntryDto> leaderboardByCountry = service.getListOfAllUsersByCountry("EU");
    assertEquals(expectedEntities.size(), leaderboardByCountry.size());

    assertEquals(expectedEntities.size(), leaderboardByCountry.size());
    for(int i=0;i<leaderboardByCountry.size();i++){
      assertEqual(expectedEntities.get(i), leaderboardByCountry.get(i));
    }
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
