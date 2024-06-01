-- This should be executed manually after deploying the community season feature
-- Since the season already started this here is some manual sql to plan all the tracks
-- for the 2024-01-SUMMER Season

-- Selected tracks spring season 2024:
-- easy (blue series)
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-01] HUMBLE BEGINNINGS'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-02] MODERN JUNGLE'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-03] PENCIL PUSHERS'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-04] SAND-IT!'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-05] APOLLO''S PATH'), 1, false);
