-- This is curently used for keeping track of the previous position when a player gets a new postion
-- Mainly this is used for the bug when drl didn't upload a replay just yet and then later adds it
-- This way we can still get the previous position if there was any.
ALTER TABLE leaderboards
    ADD COLUMN previous_position bigint,
    ADD COLUMN previous_score bigint;
