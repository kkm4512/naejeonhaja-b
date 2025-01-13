package com.example.naejeonhajab.domain.game.riot.entity;

import com.example.naejeonhajab.domain.game.riot.dto.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String playerName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RiotAccount riotAccount;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RiotSummoner riotSummoner;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RiotLeague riotLeague;

    @OneToMany(mappedBy = "riotPlayer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RiotChampionMastery> riotChampionMasteries = new ArrayList<>();

    @OneToMany(mappedBy = "riotPlayer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RiotChampion> champions;

    public void updateRiotChampionMastery(List<RiotChampionMastery> riotChampionMasterys) {
        this.riotChampionMasteries = riotChampionMasterys;
    }

    public void updateRiotChampion(List<RiotChampion> champions) {
        this.champions = champions;
    }

    public static RiotPlayerDto toRiotPlayerDto(RiotPlayer riotPlayer) {
        return new RiotPlayerDto(
                RiotAccountDto.of(riotPlayer.getRiotAccount()),
                RiotSummonerDto.of(riotPlayer.getRiotSummoner()),
                RiotChampionMasteryDto.of(riotPlayer.getRiotChampionMasteries()),
                RiotLeagueDto.of(riotPlayer.getRiotLeague()),
                RiotChampionDto.of(riotPlayer.getChampions())
        );
    }

    public static RiotPlayerBasicDto toRiotPlayerBasicDto(RiotPlayer riotPlayer) {
        return new RiotPlayerBasicDto(
                RiotAccountDto.of(riotPlayer.getRiotAccount()),
                RiotSummonerDto.of(riotPlayer.getRiotSummoner()),
                RiotLeagueDto.of(riotPlayer.getRiotLeague())
        );
    }
}
