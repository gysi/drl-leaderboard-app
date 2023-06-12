create table if not exists tracks
(
    id              bigint not null
        primary key,
    categories      text,
    created_at      timestamp(6),
    drl_track_id    text,
    guid            text
        constraint c_tracks_guid_unique
            unique,
    is_drl_official boolean,
    is_public       boolean,
    map_category    text,
    map_difficulty  integer,
    map_distance    double precision,
    map_id          text,
    map_laps        integer,
    map_mode_type   text,
    map_name        text,
    name            text,
    parent_category text,
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
