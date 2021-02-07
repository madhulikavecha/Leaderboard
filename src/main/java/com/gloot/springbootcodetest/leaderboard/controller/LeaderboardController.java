package com.gloot.springbootcodetest.leaderboard.controller;


import com.gloot.springbootcodetest.leaderboard.dto.LeaderboardEntryDto;
import com.gloot.springbootcodetest.leaderboard.exception.LeaderboardException;
import com.gloot.springbootcodetest.leaderboard.model.LeaderboardEntryEntity;
import com.gloot.springbootcodetest.leaderboard.service.LeaderboardService;
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
     * @return Top top score user from each country in list <LeaderboardEntryDto>
     */
    @GetMapping
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard() {
        try {
            return (ResponseEntity<List<LeaderboardEntryDto>>) new ResponseEntity(service.getListOfTopLeaderboardEntriesAsDTO(), HttpStatus.OK);

        } catch (LeaderboardException e) {
            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * HTTP Request handler at the /createUser endpoint. Only accepts POST requests
     *
     * @return success message if user is created. A new user is created and unique Nick is also generated..
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
     * HTTP Request handler at the /allusers endpoint
     *
     * @return all the users in leaderboard in json.
     */
    @GetMapping("/allusers")
    public ResponseEntity<List<LeaderboardEntryDto>> getAllUsers() {
        try {
            return (ResponseEntity<List<LeaderboardEntryDto>>) new ResponseEntity(service.getAllUsers(), HttpStatus.OK);

        } catch (LeaderboardException e) {
            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

    }


    /**
     * HTTP Request handler at the /position/{username}/{country} endpoint.
     *
     * @return position of specific user specified to country.
     */
    @GetMapping("/position/{username}/{country}")
    public Object getPositionOfUser(@PathVariable String username, @PathVariable String country) {
        try {
            return service.getPositionOfUserSpecificLeaderboard(username, country);
        } catch (LeaderboardException e) {
            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * HTTP Request handler at the /userscore endpoint.Only accepts POST requests
     *
     * @return success message with new score  of user.
     */
    @PostMapping("/updatescore")
    public ResponseEntity updateScore(@RequestBody LeaderboardEntryEntity entity) {
        try {
            String messageToUser = service.updateScore(entity);
            return new ResponseEntity(messageToUser, new HttpHeaders(), HttpStatus.OK);
        } catch (LeaderboardException e) {
            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * HTTP Request handler at the /country/{country} endpoint.
     * <p>
     * * @return sorted List of user based on score  from country specified.
     *
     * @Pathvariable country specified by the user
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<LeaderboardEntryDto>> topUserScoreByCountry(@PathVariable String country) {
        try {
            return (ResponseEntity<List<LeaderboardEntryDto>>) new ResponseEntity(service.getListOfAllUsersByCountry(country), HttpStatus.OK);
        } catch (LeaderboardException e) {
            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP Request handler at the /delete endpoint.Only accepts POST requests.
     *
     * @return deletes the user and returns success msg if user provide valid username or nick
     * @Pathvariable username or nick can  be  specified by the user
     */
    @PostMapping("/delete")
    public ResponseEntity deleteUser(@RequestBody LeaderboardEntryEntity entity) {
        try {
            String messageToUser = service.deleteUser(entity);
            return new ResponseEntity(messageToUser, new HttpHeaders(), HttpStatus.OK);
        } catch (LeaderboardException e) {
            return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }


    }
}
