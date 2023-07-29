ALTER TABLE leaderboards
    ADD COLUMN drl_id TEXT;

CREATE INDEX IF NOT EXISTS i_leaderboards_drl_id ON leaderboards (drl_id);
