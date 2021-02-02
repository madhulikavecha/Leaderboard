package com.gloot.springbootcodetest.leaderboard;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntryEntity, Integer> {
    LeaderboardEntryEntity findByNick(String nick);

    List<LeaderboardEntryEntity> findAllByCountry (String country, Sort sort);
    List<LeaderboardEntryEntity> findAll (Sort sort);
    LeaderboardEntryEntity findLeaderboardEntryEntityByNick(String nick);

    //List<LeaderboardEntryEntity> findTop5OrderByScore();



    //List<String> findDistinctCountry();
}
