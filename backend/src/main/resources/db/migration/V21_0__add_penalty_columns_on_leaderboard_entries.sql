alter table leaderboards add column is_replay_analyzed boolean not null default false;
create index if not exists i_leaderboards_is_replay_analysed on leaderboards (is_replay_analyzed);

alter table leaderboards add column time_penalty_total integer default null;
create index if not exists i_leaderboards_time_penalty on leaderboards (time_penalty_total);

alter table leaderboards add column penalties jsonb default null;
create index if not exists i_leaderboards_pentalties on leaderboards using gin (penalties);
