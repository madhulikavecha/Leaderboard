package com.gloot.springbootcodetest.leaderboard;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

public class LeaderboardControllerTest extends SpringBootComponentTest {

  @Autowired private MockMvc mockMvc;
  @Autowired LeaderboardRepository repository;

  @Test
  void getLeaderboardTest() throws Exception {
    LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1,"g-looter", "g-looter", 100,"USA");
    repository.saveAll(List.of(entity));

    mockMvc.perform(get("/api/v1/leaderboard"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].nick", is(entity.getNick())))
         .andExpect(jsonPath("$.[0].username", is(entity.getUsername())))
        .andExpect(jsonPath("$.[0].score", is(entity.getScore())))
         .andExpect(jsonPath("$.[0].country", is(entity.getCountry())));
  }

  @Test
  void getLeaderboardByCountryTest() throws Exception {
    List<LeaderboardEntryEntity> entities = List
            .of(new LeaderboardEntryEntity(1, "g-looter", "g-looter-1", 100, "USA"),
                    new LeaderboardEntryEntity(2, "g-looter2", "g-looter-2", 90, "USA"));
    repository.saveAll(entities);

    mockMvc.perform(get("/api/v1/leaderboard/USA"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].nick", is(entities.get(0).getNick())))
            .andExpect(jsonPath("$.[0].username", is(entities.get(0).getUsername())))
            .andExpect(jsonPath("$.[0].score", is(entities.get(0).getScore())))
            .andExpect(jsonPath("$.[0].country", is(entities.get(0).getCountry())));

  }

  @Test
  void getPositionOfUserBasedOnLeaderboardTest() throws Exception {
    repository.deleteAll();
    List<LeaderboardEntryEntity> entities = List
            .of(new LeaderboardEntryEntity(1, "g-looter", "g-looterxyz", 100, "usa"),
                    new LeaderboardEntryEntity(2,"g-looter2", "g-looter-1", 100,"usa"),
                    new LeaderboardEntryEntity(1,"g-looter3", "g-looter-1", 100,"eu"),
                    new LeaderboardEntryEntity(1, "g-looter4", "g-looter-2", 90, "eu"));
    repository.saveAll(entities);
    MvcResult position =mockMvc.perform(get("/api/v1/leaderboard/position/g-looter2/usa"))
            .andExpect(status().isOk())
            .andReturn();
    assertEquals("The position of user ( g-looter2 ) in usa leaderboard is   2",position.getResponse().getContentAsString());

  }

}
