package com.gloot.springbootcodetest.leaderboard;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gloot.springbootcodetest.leaderboard.LeaderboardEntryMapper.mapToDto;

@Service
@AllArgsConstructor
public class LeaderboardService {

    private final LeaderboardRepository repository;

    public List<LeaderboardEntryDto> getListOfAllLeaderboardEntriesAsDTO() {
        LeaderboardEntryEntity[] allEntriesAsEntities = repository.findAll().toArray(new LeaderboardEntryEntity[]{});
        List<String> countryList =repository.findDistinctByCountry();
        LeaderboardEntryEntity[] arrayOfTopUsers = new LeaderboardEntryEntity[countryList.size()];

        for (int i=0;i<countryList.size();i++) {
            arrayOfTopUsers[i]=(repository.findAllByCountry(countryList.get(i), Sort.by(Sort.Direction.DESC, "score"))[0]);
        }
        LeaderboardEntryEntity[] sortedArray= Arrays.stream(arrayOfTopUsers).sorted(Comparator.comparing(LeaderboardEntryEntity::getScore).reversed()).toArray(LeaderboardEntryEntity[]::new);
        return convertToEntityDto(updateRanks(sortedArray));
    }

    private List<LeaderboardEntryDto> convertToEntityDto(LeaderboardEntryEntity[] allEntriesAsEntities) {
        LeaderboardEntryDto[] dtoObjects = new LeaderboardEntryDto[allEntriesAsEntities.length];

        for (int i = allEntriesAsEntities.length - 1; i >= 0; i--) {
            dtoObjects[i] = mapToDto(allEntriesAsEntities[i]);
        }

        List<LeaderboardEntryDto> leaderboardEntryDtos = new ArrayList<>();
        for (int j = 0; j < dtoObjects.length; j++) {
            leaderboardEntryDtos.add(dtoObjects[j]);
        }
        return leaderboardEntryDtos;
    }

    public String createUser(String username, String country) {
        LeaderboardEntryEntity[] allEntriesAsEntities = repository.findAll().toArray(new LeaderboardEntryEntity[]{});

        String countryUpdated= country.toLowerCase();
        String msgToUser=null;
        if (username.length() < 2 || country.length() < 2) {
            msgToUser= "username / country must have atleast 2 characters";
        }else{
            Random random = new Random();
            String userID = username.substring(0, 3)  + allEntriesAsEntities.length+ countryUpdated.substring(0, 2)+random.nextInt(99999);
            LeaderboardEntryEntity newUser = new LeaderboardEntryEntity(username, countryUpdated);
            newUser.setNick(userID);
            repository.save(newUser);
            msgToUser= "New User is saved with userName,userId - " + username + "," + userID;
        }

        return msgToUser;
    }

    public String getPositionOfUserSpecificLeaderboard(String username, String country) throws Exception {
        String countryUpdated = country.toLowerCase();
        List<LeaderboardEntryEntity> user = repository.findByUsername(username);
        LeaderboardEntryEntity userWithUserId = repository.findByNick(username);
        List<LeaderboardEntryDto> sortedList = getListOfAllUsersByCountry(countryUpdated);
        String  isValidAccount=checkMultipleAccountsForUser(user,userWithUserId,username);
        String msgToUser=null;
        if (isValidAccount.equals("VALID_USER")) {
            if (user.size() == 1) {
                Optional<LeaderboardEntryDto> entryEntity = sortedList.stream().filter(i -> i.getUsername() == user.get(0).getUsername()).findAny();
                msgToUser = "The position of user ( " + username + " ) in " + country + " leaderboard is   " + entryEntity.get().getPosition();
            } else {
                Optional<LeaderboardEntryDto> entryEntity = sortedList.stream().filter(i -> i.getUsername() == userWithUserId.getUsername()).findAny();
                msgToUser = "The position of user ( " + username + " ) in " + country + " leaderboard is   " + entryEntity.get().getPosition();
            }
        }else
            msgToUser=isValidAccount;
        return msgToUser;
    }

    public String saveUserScore(String username, int score) throws LeaderboardException {
        List<LeaderboardEntryEntity> user = repository.findByUsername(username);
        LeaderboardEntryEntity userWithUserId = repository.findLeaderboardEntryEntityByNick(username);
        String  isValidAccount=checkMultipleAccountsForUser(user,userWithUserId,username);
        String msgToUser=null;
        if (isValidAccount.equals("VALID_USER")) {
            if (user.size() == 1) {
                int newscore = user.get(0).getScore() + score;
                user.get(0).setScore(newscore);
                repository.save(user.get(0));
                msgToUser="User Score is updated User , Score :\" + username + \","+newscore;
            } else {
                int newscore = userWithUserId.getScore() + score;
                userWithUserId.setScore(newscore);
                repository.save(userWithUserId);
                msgToUser="User Score is updated User , Score :\" + username + \","+newscore;
            }
        }else
            msgToUser=isValidAccount;
        return msgToUser;
    }


    public List<LeaderboardEntryDto> getListOfAllUsersByCountry(String country) throws Exception {
        String countryUpdated = country.toLowerCase();
        LeaderboardEntryEntity[] entityListsortedByCountry = repository.findAllByCountry(countryUpdated, Sort.by(Sort.Direction.DESC, "score"));
        if (entityListsortedByCountry .length==0) {
            List<String> list=repository.findDistinctByCountry();
            // List<String> countryList = repository.findDistinctCountry();
            throw new Exception("please select valid country in");
        }

        return convertToEntityDto(updateRanks(entityListsortedByCountry));

    }

  /*public List<LeaderboardEntryEntity> getTopFiveList() {
    return  updateRanks(repository.findAll(Sort.by(Sort.Direction.DESC,"score")));
  }*/

    private LeaderboardEntryEntity[] updateRanks(LeaderboardEntryEntity[] entryEntityList) {
        int rank = 1;
        while(rank <= entryEntityList.length)
        {
            entryEntityList[rank-1].setPos(rank);
            rank++;
        }
        return entryEntityList;
    }

    private String checkMultipleAccountsForUser(List<LeaderboardEntryEntity> user,LeaderboardEntryEntity userWithUserId,String username) {
        String isValidAccount ;

        if (user.size()==0 && userWithUserId == null) {
            isValidAccount = "Please provide valid userID / Userid provided is not found " + username;
        } else if (user.size() > 1) {
            isValidAccount = "There are several accounts created with this username. Please provide a unique userId for username - " + username;
        }else
            isValidAccount="VALID_USER";

        return isValidAccount;
    }

    public String deleteUser(String username) {
        List<LeaderboardEntryEntity> user = repository.findByUsername(username);
        LeaderboardEntryEntity userWithUserId = repository.findLeaderboardEntryEntityByNick(username);
        String  isValidAccount=checkMultipleAccountsForUser(user,userWithUserId,username);
        String msgToUser = null;
        if (isValidAccount.equals("VALID_USER"))  {
            if (user.size() == 1) {
                    repository.delete(user.get(0));
                } else {
                    repository.delete(userWithUserId);
                }
            msgToUser = "The user " + username + "  has been deleted";
            } else {
                msgToUser = isValidAccount;
            }
        return msgToUser;
    }
}