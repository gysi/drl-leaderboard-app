ALTER TABLE tracks
    ADD COLUMN IF NOT EXISTS map_category_id INTEGER DEFAULT null, -- from enum in MapCategory.java
    ADD COLUMN IF NOT EXISTS rating_score DOUBLE PRECISION DEFAULT null,
    ADD COLUMN IF NOT EXISTS rating_count INTEGER DEFAULT null,
    ADD COLUMN IF NOT EXISTS map_thumb TEXT DEFAULT null,
    ADD COLUMN IF NOT EXISTS track_creator TEXT DEFAULT null;

ALTER TABLE tracks
    DROP COLUMN categories,
    DROP COLUMN is_drl_official,
    DROP COLUMN is_public,
    DROP COLUMN map_mode_type;

-- Set map_category_id for existing entries according to the ordinal of the new MapCategory Class.
UPDATE tracks
SET map_category_id =
    CASE
        WHEN parent_category = 'DRL MAPS' THEN 0
        WHEN parent_category = 'VIRTUAL SEASON' THEN 1
        WHEN parent_category = 'MULTIGP' THEN 2
        WHEN parent_category = 'DRL SIM RACING CUP' THEN 3
        WHEN parent_category = 'FEATURED TRACKS' THEN 4
        WHEN parent_category = 'ORIGINALS' THEN 5
        ELSE null
END;

CREATE INDEX IF NOT EXISTS i_tracks_map_category_id
    ON tracks (map_category_id);

CREATE INDEX IF NOT EXISTS i_tracks_rating_score
    ON tracks (rating_score);

CREATE INDEX IF NOT EXISTS i_tracks_rating_count
    ON tracks (rating_count);

CREATE INDEX IF NOT EXISTS i_tracks_track_creator
    ON tracks (track_creator);

UPDATE tracks SET drl_track_id = null;
