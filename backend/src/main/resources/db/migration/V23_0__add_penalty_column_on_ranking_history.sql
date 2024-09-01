ALTER TABLE community_seasons_ranking_history ADD COLUMN penalties BIGINT DEFAULT 0;
CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_penalties
    ON community_seasons_ranking_history (penalties);
