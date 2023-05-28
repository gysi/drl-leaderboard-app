create table if not exists tournaments
(
    id                  bigint not null
        primary key,
    drl_id          varchar(255),
    guid                varchar(255)
        constraint c_tournaments_guid_unique
            unique,
    title               varchar(255),
    description         varchar(255),
    image_url           varchar(255),
    map                 varchar(255),
    custom_map          varchar(255),
    custom_map_title    varchar(255),
    track_id            varchar(255),
    status              varchar(255),
    region              varchar(255),
    is_private          boolean,
    is_test_tournament  boolean,
    players_per_match   integer,
    winners_per_match   integer,
    heats_per_match     integer,
    player_ids          jsonb,
    rankings            jsonb,
    matches             jsonb,
    created_at          timestamp(6),
    updated_at          timestamp(6),
    registration_start_at timestamp(6),
    registration_end_at  timestamp(6),
    next_turn_at        timestamp(6),
    completed_at        timestamp(6)
);

create index if not exists i_tournaments_guid on tournaments (guid);
create index if not exists i_tournaments_drl_id on tournaments (drl_id);
create index if not exists i_tournaments_title on tournaments (title);
create index if not exists i_tournaments_map on tournaments (map);
create index if not exists i_tournaments_custom_map on tournaments (custom_map);
create index if not exists i_tournaments_custom_map_title on tournaments (custom_map_title);
create index if not exists i_tournaments_player_ids on tournaments using gin (player_ids);
create index if not exists i_tournaments_rankings on tournaments using gin (rankings);
create index if not exists i_tournaments_matches on tournaments using gin (matches);
create index if not exists i_tournaments_created_at on tournaments (created_at);
create index if not exists i_tournaments_updated_at on tournaments (updated_at);
create index if not exists i_tournaments_registration_start_at on tournaments (registration_start_at);
create index if not exists i_tournaments_registration_end_at on tournaments (registration_end_at);
create index if not exists i_tournaments_next_turn_at on tournaments (next_turn_at);
create index if not exists i_tournaments_completed_at on tournaments (completed_at);

