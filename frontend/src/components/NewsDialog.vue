<template>
    <q-dialog v-model="value" transition-show="scale" transition-hide="scale">
      <q-card bordered>
        <q-banner inline-actions class="text-white bg-red">
          <div><b>General Info:</b></div>
          <div> Your position within the game for individual tracks can be different from the position shown here.</div>
          <q-btn type="a" flat padding="0"
            :to="{ name: 'faq', query: { card: 'invalidRuns' } }"
          >Read more in the FAQ</q-btn>
          <q-btn dense flat icon="close" v-close-popup style="position: absolute; top: 0; right: 0">
            <q-tooltip class="bg-white text-primary">Close</q-tooltip>
          </q-btn>
        </q-banner>
        <q-separator />
        <q-card style="max-height: 40rem" class="scroll">
<!-- 2.8.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.8.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-12-28 01:30') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>There is now a discord bot that posts new best times within the top 50 to a discord server.
                  You can add it to your server by calling the following URL in your browser:
                  <br/>
                  <a href="https://discord.com/api/oauth2/authorize?client_id=1189335274422009978&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
                  <br/>Commands:
                  <ul><li>/drl-leaderboard-posts-activate</li><li>/drl-leaderboard-posts-deactivate</li></ul>
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.7.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.7.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-12-12 23:50') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>The tournaments list now shows top10 players for each tournament instead of top3.
                </li>
                <li>The tournaments ranking table is now prettier.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.6.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.6.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-07-29 04:12') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds new invalid run reason "WRONG_DRONE".
                </li>
                <li>Adds ability to manually flag runs as invalid. If you notice a run that is invalid, please contact "gysi" on Discord.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.6.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.6.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-25 01:30') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds a progressbar to the tournament page to show the season progress.
                </li>
              </ul>
              <div class="text-h6">Misc</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Moves tournament point system explanation to the FAQ.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.5.2 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.5.2 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-24 01:08') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Record all Tournament participants and entries, instead of just the top 11.
                </li>
              </ul>
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Fixes height of tournamentpage and shows scrollbar instead.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.5.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.5.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-23 12:10') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Shows "Time Set" correctly when PB is older than a year
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.5.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.5.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-18 23:50') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>(Preview) Prettier tournament cards that show the status and "Starts in" time of the tournament.
                </li>
              </ul>
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Ordering of tournament ranking is now correctly based upon the best 12 tournaments
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.4.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.4.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-12 02:45') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>(Preview) Now you will see the tournaments (including upcoming) for the current season on the tournaments page.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.3.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.3.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-03 02:30') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>(Preview) The Tournaments page is now available. You can see the current tournament rankings.<br/>
                  Each tournament lasts 3 months and the top 12 tournaments for each player are counted.<br/>
                  There are four seasons per year: Spring, Summer, Fall and Winter.<br/>
                  Spring: March 1st - May 31th<br/>
                  Summer: June 1st - August 31th<br/>
                  Fall: September 1st - November 30th<br/>
                  Winter: December 1st - February 28th (or February 29th in a leap year)<br/>
                  Tournament points are based on the DRL irl point system.<br/>
                  The current season (Summer) started on 2023-05-01 instead of normally 2023-06-01, because otherwise
                  there would only be 3 recorded tournaments for the Fall season, as the crawling of tournaments only started
                  a few weeks ago.<br/>
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.2.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.2.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-05-24 02:00') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>DEVILS asked for it and he got it. (Easter Egg)
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.1.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.1.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-05-23 01:20') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds new SILICON VALLEY map and its tracks
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 2.0.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">2.0.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-05-01 02:40') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Create and Customize stream cards, that can be used as an overlay within your stream.
                  The data within the stream cards are automatically updated every 10 seconds, so you don't need to refresh the page.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.9.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.9.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-16 23:53') }}</div>
            </q-card-section>
            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Misc</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Changes Hashrouting (#) to Historyrouting (no #) for better SEO. If you have bookmarked a page, you have to update the link.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.9.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.9.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-16 03:00') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features & Bugfixes (Replay Viewer)</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Improved performance of the replay viewer
                </li>
                <li>If you select a new track, the old track is now removed from the replay viewer
                </li>
                <li>Lines are now thicker and have a depth due to added lightning
                </li>
                <li>If you select 2nd, 3rd.. player, the race line has a different color
                </li>
                <li>Checkpoint times are now in the correct format and don't overlap anymore (except when two checkpoints overlap)
                </li>
                <li>When a lap ends the checkpoint time has the label 'Lap' after the time.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.8.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.8.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-13 02:05') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Correctly crawl entries up to position 100 and display them all on the track leaderboard
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.8.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.8.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-12 02:40') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Very very early preview of the Replay Viewer, not all features are implemented yet.<br/>
                  It has some bugs, but it's a start
                </li>
              </ul>
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Fixes loading bar on selectors not being visible
                </li>
              </ul>
              <div class="text-h6">Misc</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Removes some unpretty padding for the navbar items
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.7.3 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.7.3 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-03-23 21:12') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds a new invalid time category "EPIC_BUG", read more in the FAQ
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.7.2 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.7.2 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-03-06 00:25') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Stops setting BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT or BETTER_ENTRY_WITH_SAME_NAME on player run if the entry it references is invalid, for example because of NO_REPLAY
                </li>
                <li>Comparing entries didn't match the actual runs. This happened when the compared entries contained multiple entries for the same track (this can happen when there are multiple entries for the same player for a track)
                </li>
                <li>Comparing entries wouldn't match if the player had multiple entries for the same track
                </li>
              </ul>
              <div class="text-h6">Misc</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Stops showing comparing times for invalid runs
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.7.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.7.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-03-05 00:12') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Grammar corrections on the FAQ page
                </li>
                <li>Adds a plus sign to the positive times when comparing them to other players on the player's lb page
                </li>
                <li>Consistent use of time difference signs in the beaten by table
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.7.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.7.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-23 01:10') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>"Overall Rankings" has been renamed to Rankings. It now contains a dropdown for optional selection of the
                  map category which will then display the rankings for that category.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.6.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.6.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-13 00:30') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>There is now a "News" section in the navbar which, when clicked, opens a dialogue showing all the releases.
                  It also has a red info box at the top to let players know why their position on this site may not match their
                  position on the game leaderboards. This is to avoid confusion when using the site for the first time.
                  This dialogue will always be displayed the first time the site is opened. After that, it is only displayed
                  when the "News" button in the navbar is clicked, or when a new version is published.
                </li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.5.3 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.5.3 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-07 21:07') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Fixes the "18:" infront of leaderboard times when being on a timezone different that UTC+1</li>
              </ul>
              <div class="text-h6">Optimizations</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Expire most backend caches after 1 minute and not just after a full recrawl of the DRL api</li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.5.2 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.5.2 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-02 23:07') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Performance</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Performance optimization for track lb page</li>
              </ul>
            </q-card-section>
          </q-card>
          <q-separator />
<!-- 1.5.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.5.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-02 03:26') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds two new statistics to the home page. "Most entries by track last 14 days" and "Most entries by track last month"</li>
                <li>Adds links on each statistics container for player and tracks</li>
                <li>Adds Map background animation on track leaderboard table header</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 1.4.2 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.4.2 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-01 11:10') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>NGINX now includes the charset in the content-type header of the response,
                  this helps with some systems to correctly display emojis (e.g. discord url embeds)</li>
                <li>Sets an additional meta tag for encoding too</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 1.4.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.4.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-01 01:55') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Also removes the 0 in front of time differences when comparing against other players when its positive</li>
                <li>Changes label of compare player select</li>
                <li>Fixes bug when comparing against others and then selecting another player</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 1.4.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.4.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-01 01:06') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds ability to compare against other player times within the player ranking section</li>
              </ul>
              <div class="text-h6">Optimizations</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Show all tracks on the first page in the tracks section</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 1.3.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.3.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-30 02:04') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds playerSearch to overall rankings which lets you scroll to the player and highlight the row</li>
              </ul>
              <div class="text-h6">Performance</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds placerholder.png to assets and don't fetch it from the drl api</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 1.2.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.2.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-29 24:00') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>You can click on the content card of the slotmachine</li>
                <li>There has been a button added to the slotmachine page to select the different "worst" categories you want to have within the track pool</li>
                <li>All settings you make on the slotmachine page are saved so that they are still there on page reload</li>
              </ul>
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Fixes some logic bugs where you could play the slotmachine without having tracks loaded</li>
              </ul>
              <div class="text-h6">Improvements</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Slotmachine content cards are now fancy</li>
                <li>Some general styling improvements regarding the slotmachine</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 1.0.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">1.0.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-29 05:14') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds a new page to the nav. Its a random map picker. It picks the worst maps for the selected player using a fancy slotmachine.</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.10.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.10.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-22 18:26') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds average position to overall rankings</li>
                <li>Question icons on columns which lead to explanations within the faq page (also links warning icon for invalid runs).
                  The section in the FAQ page will automatically expand</li>
                <li>Beaten by column is now sortable!</li>
                <li>Beaten by column has now an entry within the FAQ page explaining it</li>
              </ul>
              <div class="text-h6">Improvements</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Changes wording on beaten by overlay</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.9.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.9.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-22 03:43') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>The tracks page has now images for each map</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.8.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.8.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-21 04:09') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">DARKMODE ACTIVATED!!!</div>
            </q-card-section>
          </q-card>
<!-- 0.7.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.7.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-19 24:10') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Sort by position and then by score for the track leaderboard</li>
                <li>Correctly sets is-custom-map parameter on leaderboard requests depending on
                  name which before resulted in invalid times showing up on the leaderboard</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.7.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.7.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 19:13') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Enables sorting for the player leaderboard</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.6.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.6.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 17:01') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Advanced search highlighting within track leaderboards</li>
              </ul>
              <div class="text-h6">Performance</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds caches for statistics which are requested on the 'home' page</li>
                <li>The ajax calls on the homepage are now executed in parallel</li>
                <li>Limits mostPbsLast7Days/Month to 10 because the frontend only shows 10 entries</li>
              </ul>
              <div class="text-h6">Misc</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds default "catch all" nginx server block which avoids mapping of the ip address
                  to the first server block. Adds selfsigned ssl certificate for this, to catch ssl requests too</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.5.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.5.0 Release (PINK IS THE DEVIL)</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 02:11') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>The main theme of the page is now pink (If you don't like it then send your complaint to Devils)</li>
              </ul>
              <div class="text-h6">Improvements</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Better styling separation. One step closer to an easy configurable theme.
                  Darkmode is a little bit tricky though. For now darkmode is disabled</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.4.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.4.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-16 02:22') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Adds Time and Time Diff to beaten by table (Onhover table in the player leaderboard)</li>
                <li>Restyling of FAQ Page and adds FAQ card for "Invalid Runs"</li>
              </ul>
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Sets burst size of the rate limit to 45, because otherwise if you never saw the site
                  and have nothing in your cache, you could get a 429 status code.</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.3.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.3.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-15 19:01') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Introduces a new column on the player leaderboard that displays if player personal bests
                  have recently been surpassed or are currently performing well since they were submitted.</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.2.3 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.2.3 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-15 03:26') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Enables cascade on delete for foreignkeys inside the DB (DB Structure needs to be recreated)</li>
                <li>Fixes bug in frontend which would throw an error and not render track entries if mapName or parentCategory is null</li>
                <li>Adjusts to 64 id values inside frontend because javascript cannot handle 64bit integers so we use a string</li>
                <li>Correctly uses docker_compose.env on the backend service and not on the postgres service..</li>
              </ul>
              <div class="text-h6">Performance</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Optimizes crawling / saving of objects from the API</li>
                <li>Switches to TSIDs instead of sequence generated ids so that batch updates can be used</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.2.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.2.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-14 03:39') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Restyling of track column on player leaderboard</li>
                <li>Link from player leaderboard track row to track leaderboard</li>
              </ul>
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Fixes bug in finshed and missing tracks logic (it wasn't working correctly)</li>
              </ul>
              <div class="text-h6">Performance</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Optimizes finished and missing tracks comparing logic</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.1.2 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.1.2 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-13 02:17') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>Fixes: Points weren't displayed anymore on the player leaderboard</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.1.1 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.1.1 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-13 01:52') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Features</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>On the player leaderboard you can now see all your missing tracks</li>
              </ul>
              <div class="text-h6">Bugfixes</div>
              <ul class="q-my-sm" style="font-size: 1rem">
                <li>All available entries on the track leaderboard are now shown correctly</li>
              </ul>
            </q-card-section>
          </q-card>
<!-- 0.1.0 Release -->
          <q-card>
            <q-card-section class="q-py-none q-px-sm row dialog-news-content-header">
              <div class="text-h5 q-py-sm">0.1.0 Release</div><q-space />
              <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-13 01:50') }}</div>
            </q-card-section>

            <q-card-section class="q-py-sm q-pl-lg dialog-news-content">
              <div class="text-h6">Initial Release</div>
            </q-card-section>
          </q-card>
        </q-card>
      </q-card>
    </q-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getDateDifferenceToNowByGermanTimezone } from 'src/modules/LeaderboardFunctions'

const props = defineProps(['showDialog'])
const emit = defineEmits(['update:showDialog'])

const value = computed({
  get() {
    return props.showDialog
  },
  set(value) {
    emit('update:showDialog', value)
  }
})
</script>
