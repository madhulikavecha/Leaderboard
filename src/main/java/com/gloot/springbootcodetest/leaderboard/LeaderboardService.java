package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LeaderboardService {

  private final LeaderboardRepository repository;

  public List<LeaderboardEntryDto> getListOfAllLeaderboardEntriesAsDTO() {
    LeaderboardEntryEntity[] allEntriesAsEntities = repository.findAll().toArray(new LeaderboardEntryEntity[]{});
    LeaderboardEntryDto[] dtoObjects = new LeaderboardEntryDto[allEntriesAsEntities.length];

    for(int i=allEntriesAsEntities.length-1;i>=0;i--) {
      dtoObjects[i] = mapToDto(allEntriesAsEntities[i]);
    }

    List<LeaderboardEntryDto> leaderboardEntryDtos = new ArrayList<>();
    for(int j=dtoObjects.length-1;j>=0;j--) {
      leaderboardEntryDtos.add(dtoObjects[j]);
    }

    Collections.sort(leaderboardEntryDtos, new Comparator<LeaderboardEntryDto>(){
      public int compare(LeaderboardEntryDto e1, LeaderboardEntryDto e2) {
        return e1.getPosition() - e2.getPosition();
      }
    });

    return leaderboardEntryDtos;
  }
}
