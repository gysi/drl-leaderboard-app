package de.gregord.drlleaderboardbackend.domain.convert;

import com.google.protobuf.Timestamp;
import de.gregord.drlleaderboardbackend.domain.TournamentRankings;
import de.gregord.drlleaderboardbackend.domain.TournamentRankingsOuterClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TournamentRankingsToProto {
    // TODO LOOK AT THIS LIBRARY!
    // https://gitlab.com/protobuf-tools/proto_domain_converter#getting-the-library
    public static TournamentRankingsOuterClass.TournamentRankings convertDomainToProto(TournamentRankings tournamentRankings) {
        // Convert TournamentRankings object to TournamentRankingsOuterClass
        TournamentRankingsOuterClass.TournamentRankings.Builder builder = TournamentRankingsOuterClass.TournamentRankings.newBuilder();

        // Convert each field
        builder.setSeasonName(tournamentRankings.getSeasonName());
        // For the LocalDateTime fields, you would use your conversion method to go from LocalDateTime to Protobuf's Timestamp
        builder.setSeasonStartDate(convertToProtoTimestamp(tournamentRankings.getSeasonStartDate()));
        builder.setSeasonEndDate(convertToProtoTimestamp(tournamentRankings.getSeasonEndDate()));

        // For the List<PlayerRanking>, you need to loop over the list and convert each PlayerRanking to PlayerRankingProtos.PlayerRanking
        for (TournamentRankings.PlayerRanking playerRanking : tournamentRankings.getRankings()) {
            TournamentRankingsOuterClass.TournamentRankings.PlayerRanking.Builder playerRankingBuilder = TournamentRankingsOuterClass.TournamentRankings.PlayerRanking.newBuilder();

            playerRankingBuilder.setCommonPlayerName(playerRanking.getCommonPlayerName());
            playerRankingBuilder.setProfileThumb(playerRanking.getProfileThumb());
            playerRankingBuilder.setFlagUrl(playerRanking.getFlagUrl());
            playerRankingBuilder.setPlatform(playerRanking.getPlatform());
            playerRankingBuilder.setNumberOfTournamentsPlayed(playerRanking.getNumberOfTournamentsPlayed());
            playerRankingBuilder.setNumberOfGoldenHeats(playerRanking.getNumberOfGoldenHeats());
            playerRankingBuilder.setTotalPoints(playerRanking.getTotalPoints());
            playerRankingBuilder.setPosition(playerRanking.getPosition());
            playerRankingBuilder.setPointsBest12Tournaments(playerRanking.getPointsBest12Tournaments());
            playerRanking.getBest12Positions().forEach(playerRankingBuilder::addBest12Positions);

            for (TournamentRankings.Tournament tournament : playerRanking.getPlayedTournaments()) {
                TournamentRankingsOuterClass.TournamentRankings.Tournament.Builder tournamentBuilder = TournamentRankingsOuterClass.TournamentRankings.Tournament.newBuilder();

                tournamentBuilder.setTitle(tournament.getTitle());
                tournamentBuilder.setStartDate(convertToProtoTimestamp(tournament.getStartDate()));
                tournamentBuilder.setPosition(tournament.getPosition());
                tournamentBuilder.setPoints(tournament.getPoints());
                tournamentBuilder.setNameUsedInGame(tournament.getNameUsedInGame());

                playerRankingBuilder.addPlayedTournaments(tournamentBuilder.build());
            }

            // Add to the list in TournamentRankings
            builder.addRankings(playerRankingBuilder.build());
        }

        return builder.build();
    }

    public static Timestamp convertToProtoTimestamp(LocalDateTime localDateTime) {
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
