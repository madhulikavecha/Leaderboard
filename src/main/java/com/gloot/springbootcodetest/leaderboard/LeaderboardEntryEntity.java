package com.gloot.springbootcodetest.leaderboard;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

@Entity
@Table(name = "leaderboard")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryEntity {
  //@Id
 // @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Integer pos =0;
  @Id
  private String nick;
  private Integer score;
  private String country;


  public LeaderboardEntryEntity(String username,String country) {
    nick=username;
    this.score=0;
    this.country=country;
  }
}
