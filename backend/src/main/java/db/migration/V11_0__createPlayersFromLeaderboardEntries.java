package db.migration;

import de.gregord.drlleaderboardbackend.util.TsidUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class V11_0__createPlayersFromLeaderboardEntries extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try {
            try(Statement statement = context.getConnection().createStatement()){
                // select unique players from leaderboard table
                ResultSet resultSet = statement.executeQuery("SELECT DISTINCT ON (player_id) player_id, player_name, profile_thumb, flag_url, profile_platform, profile_platform_id FROM leaderboards order by player_id, created_at");
                while(resultSet.next()){
                    // insert player into player table if not exists
                    String insertSql = "INSERT INTO players (id, player_id, player_name, profile_thumb, flag_url, profile_platform, profile_platform_id, created_at, updated_at) " +
                            "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM players WHERE player_id=?)";
                    try(PreparedStatement preparedStatement = context.getConnection().prepareStatement(insertSql)){
                        preparedStatement.setLong(1, TsidUtil.TSID_FACTORY.create().toLong());
                        preparedStatement.setString(2, resultSet.getString("player_id"));
                        preparedStatement.setString(3, resultSet.getString("player_name"));
                        preparedStatement.setString(4, resultSet.getString("profile_thumb"));
                        preparedStatement.setString(5, resultSet.getString("flag_url"));
                        preparedStatement.setString(6, resultSet.getString("profile_platform"));
                        preparedStatement.setString(7, resultSet.getString("profile_platform_id"));
                        // Set created_at and updated_at to a very early date
                        preparedStatement.setTimestamp(8, Timestamp.valueOf("1970-01-01 00:00:00"));
                        preparedStatement.setTimestamp(9, Timestamp.valueOf("1970-01-01 00:00:00"));
                        preparedStatement.setString(10, resultSet.getString("player_id"));
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Migration failed", e);
        }
    }
}
