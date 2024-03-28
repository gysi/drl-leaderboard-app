-- This should be executed manually after deploying the community season feature
-- Since the season already started this here is some manual sql to plan all the tracks
-- for the 2024-01-SPRING Season
DELETE FROM community_seasons;
-- Future Season:
INSERT INTO community_seasons values (6, '2024-04-WINTER', (SELECT id from tracks where name = 'CHRISTMAS AT GRANDPA''S 03 - CHIMNEY SWEEPERS'), null, false);

-- Excluded for ever:
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'DONT SLOW DOWN'), null, true);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SITE SLALOM'), null, true);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'GOATS WARMUP'), null, true);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SPR: ZEPPELIN RIDE'), null, true);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'DA_BITS BEGINNER 1-2 FIG8 GATES'), null, true);

-- Selected tracks spring season 2024:
-- easy
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'DEFIANT FEW FIG-8'), 1, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'OCTOBER HARVEST 01 - PUMPKIN PATCH'), 1, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'RAINBOW ROAD 2 LAPS / BY CMIDZ'), 1, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'FALL SPIRIT - WEEKLY'), 1, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'TWO STACKS (4 LAPS)'), 1, false);

-- Medium
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'VISITOR CENTER: FEILD TRIP'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'TRACK1'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'CM10'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'CTG / 01: CONTAINER CASTLE'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'OUTLET MALL PARKING LOT'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'MIX-UP, FOREST'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'COLOR WAVE // 1 //'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = '2023 MANHATTAN LAKE DRONE RACING'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SHRED ZEPLIN I'), 2, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SWOOPS CASTLEDOME #3'), 2, false);

-- Hard
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SOLS GRID.EXE'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'RIVERSS - WEEKLY'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SOL''S WATERFRONT GP'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SWOOPS UFO'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'JGP-01'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'MAD: BLADE RUNNER'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'COMPLEX CLIMB'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'LEAF FIELD - WEEKLY'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SWOOP HAVE A NICE DAY'), 3, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SAMMY''S WHITE OUT'), 3, false);

-- Very hard
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'MEGA CITY FLOW 2'), 4, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'SWOOPSIES #1'), 4, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = '[FS02] LOVE IS IN THE AIR'), 4, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = '[FS01] HORNET''S HAZARD'), 4, false);
INSERT INTO community_seasons values (3, '2024-01-SPRING', (SELECT id from tracks where name = 'PANDANBERG SESSION'), 4, false);

DELETE FROM leaderboards l
    USING tracks t
        LEFT JOIN community_seasons cs ON cs.track_id = t.id
WHERE t.id = l.track_id
  AND t.map_category_id = 6
  AND cs.track_id IS NULL;
