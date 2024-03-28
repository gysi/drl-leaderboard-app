CREATE TABLE IF NOT EXISTS community_seasons(
  season_id INT NOT NULL,
  season_id_name TEXT NOT NULL,
  track_id BIGINT NOT NULL,
  custom_track_difficulty INTEGER,
  excluded BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (season_id, track_id)
);

CREATE INDEX IF NOT EXISTS i_community_seasons_season_id
    ON community_seasons (season_id);

CREATE INDEX IF NOT EXISTS i_community_seasons_season_id_name
    ON community_seasons (season_id_name);

CREATE INDEX IF NOT EXISTS i_community_seasons_track_id
    ON community_seasons (track_id);

CREATE INDEX IF NOT EXISTS i_community_seasons_excluded
    ON community_seasons (excluded);
