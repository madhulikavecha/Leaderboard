package com.gloot.springbootcodetest.leaderboard.repository;

import com.gloot.springbootcodetest.leaderboard.model.LeaderboardEntryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntryEntity, Integer> {
    LeaderboardEntryEntity findByNick(String nick);
   List<LeaderboardEntryEntity> findByUsername(String username);
    LeaderboardEntryEntity[] findAllByCountry (String country, Sort sort);
    @Query("select distinct user.country from LeaderboardEntryEntity user")
    List<String > findDistinctByCountry();
    @Query("select user from LeaderboardEntryEntity user order by user.score desc ")
    LeaderboardEntryEntity[] findAllTopFive (String country, Sort sort);
    List<LeaderboardEntryEntity> findAll (Sort sort);
    LeaderboardEntryEntity findLeaderboardEntryEntityByNick(String nick);

    //List<LeaderboardEntryEntity> findTop5OrderByScore();



    //List<String> findDistinctCountry();
}
