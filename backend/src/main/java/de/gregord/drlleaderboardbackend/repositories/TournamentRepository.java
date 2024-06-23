package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.TournamentView;
import de.gregord.drlleaderboardbackend.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findTournamentByDrlId(String drlId);

    @Query("""
        SELECT t FROM Tournament t
            WHERE t.isTestTournament = false AND t.isPrivate = false AND t.status = 'complete'
                AND t.registrationEndAt >= :lowerBound AND t.registrationEndAt < :upperBound
            ORDER BY t.registrationEndAt ASC
    """)
    Stream<Tournament> streamTournaments(LocalDateTime lowerBound, LocalDateTime upperBound);

    @Query(value = """
    SELECT
        t.guid as guid,
        t.title as title,
        CASE WHEN t.registration_end_at <= CURRENT_TIMESTAMP THEN t.custom_map_title ELSE NULL END as trackName,
        t.image_url as imgUrl,
        (
            SELECT jsonb_agg(jsonb_build_object(
                    'profileName', item.value->>'profileName',
                    'goldenPos', (item.value->>'goldenPos')::int,
                    'score', (item.value->>'score')::int
                             ))
            FROM jsonb_array_elements(t.rankings) WITH ORDINALITY AS item
            WHERE item.ordinality <= 10
        ) AS top10,
        t.registration_end_At as startDate,
        t.status as status
     FROM tournaments t
     WHERE t.is_test_tournament = false AND t.is_private = false
         AND t.registration_end_at >= :lowerBound AND t.registration_end_at < :upperBound
         AND t.status != 'canceled'
     ORDER BY t.registration_end_at DESC
    """, nativeQuery = true)
    List<TournamentView> _getTournament(LocalDateTime lowerBound, LocalDateTime upperBound);
}
