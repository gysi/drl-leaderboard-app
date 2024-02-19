CREATE TABLE players (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id TEXT constraint c_players_player_id_unique UNIQUE,
    player_name TEXT,
    profile_thumb TEXT,
    profile_platform TEXT,
    profile_platform_id TEXT,
    flag_url TEXT,
    created_at TIMESTAMP(3),
    updated_at TIMESTAMP(3)
);

CREATE INDEX IF NOT EXISTS i_players_player_id ON players (player_id);
CREATE INDEX IF NOT EXISTS i_players_profile_platform ON players (profile_platform);
CREATE INDEX IF NOT EXISTS i_players_profile_platform_id ON players (profile_platform_id);
CREATE INDEX IF NOT EXISTS i_players_created_at ON players (created_at);
CREATE INDEX IF NOT EXISTS i_players_updated_at ON players (updated_at);
