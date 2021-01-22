package com.gloot.springbootcodetest.leaderboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntryEntity, Integer> {}
