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

    /**
     * @return a list of user(LeaderboardEntryEntity) containing top score user from each country
     */
    public List<LeaderboardEntryDto> getListOfTopLeaderboardEntriesAsDTO() throws LeaderboardException {

        LeaderboardEntryEntity[] allEntriesAsEntities = repository.findAll().toArray(new LeaderboardEntryEntity[]{});
        if (allEntriesAsEntities.length == 0) {
            throw new LeaderboardException("No user are available.Please create Users");
        } else {
            List<String> countryList = repository.findDistinctByCountry();
            LeaderboardEntryEntity[] arrayOfTopUsers = new LeaderboardEntryEntity[countryList.size()];
            for (int i = 0; i < countryList.size(); i++) {
                arrayOfTopUsers[i] = (repository.findAllByCountry(countryList.get(i), Sort.by(Sort.Direction.DESC, "score"))[0]);
            }
            LeaderboardEntryEntity[] sortedArray = Arrays.stream(arrayOfTopUsers).sorted(Comparator.comparing(LeaderboardEntryEntity::getScore).reversed()).toArray(LeaderboardEntryEntity[]::new);
            return convertToEntityDto(updateRanks(sortedArray));
        }

    }

    /**
     * Converts a collection of LeaderboardEntryEntity  to LeaderboardEntryDto
     *
     * @param allEntriesAsEntities Collection of LeaderboardEntryEntity participants
     * @return leaderboardEntryDtos list
     */
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

    /**
     * Creates a new user with username given and creates nick - uniquq ID and save in database
     *
     * @param entity, specified by user
     * @return A success/failure string with message
     */
    public String createUser(LeaderboardEntryEntity entity) throws LeaderboardException {

        LeaderboardEntryEntity[] allEntriesAsEntities = repository.findAll().toArray(new LeaderboardEntryEntity[]{});

        String countryUpdated = entity.getCountry().toLowerCase();
        String username = entity.getUsername();
        String msgToUser = null;
        if (username.length() < 2 || countryUpdated.length() < 2) {
            throw new LeaderboardException("username and country must have atleast 2 characters");
        } else {
            try {
                Random random = new Random();
                String userID = username.substring(0, 3) + allEntriesAsEntities.length + countryUpdated.substring(0, 2) + random.nextInt(99999);
                LeaderboardEntryEntity newUser = new LeaderboardEntryEntity(username, countryUpdated);
                newUser.setNick(userID);
                repository.save(newUser);
                msgToUser = "New User is saved with userName, userId - " + username + ", " + userID;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return msgToUser;
    }

    /**
     * position of user in given country .Position can be searched based on username or nick ID
     *
     * @param username,contry specified by user
     * @return position of given user from specified country
     */
    public String getPositionOfUserSpecificLeaderboard(String username, String country) throws LeaderboardException {
        String countryUpdated = country.toLowerCase();
        List<LeaderboardEntryEntity> user = repository.findByUsername(username);
        LeaderboardEntryEntity userWithUserId = repository.findByNick(username);
        List<LeaderboardEntryDto> sortedList;
        try {
            sortedList = getListOfAllUsersByCountry(countryUpdated);
        } catch (LeaderboardException e) {
            throw new LeaderboardException(e.getMessage());
        }
        String msgToUser = null;
        if (userWithUserId != null && userWithUserId.getCountry()==countryUpdated) {
            Optional<LeaderboardEntryDto> entryEntity = sortedList.stream().filter(i -> i.getUsername() == userWithUserId.getUsername()).findAny();
            msgToUser = "The position of user ( " + username + " ) in " + country + " leaderboard is   " + entryEntity.get().getPosition();
        } else {
            try {
                    checkMultipleAccountsForUser(username);
                    Optional<LeaderboardEntryDto> entryEntity = sortedList.stream().filter(i -> i.getUsername() == user.get(0).getUsername()).findAny();
                    msgToUser = "The position of user ( " + username + " ) in " + country + " leaderboard is   " + entryEntity.get().getPosition();

            } catch (LeaderboardException e) {
                throw new LeaderboardException(e.getMessage());
            }
        }
        return msgToUser;
    }

    /**
     * Update score of user based in username / Nick
     *
     * @return position of given user from specified country
     */
    public String updateScore(LeaderboardEntryEntity entity) throws LeaderboardException {
        int score = entity.getScore();
        String msgToUser = null;
        int newscore;
        String username = entity.getUsername();
        String nickId = entity.getNick();
        if (nickId != null) {
            try {
                LeaderboardEntryEntity userWithUserId = checkIsavailableNickaccount(nickId);
                newscore = userWithUserId.getScore() + score;
                userWithUserId.setScore(newscore);
                repository.save(userWithUserId);
                msgToUser = "User Score is updated User , Score :\" + username + \"," + newscore;
            } catch (LeaderboardException e) {
                throw new LeaderboardException(e.getMessage());
            }
        } else if (username != null && nickId == null) {
            try {
                List<LeaderboardEntryEntity> userList = checkMultipleAccountsForUser(username);
                newscore = userList.get(0).getScore() + score;
                userList.get(0).setScore(newscore);
                repository.save(userList.get(0));
                msgToUser = "User Score is updated User , Score :" + username + "," + newscore;

            } catch (LeaderboardException e) {
                throw new LeaderboardException(e.getMessage());
            }
        } else throw new LeaderboardException("Username or nick cannot be empty!!");

        return msgToUser;
    }


    public List<LeaderboardEntryDto> getListOfAllUsersByCountry(String country) throws LeaderboardException {
        String countryUpdated = country.toLowerCase();
        LeaderboardEntryEntity[] entityListsortedByCountry = repository.findAllByCountry(countryUpdated, Sort.by(Sort.Direction.DESC, "score"));
        if (entityListsortedByCountry.length == 0) {
            List<String> list = repository.findDistinctByCountry();
            throw new LeaderboardException("please select valid country among " + list);
        }
        return convertToEntityDto(updateRanks(entityListsortedByCountry));

    }

    public List<LeaderboardEntryDto> getAllUsers() throws LeaderboardException {
        List<LeaderboardEntryEntity> entityList = repository.findAll(Sort.by(Sort.Direction.DESC, "score"));
        if (entityList.size() == 0) {
            throw new LeaderboardException("No user are available.Please create Users");
        } else
            return convertToEntityDto(updateRanks(entityList.toArray(new LeaderboardEntryEntity[entityList.size()])));
    }

    private LeaderboardEntryEntity[] updateRanks(LeaderboardEntryEntity[] entryEntityList) {
        int rank = 1;
        while (rank <= entryEntityList.length) {
            entryEntityList[rank - 1].setPos(rank);
            rank++;
        }
        return entryEntityList;
    }

    private LeaderboardEntryEntity checkIsavailableNickaccount(String nickId) throws LeaderboardException {
        LeaderboardEntryEntity userWithUserId = repository.findLeaderboardEntryEntityByNick(nickId);
        if (userWithUserId == null) {
            throw new LeaderboardException("Please provide valid  Userid(nick)." + nickId + "  is not found in our records. ");
        }
        return userWithUserId;
    }

    private List<LeaderboardEntryEntity> checkMultipleAccountsForUser(String username) throws LeaderboardException {
        List<LeaderboardEntryEntity> userList = repository.findByUsername(username);
        if (userList.size() > 1) {
            throw new LeaderboardException("There are several accounts created with this username. Please provide a unique userId for username - " + username);
        } else if (userList.size() == 0) {
            throw new LeaderboardException("Please provide valid  username .'" + username + "'  is not found in our records. please provide valid input");
        }
        return userList;
    }

    public String deleteUser(LeaderboardEntryEntity entity) throws LeaderboardException {
        String username = entity.getUsername();
        String nickId = entity.getNick();
        String msgToUser = null;
        if (nickId != null) {
            try {
                repository.delete(checkIsavailableNickaccount(nickId));
                msgToUser = "The user " + nickId + "  has been deleted";
            } catch (LeaderboardException e) {
                throw new LeaderboardException(e.getMessage());
            }
        } else if (username != null && nickId == null) {
            try {
                repository.delete(checkMultipleAccountsForUser(username).get(0));
                msgToUser = "The user " + username + "  has been deleted";
            } catch (LeaderboardException e) {
                throw new LeaderboardException(e.getMessage());
            }
        }
        return msgToUser;
    }
}