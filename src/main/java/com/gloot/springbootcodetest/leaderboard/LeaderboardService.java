package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
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

  public String createUser(String username, String country) {
    LeaderboardEntryEntity[] allEntriesAsEntities = repository.findAll().toArray(new LeaderboardEntryEntity[]{});

    if(username.length()<2 || country.length()<2){
      return "username / country must have atleast 2 characters";
    }
    String userID = username.substring(0,2)+country.substring(0,2)+(allEntriesAsEntities.length+1);
    LeaderboardEntryEntity newUser = new LeaderboardEntryEntity(userID,country);
    repository.save(newUser);
    return "New User is saved with userName"+userID;

  }

  public int getPositionOfUserSpecificLeaderboard(String username) {
    LeaderboardEntryEntity userDetails = repository.findByNick(username);
    return userDetails.getPos();
  }

  public String saveUserScore(String username, int score) {
    LeaderboardEntryEntity[] allEntriesAsEntities = repository.findAll().toArray(new LeaderboardEntryEntity[]{});
    LeaderboardEntryEntity userDetails = repository.findByNick(username);
    if(userDetails==null){
      return "Please check your username or create new user --   http://localhost:8080/api/v1/leaderboard/createUser/username/country ";
    }else{
      userDetails.setScore(userDetails.getScore()+score);
      repository.save(userDetails);
      return "User Score is updated User , Score :"+username+","+userDetails.getScore();
    }
  }


  public List<LeaderboardEntryEntity> getListOfAllLeaderboardUSA(String country) throws Exception {
    List<LeaderboardEntryEntity> entityListsortedByCountry = repository.findAllByCountry(country,Sort.by(Sort.Direction.DESC,"score"));
    if(entityListsortedByCountry == null){
     // List<String> countryList = repository.findDistinctCountry();
      throw new Exception("please select valid country in");
    }
    return updateRanks(entityListsortedByCountry);

  }

  public List<LeaderboardEntryEntity> getTopFiveList() {
    return  updateRanks(repository.findAll(Sort.by(Sort.Direction.DESC,"score")));
  }

  private List<LeaderboardEntryEntity> updateRanks(List<LeaderboardEntryEntity> entryEntityList){
    int rank =1;
    for(int i=0;i<entryEntityList.size();i++){
     entryEntityList.get(i).setPos(rank);
     rank++;
    }
    return entryEntityList;
  }

  public String deleteUser(String userid) {
    LeaderboardEntryEntity user = repository.findLeaderboardEntryEntityByNick(userid);
    if(user== null){
      return "Please provide valid userID / Userid provided is not found" + userid;
    }
    repository.delete(user);
    return "Sucessfully deleted User - "+ userid ;
  }
}
