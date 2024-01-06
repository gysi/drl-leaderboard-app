ALTER TABLE discord_servers
    ADD COLUMN bot_type VARCHAR(30) DEFAULT 'LEADERBOARD';

CREATE UNIQUE INDEX discord_servers_id_bottype_uindex
    ON discord_servers (id, bot_type);
