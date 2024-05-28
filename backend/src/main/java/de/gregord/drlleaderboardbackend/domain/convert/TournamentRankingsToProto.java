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
        TournamentRankingsOuterClass.TournamentRankings.Builder builder = TournamentRankingsOuterClass.TournamentRankings.newBuilder();
        builder.setSeasonName(tournamentRankings.getSeasonName());
        builder.setSeasonStartDate(convertToProtoTimestamp(tournamentRankings.getSeasonStartDate()));
        builder.setSeasonEndDate(convertToProtoTimestamp(tournamentRankings.getSeasonEndDate()));
        for (TournamentRankings.PlayerRanking playerRanking : tournamentRankings.getRankings()) {
            TournamentRankingsOuterClass.TournamentRankings.PlayerRanking.Builder playerRankingBuilder = TournamentRankingsOuterClass.TournamentRankings.PlayerRanking.newBuilder();

            if(playerRanking.getPlayerId() == null) {
                playerRankingBuilder.clearPlayerId();
            } else {
                playerRankingBuilder.setPlayerId(playerRanking.getPlayerId());
            }
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
            playerRanking.getAllPositions().forEach(playerRankingBuilder::addAllPositions);

            for (TournamentRankings.Tournament tournament : playerRanking.getPlayedTournaments()) {
                TournamentRankingsOuterClass.TournamentRankings.Tournament.Builder tournamentBuilder = TournamentRankingsOuterClass.TournamentRankings.Tournament.newBuilder();

                tournamentBuilder.setTitle(tournament.getTitle());
                tournamentBuilder.setStartDate(convertToProtoTimestamp(tournament.getStartDate()));
                tournamentBuilder.setPosition(tournament.getPosition());
                tournamentBuilder.setPoints(tournament.getPoints());
                tournamentBuilder.setNameUsedInGame(tournament.getNameUsedInGame());

                playerRankingBuilder.addPlayedTournaments(tournamentBuilder.build());
            }
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
