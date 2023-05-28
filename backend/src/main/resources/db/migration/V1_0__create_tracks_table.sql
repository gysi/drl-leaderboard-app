create table if not exists tracks
(
    id              bigint not null
        primary key,
    categories      varchar(255),
    created_at      timestamp(6),
    drl_track_id    varchar(255),
    guid            varchar(255)
        constraint c_tracks_guid_unique
            unique,
    is_drl_official boolean,
    is_public       boolean,
    map_category    varchar(255),
    map_difficulty  integer,
    map_distance    double precision,
    map_id          varchar(255),
    map_laps        integer,
    map_mode_type   varchar(255),
    map_name        varchar(255),
    name            varchar(255),
    parent_category varchar(255),
    updated_at      timestamp(6)
);

create index i_tracks_map_id
    on tracks (map_id);

create index i_tracks_map_name
    on tracks (map_name);

create index i_tracks_parent_category
    on tracks (parent_category);

create index i_tracks_name
    on tracks (name);
