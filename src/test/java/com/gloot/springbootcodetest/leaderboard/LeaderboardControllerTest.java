package com.gloot.springbootcodetest.leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gloot.springbootcodetest.SpringBootComponentTest;
import com.gloot.springbootcodetest.leaderboard.model.LeaderboardEntryEntity;
import com.gloot.springbootcodetest.leaderboard.repository.LeaderboardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LeaderboardControllerTest extends SpringBootComponentTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    LeaderboardRepository repository;

    /**
     * test to get list of LeaderboardEntryEntity  containing top score user from each country - "/api/v1/leaderboard"
     *
     * @throws Exception
     */
    @Test
    void getLeaderboardTest() throws Exception {
        repository.deleteAll();
        LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1, "g-lo7us678", "g-looter", 100, "usa");
        repository.saveAll(List.of(entity));

        mockMvc.perform(get("/api/v1/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].nick", is(entity.getNick())))
                .andExpect(jsonPath("$.[0].username", is(entity.getUsername())))
                .andExpect(jsonPath("$.[0].score", is(entity.getScore())))
                .andExpect(jsonPath("$.[0].country", is(entity.getCountry())));
    }

    /**
     * test to get list of LeaderboardEntryEntity  user from country specified sorted by score - "/api/v1/leaderboard/country/{anycountry}"
     *
     * @throws Exception
     */
    @Test
    void getLeaderboardByCountryTest() throws Exception {
        repository.deleteAll();
        List<LeaderboardEntryEntity> entities = List
                .of(new LeaderboardEntryEntity(1, "g-lo9uus2322", "g-looter-1", 100, "usa"),
                        new LeaderboardEntryEntity(2, "g-lo0ua55", "g-looter-2", 90, "usa"));
        repository.saveAll(entities);

        mockMvc.perform(get("/api/v1/leaderboard/country/USA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].nick", is(entities.get(0).getNick())))
                .andExpect(jsonPath("$.[0].username", is(entities.get(0).getUsername())))
                .andExpect(jsonPath("$.[0].score", is(entities.get(0).getScore())))
                .andExpect(jsonPath("$.[0].country", is(entities.get(0).getCountry())));

    }

    /**
     * test to get position of LeaderboardEntryEntity  user from country specified ,username or nick specified  - "/api/v1/leaderboard/position/{username or nick}/{anycountry}"
     *
     * @throws Exception
     */
    @Test
    void getPositionOfUserBasedOnLeaderboardTest() throws Exception {
        repository.deleteAll();
        List<LeaderboardEntryEntity> entities = List
                .of(new LeaderboardEntryEntity(1, "g-lo2us855", "g-looter", 100, "usa"),
                        new LeaderboardEntryEntity(1, "g-lo3us234", "g-looter", 100, "usa"),
                        new LeaderboardEntryEntity(1, "g-l4us2654", "g-looter", 100, "eu"),
                        new LeaderboardEntryEntity(1, "g-loo7us948", "g-looter", 90, "eu"));
        repository.saveAll(entities);
        MvcResult position = mockMvc.perform(get("/api/v1/leaderboard/position/g-lo3us234/usa"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("The position of user ( g-lo3us234 ) in usa leaderboard is   2", position.getResponse().getContentAsString());

    }

    /**
     * test to create new user (LeaderboardEntryEntity)  from country specified ,username or nick specified  - "/api/v1/leaderboard/createuser"
     *
     * @throws Exception
     */
    @Test
    void createUserTest() throws Exception {
        repository.deleteAll();

        LeaderboardEntryEntity entity = new LeaderboardEntryEntity();
        entity.setUsername("username1");
        entity.setCountry("sweden");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entity);

        MvcResult result = mockMvc.perform(post("/api/v1/leaderboard/createuser")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andReturn();

    }

    /**
     * test to update  user (LeaderboardEntryEntity) score  for  username or nick specified and given score - "/api/v1/leaderboard/updatescore"
     *
     * @throws Exception
     */
    @Test
    void updateUserScoreTest() throws Exception {
        repository.deleteAll();
        LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1, "glo4us234", "username1", 100, "usa");
        repository.saveAll(List.of(entity));
        String username = "username1";

        LeaderboardEntryEntity entityToUpdate = new LeaderboardEntryEntity();
        entityToUpdate.setUsername("username1");
        entityToUpdate.setScore(200);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entityToUpdate);

        MvcResult result = mockMvc.perform(post("/api/v1/leaderboard/updatescore")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andReturn();
        assertEquals("User Score is updated User , Score :" + username + "," + 300, result.getResponse().getContentAsString());
    }


    /**
     * test to delete user (LeaderboardEntryEntity)  username or nick specified  - "/api/v1/leaderboard/delete"
     *
     * @throws Exception
     */
    @Test
    void deleteUserTest() throws Exception {
        LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1, "glo4us234", "username1", 100, "usa");
        repository.saveAll(List.of(entity));
        String username = "username1";

        LeaderboardEntryEntity entityToDelete = new LeaderboardEntryEntity();
        entityToDelete.setUsername("username1");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entityToDelete);

        MvcResult result = mockMvc.perform(post("/api/v1/leaderboard/delete")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andReturn();
        assertEquals("The user " + username + "  has been deleted", result.getResponse().getContentAsString());

    }

    @Test
    void invalidUrl_returnsHttp404() throws Exception {
        LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1, "glo4us234", "username1", 100, "usa");
        repository.saveAll(List.of(entity));
        String username = "username1";

        LeaderboardEntryEntity entityToUpadte = new LeaderboardEntryEntity();
        entityToUpadte.setUsername("username1");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entityToUpadte);

        MvcResult result = mockMvc.perform(post("/api/v1/leaderboard/updateuser/wrongurl/badrequest")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound()).andReturn();
        assertEquals("The URL you have reached is not in service at this time (404). ", result.getResponse().getContentAsString());

    }


    @Test
    void invalidData_returnsInternalServeError() throws Exception {
        LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1, "glo4us234", "username1", 100, "usa");
        repository.saveAll(List.of(entity));
        String username = "username1";

        LeaderboardEntryEntity entityToUpadte = new LeaderboardEntryEntity();
        entityToUpadte.setUsername("username1");
        entityToUpadte.setCountry("usa");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entityToUpadte);

        MvcResult result = mockMvc.perform(post("/api/v1/leaderboard/updatescore")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isInternalServerError()).andReturn();
        assertEquals("Invalid HTTP request !! Please provide valid data!! ", result.getResponse().getContentAsString());

    }


}
