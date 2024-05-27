create table community_seasons_ranking_history
(
    id               bigint not null primary key,
    season_id        bigint not null,
    player_id        bigint not null,
    points           double precision,
    position         bigint,
    position_average double precision,
    position_best    bigint,
    invalid_runs     bigint,
    completed_tracks bigint,
    crashes          bigint,
    total_time       bigint,
    top_speed        double precision
);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_season_id
    ON community_seasons_ranking_history (season_id);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_player_id
    ON community_seasons_ranking_history (player_id);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_points
    ON community_seasons_ranking_history (points);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_position
    ON community_seasons_ranking_history (position);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_position_average
    ON community_seasons_ranking_history (position_average);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_position_best
    ON community_seasons_ranking_history (position_best);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_invalid_runs
    ON community_seasons_ranking_history (invalid_runs);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_completed_tracks
    ON community_seasons_ranking_history (completed_tracks);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_crashes
    ON community_seasons_ranking_history (crashes);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_total_time
    ON community_seasons_ranking_history (total_time);

CREATE INDEX IF NOT EXISTS i_community_seasons_ranking_history_top_speed
    ON community_seasons_ranking_history (top_speed);


