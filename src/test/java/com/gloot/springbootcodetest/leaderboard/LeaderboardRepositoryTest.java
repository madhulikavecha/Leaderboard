package com.gloot.springbootcodetest.leaderboard;

import com.gloot.springbootcodetest.SpringBootComponentTest;
import com.gloot.springbootcodetest.leaderboard.model.LeaderboardEntryEntity;
import com.gloot.springbootcodetest.leaderboard.repository.LeaderboardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LeaderboardRepositoryTest extends SpringBootComponentTest {
    @Autowired
    LeaderboardRepository repository;

    @Test
    void saveAndRetrieve() {
        LeaderboardEntryEntity entity = new LeaderboardEntryEntity(1, "g-lo2us8594r", "g-looter", 100, "USA");
        repository.saveAll(List.of(entity));
        LeaderboardEntryEntity fromRepository = repository.findByNick(entity.getNick());
        assertThat(fromRepository, is(entity));
    }
}
