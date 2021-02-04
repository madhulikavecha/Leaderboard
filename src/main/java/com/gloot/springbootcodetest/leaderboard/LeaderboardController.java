package com.gloot.springbootcodetest.leaderboard;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gloot.springbootcodetest.Application.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/leaderboard")
@AllArgsConstructor
public class LeaderboardController {
    private final LeaderboardService service;

    @GetMapping
    public List<LeaderboardEntryDto> getLeaderboard() {

        //return service.getTopFiveList();
        return service.getListOfAllLeaderboardEntriesAsDTO();
    }

    /**
     * HTTP Request handler at the /createUser/{username}/{country} endpoint. Only accepts POST requests
     *
     * @return success message. A new user is created and uniquie Nick is also generated.
     * @Pathvariable username specified by the user
     * @Pathvariable country specified by the user
     */
    @PostMapping("/createuser/{username}/{country}")
    public String createUser(@PathVariable String username, @PathVariable String country) {
        return service.createUser(username, country);
    }

    @GetMapping("/allusers")
    public List<LeaderboardEntryDto> getAllUsers() {
        return service.getListOfAllLeaderboardEntriesAsDTO();
    }

    /**
     * HTTP Request handler at the /position/{username}/{country} endpoint.
     *
     * @return position of user specified to country.
     * @Pathvariable username specified by the user
     * @Pathvariable country specified by the user
     */
    @GetMapping("/position/{username}/{country}")
    public String getPositionOfUser(@PathVariable String username, @PathVariable String country) throws Exception {
        return service.getPositionOfUserSpecificLeaderboard(username, country);
    }

    /**
     * HTTP Request handler at the /userscore/{username}/{country} endpoint.
     *
     * @return Updates the score of user.
     * @Pathvariable username specified by the user
     * @Pathvariable country specified by the user
     */
    @PostMapping("/userscore/{username}/{score}")
    public String saveUserScore(@PathVariable String username, @PathVariable int score) throws LeaderboardException {
        return service.saveUserScore(username, score);
    }

    /**
     * HTTP Request handler at the /{country} endpoint.
     *
     * @return sorted List of user based on score  from country specified.
     * @Pathvariable country specified by the user
     */
    @GetMapping("/{country}")
    public List<LeaderboardEntryDto> topUsaUserScore(@PathVariable String country) throws Exception {
        return service.getListOfAllUsersByCountry(country);
    }

    /**
     * HTTP Request handler at the /delete/{username} endpoint.
     *
     * @return deletes the user from datanase and returns success msg
     * @Pathvariable username specified by the user
     */
    @PostMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
      return service.deleteUser(username);
    }
}
