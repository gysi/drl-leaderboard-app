ALTER TABLE discord_active_channels
    DROP CONSTRAINT discord_active_channels_server_id_channel_id_key;

ALTER TABLE discord_active_channels
    ADD CONSTRAINT discord_active_channels_server_id_channel_id_post_type_key
        UNIQUE (server_id, channel_id, post_type);
