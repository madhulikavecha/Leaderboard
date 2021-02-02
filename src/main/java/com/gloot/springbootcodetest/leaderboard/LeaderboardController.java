package com.gloot.springbootcodetest.leaderboard;


import static com.gloot.springbootcodetest.Application.API_VERSION_1;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(API_VERSION_1 + "/leaderboard")
@AllArgsConstructor
public class LeaderboardController {
  private final LeaderboardService service;

  @GetMapping
  public List<LeaderboardEntryEntity> getLeaderboard()
  {

    return service.getTopFiveList();
   //return service.getListOfAllLeaderboardEntriesAsDTO();
  }

  @PostMapping("/createUser/{username}/{country}")
  public String createUser(@PathVariable String username,@PathVariable String country)
  {
    return service.createUser(username,country);
  }

  @GetMapping("/alluser")
    public List<LeaderboardEntryDto> getAllUsers(){

      return service.getListOfAllLeaderboardEntriesAsDTO();
    }

  @GetMapping("/position/{username}/{country}")
  public String getPositionOfUser(@PathVariable String username,@PathVariable String country) throws Exception {
    return service.getPositionOfUserSpecificLeaderboard(username,country);
  }

  @PostMapping("/userscore/{username}/{score}")
    public String saveUserScore(@PathVariable String username,@PathVariable int score){
    return service.saveUserScore(username,score);
    }

    @GetMapping("/{country}")
  public List<LeaderboardEntryEntity> topUsaUserScore(@PathVariable String country) throws Exception {
    return service.getListOfAllUsersByCountry(country);
    }

    @PostMapping("/delete/{userid}")
  public String deleteUser(@PathVariable String userid) throws LeaderboardException {

    return service.deleteUser(userid);
    }


}
