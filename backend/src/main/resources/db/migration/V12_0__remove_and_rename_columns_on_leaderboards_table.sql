ALTER TABLE leaderboards
    RENAME COLUMN player_id TO player_id_drl;

ALTER TABLE leaderboards
    DROP COLUMN player_name,
    DROP COLUMN profile_thumb,
    DROP COLUMN profile_platform,
    DROP COLUMN profile_platform_id,
    DROP COLUMN flag_url;

ALTER TABLE leaderboards
    ADD COLUMN player_id BIGINT;

CREATE INDEX IF NOT EXISTS i_leaderboards_player_id ON leaderboards (player_id);

UPDATE leaderboards
SET player_id = players.id
FROM players
WHERE players.player_id = leaderboards.player_id_drl;
