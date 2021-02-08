package com.gloot.springbootcodetest.leaderboard;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import com.gloot.springbootcodetest.leaderboard.dto.LeaderboardEntryDto;
import com.gloot.springbootcodetest.leaderboard.exception.LeaderboardException;
import com.gloot.springbootcodetest.leaderboard.model.LeaderboardEntryEntity;
import com.gloot.springbootcodetest.leaderboard.repository.LeaderboardRepository;
import com.gloot.springbootcodetest.leaderboard.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaderboardServiceTest extends SpringBootComponentTest {

    @Autowired
    LeaderboardRepository repository;
    @Autowired
    LeaderboardService service;


    /**
     * test to get list of LeaderboardEntryEntity  containing top score user from each country
     *
     * @throws LeaderboardException if no users are available
     */
    @Test
    void getLeaderboard() throws LeaderboardException {
        repository.deleteAll();
        List<LeaderboardEntryEntity> entities = List
                .of(new LeaderboardEntryEntity(1, "g-lous555", "g-looter-1", 100, "usa"),
                        new LeaderboardEntryEntity(1, "g-lo00us565", "g-looter-1", 100, "usa"),
                        new LeaderboardEntryEntity(1, "g-lo1us5658", "g-looter-1", -1, "usa"),
                        new LeaderboardEntryEntity(1, "use1sw32e", "g-looter-1", 999, "sweden"),
                        new LeaderboardEntryEntity(1, "g-use3swe12", "g-looter-1", 100, "sweden"),
                        new LeaderboardEntryEntity(1, "us32eu234", "europeuser1", 100, "eu"),
                        new LeaderboardEntryEntity(1, "eu32eu554", "euuser2", 68294, "eu"),
                        new LeaderboardEntryEntity(2, "lr2usa2323", "euuser3", 123, "usa"));
        repository.saveAll(entities);

        List<LeaderboardEntryEntity> expectedEntities = List
                .of(new LeaderboardEntryEntity(1, "eu32eu554", "euuser2", 68294, "eu"),
                        new LeaderboardEntryEntity(2, "use1sw32e", "g-looter-1", 999, "sweden"),
                        new LeaderboardEntryEntity(3, "lr2usa2323", "euuser3", 123, "usa"));
        List<LeaderboardEntryDto> actualLeaderBoardList = service
                .getListOfTopLeaderboardEntriesAsDTO();
        assertEquals(expectedEntities.size(), actualLeaderBoardList.size());
        for (int i = 0; i < actualLeaderBoardList.size(); i++) {
            assertEqual(expectedEntities.get(i), actualLeaderBoardList.get(i));
        }
    }

    /**
     * test to get all list of LeaderboardEntryEntity  from all countries country in sorted order
     *
     * @throws LeaderboardException if no users are available
     */
    @Test
    void getLeaderboardAllusersTest() throws LeaderboardException {
        repository.deleteAll();
        List<LeaderboardEntryEntity> entities = List
                .of(new LeaderboardEntryEntity(1, "g-loo23us23", "g-looter", 100, "USA"),
                        new LeaderboardEntryEntity(2, "g-lo3us79", "g-looter", 90, "USA"));
        repository.saveAll(entities);

        List<LeaderboardEntryDto> leaderboard = service
                .getAllUsers();
        assertEquals(entities.size(), leaderboard.size());
        for (int i = 0; i < entities.size(); i++) {
            assertEqual(entities.get(i), leaderboard.get(i));
        }
    }

    private void assertEqual(LeaderboardEntryEntity entity, LeaderboardEntryDto dto) {
        assertEquals(entity.getPos(), dto.getPosition());
        assertEquals(entity.getNick(), dto.getNick());
        assertEquals(entity.getScore(), dto.getScore());
    }

    /**
     * test to get list of LeaderboardEntryEntity  user from country specified sorted by score
     *
     * @throws LeaderboardException if no users are available
     */
    @Test
    void getLeaderboardByCountry() throws Exception {
        repository.deleteAll();
        List<LeaderboardEntryEntity> entities = List
                .of(new LeaderboardEntryEntity(1, "g-looter", "g-looter-1", 100, "usa"),
                        new LeaderboardEntryEntity(1, "g-lo1us998", "g-looter-1", 100, "usa"),
                        new LeaderboardEntryEntity(1, "g-lo11us797", "g-looter-1", -1, "usa"),
                        new LeaderboardEntryEntity(1, "use1sw32e", "g-looter-1", 999, "sweden"),
                        new LeaderboardEntryEntity(1, "g-use3swe12", "g-looter-1", 100, "sweden"),
                        new LeaderboardEntryEntity(1, "us32eu234", "europeuser1", 100, "eu"),
                        new LeaderboardEntryEntity(1, "eu32eu554", "euuser2", 68294, "eu"),
                        new LeaderboardEntryEntity(2, "lr2usa2323", "euuser3", 123, "usa"));
        repository.saveAll(entities);

        List<LeaderboardEntryEntity> expectedEntities = List
                .of(new LeaderboardEntryEntity(1, "eu32eu554", "euuser2", 68294, "eu"),
                        new LeaderboardEntryEntity(2, "us32eu234", "europeuser1", 100, "eu"));

        List<LeaderboardEntryDto> leaderboardByCountry = service.getListOfAllUsersByCountry("EU");
        assertEquals(expectedEntities.size(), leaderboardByCountry.size());

        assertEquals(expectedEntities.size(), leaderboardByCountry.size());
        for (int i = 0; i < leaderboardByCountry.size(); i++) {
            assertEqual(expectedEntities.get(i), leaderboardByCountry.get(i));
        }
    }


}
