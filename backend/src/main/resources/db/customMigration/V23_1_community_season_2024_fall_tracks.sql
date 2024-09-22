-- This should be executed manually after deploying the community season feature
-- Since the season already started this here is some manual sql to plan all the tracks
-- for the 2024-01-FALL Season

-- Selected tracks fall season 2024:
-- easy (blue series)
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name = '[FALL24-01] CONTAIN YOURSELF'), 0, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name = '[FALL24-02] ARTILLERY STATION'), 1, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name = '[FALL24-03] SILO SPEEDWAY'), 1, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-04]%'), 1, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-05]%'), 1, false);

-- green series
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-06]%'), 1, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-07]%'), 1, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-08]%'), 1, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-09]%'), 1, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-10]%'), 1, false);

-- yellow series
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-11]%'), 2, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-12]%'), 2, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-13]%'), 2, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-14]%'), 2, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-15]%'), 2, false);

-- orange series
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-16]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-17]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-18]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-19]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-20]%'), 3, false);

-- Red series
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-21]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-22]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-23]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-24]%'), 3, false);
INSERT INTO community_seasons values (5, '2024-03-FALL', (SELECT id from tracks where name like '[FALL24-25]%'), 3, false);
