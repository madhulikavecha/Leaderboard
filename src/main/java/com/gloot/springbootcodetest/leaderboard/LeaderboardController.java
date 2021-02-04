package com.gloot.springbootcodetest.leaderboard;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gloot.springbootcodetest.Application.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/leaderboard")
@AllArgsConstructor
public class LeaderboardController {
    private final LeaderboardService service;

    /**
     * HTTP Request handler at the api/v1/leaderboard endpoint.
     *
     * @return Top list<User> of Top 1 user score from each country
     * @Pathvariable username specified by the user
     * @Pathvariable country specified by the user
     */
    @GetMapping
    public List<LeaderboardEntryDto> getLeaderboard() {

        return service.getListOfTopLeaderboardEntriesAsDTO();
    }

    /**
     * HTTP Request handler at the /createUser/{username}/{country} endpoint. Only accepts POST requests
     *
     * @return success message. A new user is created and uniquie Nick is also generated.
     * @Pathvariable username specified by the user
     * @Pathvariable country specified by the user
     */
    @PostMapping("/createuser")
    public ResponseEntity createUser(@RequestBody LeaderboardEntryEntity entity) throws LeaderboardException {
        try {
            String messageToUser = service.createUser(entity);
            return new ResponseEntity(messageToUser, new HttpHeaders(), HttpStatus.CREATED);
        } catch (LeaderboardException e) {
            throw new LeaderboardException("Failed to create user");
        }

    }

    /**
     * HTTP Request handler at the /allusers endpoint. Only accepts POST requests
     *
     * @return all the users in leaderboard.
     */
    @GetMapping("/allusers")
    public List<LeaderboardEntryDto> getAllUsers() {
        return service.getAllUsers();
    }

    /**
     * HTTP Request handler at the /position/{username}/{country} endpoint.
     *
     * @return position of user specified to country.
     * @Pathvariable username specified by the user
     * @Pathvariable country specified by the user
     */
    @GetMapping("/position/{username}/{country}")
    public Object getPositionOfUser(@PathVariable String username, @PathVariable String country)  {
        try{
            return service.getPositionOfUserSpecificLeaderboard(username, country);
        }catch (LeaderboardException e){
            return  new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
      }

    /**
     * HTTP Request handler at the /userscore/{username}/{country} endpoint.
     *
     * @return Updates the score of user.
     * @Pathvariable username specified by the user
     * @Pathvariable country specified by the user
     */
    @PostMapping("/updatescore")
    public ResponseEntity updateScore(@RequestBody LeaderboardEntryEntity entity) {
        try{
            String messageToUser= service.updateScore(entity);
            return new ResponseEntity(messageToUser, new HttpHeaders(), HttpStatus.OK);
        }catch (LeaderboardException e){
            return  new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
     }

    /**
     * HTTP Request handler at the /{country} endpoint.
     *
     * @return sorted List of user based on score  from country specified.
     * @Pathvariable country specified by the user
     */
    @GetMapping("/{country}")
    public ResponseEntity<List<LeaderboardEntryDto>> topUserScoreByCountry(@PathVariable String country){
        try{
          return (ResponseEntity<List<LeaderboardEntryDto>>) new ResponseEntity(service.getListOfAllUsersByCountry(country),HttpStatus.OK);
        }catch (LeaderboardException e){
            return  new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP Request handler at the /delete/{username} endpoint.
     *
     * @return deletes the user from datanase and returns success msg
     * @Pathvariable username specified by the user
     */
    @PostMapping("/delete")
    public ResponseEntity deleteUser(@RequestBody LeaderboardEntryEntity entity){
        try{
            String messageToUser= service.deleteUser(entity);
            return new ResponseEntity(messageToUser, new HttpHeaders(), HttpStatus.OK);
        }
        catch (LeaderboardException e){
            return new ResponseEntity(e.getMessage(),new HttpHeaders(),HttpStatus.NOT_FOUND);
        }


    }
}
