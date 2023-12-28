create table if not exists discord_servers (
    id bigint not null primary key,
    server_id varchar(255) not null,
    server_name varchar(255) not null
);

create table if not exists discord_active_channels (
    id bigint not null primary key,
    server_id bigint not null,
    channel_id varchar(255) not null,
    channel_name varchar(255) not null,
    post_type varchar(32) not null,
    last_post_at timestamp(6),
    UNIQUE (server_id, channel_id)
);
