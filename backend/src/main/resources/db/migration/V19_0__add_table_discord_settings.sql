create table if not exists discord_server_settings (
   id bigint not null primary key,
   server_id bigint not null,
   setting varchar(255) not null,
   value varchar(255)
);
