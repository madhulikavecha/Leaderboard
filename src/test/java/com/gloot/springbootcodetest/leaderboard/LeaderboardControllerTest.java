package com.gloot.springbootcodetest.leaderboard;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class LeaderboardControllerTest extends SpringBootComponentTest {

  @Autowired private MockMvc mockMvc;
  @Autowired LeaderboardRepository repository;

  @Test
  void getLeaderboardTest() throws Exception {
    LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1,"g-looter", "g-looter", 100,"USA");
    repository.saveAll(List.of(entity));

    mockMvc.perform(get("/api/v1/leaderboard"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[0].position", is(entity.getPos())))
        .andExpect(jsonPath("$.[0].nick", is(entity.getNick())))
        .andExpect(jsonPath("$.[0].score", is(entity.getScore())));
  }
}
