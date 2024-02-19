package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static de.gregord.drlleaderboardbackend.domain.PointsCalculation.calculatePointsByPositionV2;

public class V13_0__modifyPointsAccordingToNewPointsSystem extends BaseJavaMigration {
    public static Logger LOG = LoggerFactory.getLogger(V13_0__modifyPointsAccordingToNewPointsSystem.class);
    @Override
    public void migrate(Context context) throws Exception {

        Connection connection = context.getConnection();
        int i = 1;

        try (Statement stmt = connection.createStatement()) {

            // Your existing code starts here
            ResultSet tracks = stmt.executeQuery("SELECT id FROM tracks");

            while (tracks.next()) {

                Long trackId = tracks.getLong("id");

                try (PreparedStatement bestScoreStmt = connection.prepareStatement(
                        "SELECT score FROM leaderboards WHERE track_id = ? AND is_invalid_run = false ORDER BY score ASC LIMIT 1")) {
                    bestScoreStmt.setLong(1, trackId);

                    ResultSet bestScoreResult = bestScoreStmt.executeQuery();

                    if (bestScoreResult.next()) {
                        Long firstPositionMS = bestScoreResult.getLong("score");

                        try (PreparedStatement scoresStmt = connection.prepareStatement(
                                "SELECT id, score FROM leaderboards WHERE track_id = ?");
                             PreparedStatement updateStmt = connection.prepareStatement(
                                     "UPDATE leaderboards SET points = ? WHERE id = ?")) {
                            scoresStmt.setLong(1, trackId);

                            ResultSet scores = scoresStmt.executeQuery();

                            while (scores.next()) {
                                Long positionMS = scores.getLong("score");
                                Long scoreId = scores.getLong("id");

                                Double newPoints = calculatePointsByPositionV2(firstPositionMS, positionMS);

                                updateStmt.setDouble(1, newPoints);
                                updateStmt.setLong(2, scoreId);

                                updateStmt.addBatch();
                            }

                            updateStmt.executeBatch();
                            LOG.info("After Batch {}", i);
                            i++;
                        }
                    }
                }
            }
        } catch (Exception e){
            LOG.error("ERROR", e);
        }
    }
}
