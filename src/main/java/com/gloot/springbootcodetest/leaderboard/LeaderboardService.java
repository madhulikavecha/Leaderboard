package com.gloot.springbootcodetest.leaderboard;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

import java.util.*;

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
    Random random = new Random();
    String userID = username.substring(0,3)+country.substring(0,2)+random.nextInt(99999);
    LeaderboardEntryEntity newUser = new LeaderboardEntryEntity(username,country);
    newUser.setNick(userID);
    repository.save(newUser);
    return "New User is saved with userName,userId - "+username+","+userID;

  }

  public String getPositionOfUserSpecificLeaderboard(String username,String country) throws Exception {
    List<LeaderboardEntryEntity> user = repository.findByUsername(username);
    LeaderboardEntryEntity userWithUserId = repository.findByNick(username);
    List<LeaderboardEntryEntity> sortedList  = getListOfAllUsersByCountry(country);
    if(checkMultipleAccountsForUser(username) == "None"){
      if(user.size()==1){
       // return user.get(0).getPos().toString();
       Optional<LeaderboardEntryEntity> entryEntity = sortedList.stream().filter(i->i.getUsername()==user.get(0).getUsername()).findAny();
        return "The position of user ( "+username+ " ) in " +country+" leaderboard is   " +entryEntity.get().getPos().toString();
      }
      else {
       // return userWithUserId.getPos().toString();
        Optional<LeaderboardEntryEntity> entryEntity = sortedList.stream().filter(i->i.getUsername()==userWithUserId.getUsername()).findAny();
        return "The position of user ( "+username+ " ) in " +country+" leaderboard is   " +entryEntity.get().getPos().toString();
      }
    }return checkMultipleAccountsForUser(username);


  }

  public String saveUserScore(String username, int score) {
    List<LeaderboardEntryEntity> user = repository.findByUsername(username);
    LeaderboardEntryEntity userWithUserId = repository.findLeaderboardEntryEntityByNick(username);
    if(checkMultipleAccountsForUser(username) == "None"){
      if(user.size()==1){
        int newscore=user.get(0).getScore()+score;
        user.get(0).setScore(newscore);
        repository.save(user.get(0));
        return "User Score is updated User , Score :"+username+","+ newscore ;
      }
      else{
        int newscore=userWithUserId.getScore()+score;
        userWithUserId.setScore(newscore);
        repository.save(userWithUserId);
        return "User Score is updated User , Score :"+username +" ,"+newscore;
      }
    }return checkMultipleAccountsForUser(username);

  }


  public List<LeaderboardEntryEntity> getListOfAllUsersByCountry(String country) throws Exception {
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

  private String checkMultipleAccountsForUser(String username){
    List<LeaderboardEntryEntity> user = repository.findByUsername(username);
    LeaderboardEntryEntity userWithUserId = repository.findLeaderboardEntryEntityByNick(username);
    if(user== null && userWithUserId== null){
      return "Please provide valid userID / Userid provided is not found" + username;
    }
    else if(user.size()>1){
      return "There are several accounts created with this username please provide unique userId  ";
    }

    return "None";

  }

  public String deleteUser(String username) throws LeaderboardException {
    List<LeaderboardEntryEntity> user = repository.findByUsername(username);
    LeaderboardEntryEntity userWithUserId = repository.findLeaderboardEntryEntityByNick(username);
    if(checkMultipleAccountsForUser(username) == "None") {
      if (user.size() == 1) {
        repository.delete(user.get(0));
        return "Sucessfully deleted User - " + username;
      } else {
        repository.delete(userWithUserId);
        return "Sucessfully deleted User - " + username;
      }
    }return checkMultipleAccountsForUser(username);


  }
}
