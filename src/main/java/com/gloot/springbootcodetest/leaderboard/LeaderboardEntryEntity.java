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
  private Integer pos ;
  @Id
  private String nick;
  private String username;
  private Integer score;
  private String country;


  public LeaderboardEntryEntity(String username,String country) {
    this.username=username;
    this.country=country;
    this.score=0;
    pos=1;
  }
}
