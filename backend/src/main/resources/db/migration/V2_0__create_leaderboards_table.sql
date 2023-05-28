create table if not exists leaderboards
(
    id                  bigint not null
        primary key,
    crash_count         integer,
    created_at          timestamp(6),
    drone_name          varchar(255),
    flag_url            varchar(255),
    invalid_run_reason  varchar(255),
    is_invalid_run      boolean,
    player_id           varchar(255),
    player_name         varchar(255),
    points              double precision,
    position            bigint,
    profile_platform    varchar(255),
    profile_platform_id varchar(255),
    profile_thumb       varchar(255),
    replay_url          varchar(255),
    score               bigint,
    top_speed           double precision,
    updated_at          timestamp(6),
    track_id            bigint not null
        constraint c_leaderboards_track_id_fk
            references tracks
            on delete cascade,
    constraint c_leaderboards_track_id_player_id_unique
        unique (track_id, player_id)
);

alter table leaderboards
    owner to admin;

create index if not exists idx5vbspgdo98aau1g4lrr05992s
    on leaderboards (track_id);

create index if not exists idxo4wbmd7e523wjvm3lshh54w7u
    on leaderboards (player_id);

create index if not exists idxph9w4fr2iegcpyxi9hkpebgw0
    on leaderboards (player_name);

create index if not exists idx1u6lgs1sgbs8qem1icmda3wv2
    on leaderboards (score);

create index if not exists idx5mrdy3mefxauehjnu55vwlaig
    on leaderboards (position);

create index if not exists idxn5x7jb5kn8adq9oo8r2beq837
    on leaderboards (created_at);

create index if not exists idx1xawo2jmq5cvc37fxs3k51w0d
    on leaderboards (updated_at);

create index if not exists idxmrxag6tqa3p13ivkcfdwxg598
    on leaderboards (points);

create index if not exists idxh6tlpuduv5a7bof4hd4gdsa2g
    on leaderboards (is_invalid_run);

