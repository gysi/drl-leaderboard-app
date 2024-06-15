-- This should be executed manually after deploying the community season feature
-- Since the season already started this here is some manual sql to plan all the tracks
-- for the 2024-01-SUMMER Season

-- Selected tracks spring season 2024:
-- easy (blue series)
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-01] HUMBLE BEGINNINGS'), 0, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-02] MODERN JUNGLE'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-03] PENCIL PUSHERS'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-04] SAND-IT!'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-05] APOLLO''S PATH'), 1, false);

-- green series
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-06] GREEN FLAG'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-07] PAPERJAM'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-08] CASTLE CRUISE'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-09] RIPPIN'' THROUGH THE RUINS'), 1, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-10] TRIPP LITE'), 1, false);

-- yellow series
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-11] DRL HEADQUARTERS'), 2, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-12] KING''S COURT'), 2, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-13] SANDSTORM'), 2, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-14] SHADOWS AND DUST'), 2, false);
INSERT INTO community_seasons values (4, '2024-02-SUMMER', (SELECT id from tracks where name = '[SUM24-15] COOL BREEZE'), 2, false);
