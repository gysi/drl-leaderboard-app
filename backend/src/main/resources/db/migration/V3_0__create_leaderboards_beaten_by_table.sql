create table if not exists leaderboards_beaten_by
(
    leaderboard_id           bigint not null
        constraint c_leaderboards_beaten_by_leaderboard_id_fk
            references leaderboards
            on delete cascade,
    beaten_by_leaderboard_id bigint not null
        constraint c_leaderboards_beaten_by_beaten_by_leaderboard_id_fk
            references leaderboards
            on delete cascade
);

