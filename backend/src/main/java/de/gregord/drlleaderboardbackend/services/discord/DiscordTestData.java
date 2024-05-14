package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.entities.Tournament;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound;

import java.time.LocalDateTime;
import java.util.List;

public class DiscordTestData {
    public static Tournament createTestTournamentForTournamentReminder30Min() {
        Tournament testTournament = new Tournament();
        testTournament.setTitle("Test Tournament");
        testTournament.setRegistrationEndAt(LocalDateTime.now().plusMinutes(30));

        TournamentRound testRound = new TournamentRound();
        testRound.setTitle("Test Round");
        testRound.setNOrder(0);
        testTournament.setRounds(List.of(
                testRound
        ));

        TournamentRound.Match testMatch = new TournamentRound.Match();
        testMatch.setPlayers(
                List.of(
                        createPlayer("Player 1"),
                        createPlayer("Player 2"),
                        createPlayer("Player 3"),
                        createPlayer("Player 4"),
                        createPlayer("Player 5"),
                        createPlayer("Player 6"),
                        createPlayer("Player 7"),
                        createPlayer("Player 8"),
                        createPlayer("Player 9"),
                        createPlayer("Player 10")
                )
        );

        testRound.setMatches(List.of(testMatch));
        return testTournament;
    }

    private static TournamentRound.Player createPlayer(String name){
        TournamentRound.Player player = new TournamentRound.Player();
        player.setProfileName(name);
        return player;
    }

    public static Tournament createTestTournamentFinished() {
        Tournament testTournament = new Tournament();
        testTournament.setTitle("Test Tournament");
        testTournament.setRegistrationEndAt(LocalDateTime.now());
        testTournament.setRankings(List.of(
                createRanking("Player 1", 10000, 1),
                createRanking("Player 2", 10500, 2),
                createRanking("Player 3", 11000, 3),
                createRanking("Player 4", 11500, 0),
                createRanking("Player 5", 12000, 0),
                createRanking("Player 6", 12500, 0),
                createRanking("Player 7", 13000, 0),
                createRanking("Player 8", 13500, 0),
                createRanking("Player 9", 14000, 0),
                createRanking("Player 10", 14500, 0)
        ));

        return testTournament;
    }

    private static TournamentRanking createRanking(String name, long score, int goldenPos){
        TournamentRanking ranking = new TournamentRanking();
        ranking.setProfileName(name);
        ranking.setScore(score);
        ranking.setGoldenPos(goldenPos);
        return ranking;
    }
}
