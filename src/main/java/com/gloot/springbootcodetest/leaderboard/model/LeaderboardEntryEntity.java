package com.gloot.springbootcodetest.leaderboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "leaderboard")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryEntity {
    //@Id
    // @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer pos;
    private String username;
    @Id
    private String nick;
    private Integer score;
    private String country;


    public LeaderboardEntryEntity(String username, String country) {
        this.username = username;
        this.country = country;
        this.score = 0;
        pos = 1;
    }
}
