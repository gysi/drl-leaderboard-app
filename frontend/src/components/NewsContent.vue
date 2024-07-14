<template>
  <q-card bordered style="max-height: 100%; display: flex; flex-direction: column">
    <q-banner inline-actions class="text-white bg-red-6" style="min-height: fit-content">
      <div class="text-h6">News</div>
<!--      <div> Your position within the game for individual tracks can be different from the position shown here.</div>-->
<!--      <q-btn type="a" flat padding="0"-->
<!--             :to="{ name: 'faq', query: { card: 'ingame-mismatch' } }"-->
<!--      >Read more in the FAQ</q-btn>-->
      <q-btn dense flat icon="close" v-close-popup style="position: absolute; top: 0; right: 0">
        <q-tooltip class="bg-white text-primary">Close</q-tooltip>
      </q-btn>
    </q-banner>
    <q-separator />
    <q-card style="overflow-wrap: break-word" class="scroll">
      <!-- 3.10.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Prize Pool Contributions and Rewards!</div>
          <q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-07-14 22:30') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            We're excited to introduce a new feature to reward our contributors! From now on, contributing to the prize pool will earn you special awards on the website and a unique Discord role.
            <br/><br/>
            <div class="text-h6">Website Awards</div>
            Contributors will have a special icon displayed beside their name on the leaderboards, showcasing their support for the community.
            <br/>
            <q-img
              src="~assets/news/24_summer_season_awards_preview.png"
              spinner-color="white"
              height="300px"
              fit="contain"
              position="0 0"
            />
            <br/><br/>
            <div class="text-h6">Discord Role</div>
            Contributors will also receive a special role in our Discord server, making it easy for everyone to recognize their support.
            <br/>
            <q-img
              src="~assets/news/24_summer_season_awards_discord_preview.png"
              spinner-color="white"
              height="100px"
              fit="contain"
              position="0 0"
            />
            <br/><br/>
            <div class="text-h6">Micro Contributions</div>
            You can now make micro contributions to the prize pool by subscribing to the Matcherino YouTube channel. Each subscription contributes 28 cents to the prize pool.
            <br/>
            <q-img
              src="~assets/news/24_summer_season_matcherino_prize_pool_contribution_subscription.png"
              spinner-color="white"
              height="250px"
              fit="contain"
              position="0 0"
            />
            <br/><br/>
            <div class="text-h6">Contribution Items</div>
            Check out the items you can purchase to contribute to the prize pool and earn these rewards. <br/>
            You will find it here: <a href="https://matcherino.com/tournaments/116263" target="_blank">Matcherino page</a>
            <br/>
            <q-img
              src="~assets/news/24_summer_season_matcherino_marketplace_items.png"
              spinner-color="white"
              height="300px"
              fit="contain"
              position="0 0"
            />
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.9.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Prize Pool Grows Again: Thanks, TheSauceror!</div>
          <q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-07-07 04:35') }}</div> </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            A big shoutout to <b>TheSauceror</b> for contributing $10 to the Community Summer Season prize pool! Your generosity is much appreciated and helps make this competition even more rewarding for our top pilots.
            <br/><br/>
            <div class="text-h6">Updated Prize Pool:</div>
            The updated prize pool distribution is as follows:
            <ul class="q-my-sm" style="font-size: 1rem">
              <li>1st Place: $474</li>
              <li>2nd Place: $338</li>
              <li>3rd Place: $230</li>
              <li>4th Place: $176</li>
              <li>5th Place: $135</li>
            </ul>

            Remember, you can still contribute to the prize pool through our <a href="https://matcherino.com/tournaments/116263" target="_blank">Matcherino page</a>!
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.9.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">🚨 Summer Season Heats Up: Red Series Tracks & New Bounce Penalty 🚨</div>
          <q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-07-07 03:20') }}</div> </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            The Community Summer Season is reaching its peak, and we've got a double dose of excitement to share!
            <br/><br/>
            <div class="text-h6">🔥 Red Series Unleashed! 🔥</div>
            Get ready to test your piloting mettle with the final five tracks of the season, the <b>Red Series</b>. Designed for the most advanced pilots, these tracks are a technical tour de force, featuring:
            <ul>
              <li>[SUM24-21] HOT LAP, by NoSkillSammy</li>
              <li>[SUM24-22] LAST ROUND, by Shaigne</li>
              <li>[SUM24-23] ROASTED AND TOASTED, by WestFPV</li>
              <li>[SUM24-24] CRUNCH TIME, by GreenMachine</li>
              <li>[SUM24-25] HEAD IN THE SAND, by Mr. Persister</li>
            </ul>
            Expect a symphony of gravity elements, precision turns, and every other challenge the track designers could throw your way. It's time to leave it all on the track!
            <br/><br/>
            <div class="text-h6">⚠️ Bounce Penalty Incoming ⚠️</div>
            Following extensive community discussion and a poll, we're introducing a new bounce penalty system to ensure fair play and discourage unintended speed boosts. Here's the lowdown:
            <ul>
              <li><b>What's a Bounce?</b> A bounce occurs when your drone hits a surface and gains a speed advantage.</li>
              <li><b>The Penalty:</b> A 1-second penalty will be added to your time for each significant bounce detected in your replay.</li>
              <li><b>Why the Change?</b> To promote skillful piloting and maintain the integrity of the competition.</li>
              <li><b>How It Works:</b> A smart algorithm analyzes replays for bounces that would cause damage. Minor bumps won't be penalized.</li>
              <li><b>Transparency:</b> Runs with penalties will be clearly marked on the leaderboards.</li>
            </ul>
            We're committed to refining the algorithm to minimize false positives. If you believe a penalty was applied in error, please report it.
            <br/><br/>
            <div class="text-h6">🚀 Race On!</div>
            We can't wait to see you master the Red Series tracks and adapt to the new penalty system. The competition is fiercer than ever, so give it your all and make your mark on the Summer Season!
            <br/><br/>
            <div class="text-h6">Additional Notes:</div>
            <ul>
              <li><b>FAQ Update:</b> Check out the <q-btn type="a" flat padding="0" :to="{ name: 'communitySeasonCompetitionFaq' }">FAQ</q-btn> section on the website for a detailed breakdown of the bounce penalty system.</li>
              <li><b>Community Feedback:</b> As always, we value your feedback! Share your thoughts on the new tracks and penalty system on the DRL Discord.</li>
            </ul>
            Let's see who will claim victory in the final stretch of the Community Summer Season! 🏆
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.8.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.8.0 - Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-07-03 20:00') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>The replay viewer now shows bounces (vertical yellow lines).<br/>
              In the upcomming days we will introduces a time penalty for bounces on replays for the summer season tracks
              (go to the DRL Discord Announcement channel for more information)
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.7.8 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Summer Season - Orange Series Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-29 03:33') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            We are thrilled to continue our Summer Season competition with the release of our next series of tracks. 🌞🎉
            <br/><br/>
            After the launch of the Yellow Series, it's time to introduce the Orange Series!
            We have an incredible lineup of tracks that we can't wait for you to explore and enjoy.
            Here are the tracks being released this week:
            <ul>
              <li>[SUM24-16] SUMMERFELL (by FPVgan)</li>
              <li>[SUM24-17] ATHENA''S RAMPAGE (by Swoop)</li>
              <li>[SUM24-18] NIGHTWATCH (by Jab)</li>
              <li>[SUM24-19] CUBIC TRIPP (by TrippFPV)</li>
              <li>[SUM24-20] AFTER WORK (by Shaigne)</li>
            </ul>
            A huge shoutout and thanks to our talented track creators for their amazing work.

            Best regards, <br/>
            Your Community Team
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.7 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.7.7 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-23 19:50') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Highlights the active tab's background on the tournament page so that its more clear that you can click on them
            </li>
            <li>Adds more metadata to tournament results:<br/>
              - Track Name<br/>
              - Winner icon<br/>
              - Goldenheat participant icon<br/>
              - Goldenheat time<br/>
              - Dnf icon<br/>
              <q-img
                src="~assets/news_tournament_metadata.png"
                spinner-color="white"
                height="300px"
                fit="contain"
                position="0 0"
              />
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.6 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Summer Season Competition - Yellow Series Release!</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-15 02:15') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            We are thrilled to continue our Summer Season competition with the release of our next series of tracks. 🌞🎉
            <br/><br/>
            After the launch of the Green series, it's time to introduce the Yellow Series!
            We have an incredible lineup of tracks that we can't wait for you to explore and enjoy.
            Here are the tracks being released this week:
            <ul>
              <li>[SUM24-11] DRL HEADQUARTERS</li>
              <li>[SUM24-12] KING'S COURT</li>
              <li>[SUM24-13] SANDSTORM</li>
              <li>[SUM24-14] SHADOWS AND DUST</li>
              <li>[SUM24-15] COOL BREEZE</li>
            </ul>

            A huge shoutout and thanks to our talented track creators for their amazing work:
            <ul>
              <li>FPVgan</li>
              <li>Jab</li>
              <li>Mr. Persister</li>
              <li>Shaigne</li>
              <li>WestFPV</li>
            </ul>

            Best regards, <br/>
            Your Community Team
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.5 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Prize Pool Update</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-13 00:19') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            The prize pool funding and payout is now managed through Matcherino.<br/>
            Check it out: <a href="https://matcherino.com/tournaments/116263" target="_blank">https://matcherino.com/tournaments/116263</a>
            <br/>
            This makes contributions to the prize pool easier and more legitimate.
            Also payouts are way easier to manage through their system.
            <br/><br/>
            Make sure to create an account on Matcherino and join the competition if you want to claim prize money.
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.4 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Prize Pool Update</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-08 04:15') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            The prize pool got updated, another contribution by THE_BOB!!: $50
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.3 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Summer Season Competition - Green Series Release!</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-08 02:00') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
            We are thrilled to continue our Summer Season competition with the release of our next series of tracks. 🌞🎉
            <br/><br/>
            After the launch of the Blue series, it's time to introduce the Green Series! We have an incredible lineup of tracks that we can't wait for you to explore and enjoy. Here are the tracks being released this week:
            <ul>
              <li>[SUM24-06] GREEN FLAG</li>
              <li>[SUM24-07] PAPERJAM</li>
              <li>[SUM24-08] CASTLE CRUISE</li>
              <li>[SUM24-09] RIPPIN' THROUGH THE RUINS</li>
              <li>[SUM24-10] TRIPP LITE</li>
            </ul>

            A huge shoutout and thanks to our talented track creators for their amazing work:
            <ul>
              <li>FPVgan</li>
              <li>Green Machine</li>
              <li>TrippFPV</li>
              <li>Mr. Persister</li>
              <li>Shaigne</li>
            </ul>

            We also want to extend our gratitude to the creators who built those maps from scratch:
            <ul>
              <li>Flightless (Collosuem)</li>
              <li>Mr. Persister (Sand Castle)</li>
              <li>Shaigne (Solitude & Bio Modern City)</li>
              <li>XTK FPV (Office Space)</li>
            </ul>
            Thank you all for your incredible contributions and hard work.
            <br/><br/>
            Get ready to race, explore, and compete on these fantastic new tracks. We can't wait to see your best times and epic moments. Let the Green Series begin! 🏁🏆
            <br/><br/>
            Best regards, <br/>
            Your Community Team
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.2 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Prize Pool Update & Fix</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-05 09:35') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
          The prize pool got updated, another contribution by THE_BOB!!: $50
          </div>
          <div class="text-h6">Fixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>When Community Season tracks are added to the Featured section,
              they don't count towards the official track rankings.
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">Community Summer Season Competition FAQ</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-02 03:40') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="q-my-sm" style="font-size: 1rem">
          There is now a FAQ Page about the current ongoing competition: <br/>
          <q-btn type="a" flat padding="0"
                 :to="{ name: 'communitySeasonCompetitionFaq' }"
          >Click here to go to the FAQ</q-btn>
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
<!-- 3.7.0 Release - Summer Season 2024 Start -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">🏁 Summer Season 2024 Has Begun! 🏁</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-01 01:40') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="text-h6">Info:</div>
          <div style="font-size: 1rem">
            The highly anticipated Community Summer Season 2024 is here, and this time we have something truly special lined up. Instead of racing on existing community tracks, we've collaborated with nine exceptional track designers to bring you an exclusive map pack designed just for this season.
            <br/><br/>
            <b>🏆 Prize Pool 🏆</b><br/>
            We are excited to announce a prize pool for the top pilots this season:
            <ul class="q-my-sm" style="font-size: 1rem">
              <li>1st Place: $470</li>
              <li>2nd Place: $336</li>
              <li>3rd Place: $228</li>
              <li>4th Place: $174</li>
              <li>5th Place: $134</li>
            </ul>
            You can also contribute to the prize pool! Any contributions will increase the total prize pool proportionally across all placings based on the current distribution.
            <br/><br/>
            <b>💸 Prize Pool Contributors 💸</b>
            <ul class="q-my-sm" style="font-size: 1rem">
              <li>gysi: $575</li>
              <li>FPVgan: $250</li>
              <li>itsLeeFPV: $200</li>
              <li>THE_BOB!!: $200</li>
              <li>TrippFPV: $100</li>
              <li>Str33tz: $20</li>
            </ul>
            Our talented track designers for this season are:
            <ul class="q-my-sm" style="font-size: 1rem">
              <li>FPVgan</li>
              <li>Green Machine</li>
              <li>Jab</li>
              <li>Mr. Persister</li>
              <li>NoSkillSammy</li>
              <li>Shaigne</li>
              <li>Swoop</li>
              <li>TrippFPV</li>
              <li>WestFPV</li>
            </ul>
            These creators, led by the meticulous planning and organization of Mr. Persister, have developed a series of unique tracks that promise to challenge and engage pilots of all skill levels. The project kicked off with a vision to create 25 brand new tracks across five community maps, each offering varied difficulty and exciting new racing lines.
            <br/><br/>
            Tracks will be released in batches throughout the season (every week), and each one is designed to push your skills to the limit while ensuring a fun and thrilling experience.
            <br/><br/>
            We have organized the tracks into series based on difficulty, each marked with a distinct color. The first to be released is the Blue Series, designed for beginners with 30-40 second tracks featuring basic elements such as chicanes, slaloms, and gentle turns. Here's a breakdown of the series and what to expect:
            <ul class="q-my-sm" style="font-size: 1rem">
              <li><b>🔵 Blue Series - Beginner (30-40 seconds)</b><br/>
                Basic elements, chicane + slalom, gentle turns, lots of space between gates.
              </li>
              <li><b>🟢 Green Series - Easier Intermediate (40-50 seconds)</b><br/>
                Easy elements, split S/Immelmann, sweeper/blaster tunnels.
              </li>
              <li><b>🟡 Yellow Series - Intermediate (50-60 seconds)</b><br/>
                Intermediate elements, inversions/gravity elements, dive gates, hairpin turns.
              </li>
              <li><b>🟠 Orange Series - Easier Advanced (60-70 seconds)</b><br/>
                Intro to technical elements, ladders, corkscrews, hurdles.
              </li>
              <li><b>🔴 Red Series - Advanced (70-80 seconds)</b><br/>
                All elements, technical, precision, gravity.
              </li>
            </ul>
            <div style="font-size: 1.2rem; font-weight: bold; color: #FF5733;">
              A HUGE SHOUTOUT and THANK YOU to everyone who made this possible
            </div>
            <div style="font-size: 1.2rem; font-weight: bold; color: #FF5733;">
              Your dedication and creativity are what make our community thrive!
            </div>
            Stay tuned for more updates and get ready to test your piloting skills on these innovative new tracks. Happy racing!
          </div>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.6.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.6.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-05-14 02:40') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Adds command to discord tournament bot to tag a role for tournament reminders
            </li>
            <li>Adds command to discord tournament bot to test the messages
            </li>
            <li><a href="https://discord.com/api/oauth2/authorize?client_id=1192998273594708058&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.5.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.5.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-05-04 17:10') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Adds Best Position as an additional column for the ranking pages
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.4.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.4.0 Release - Better Replay Viewer #3</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-04-08 04:42') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Its now possible to move arround with wasd within the replay viewer
            </li>
          </ul>
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>It was possible to zoom in too much and then panning using the right mouse was too slow
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.3.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.3.0 Release - Better Replay Viewer #2</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-04-07 20:30') }}</div>
        </q-card-section>
        <q-card-section class="q-pa-sm news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>The replay line for each player now displays a gradient that begins with a brighter color and gradually darkens.
              This enhancement aids in distinguishing between lines when a track includes multiple laps.
            </li>
            <li>The replay line now indicates spots where the throttle is less engaged, starting with yellow for slight easing
              and progressing to red/pink when the throttle is nearly fully released (below mid-throttle).
              <q-img
                src="~assets/news_replay_throttle.png"
                spinner-color="white"
                height="250px"
                fit="contain"
                position="0 0"
              />
            </li>
          </ul>
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Introduced lazy loading for player profile pictures on the overall ranking page, rather than loading all 500 images directly.
            </li>
            <li>Player replays now load asynchronously, preventing UI blockages.
            </li>
            <li>Resolved an issue where, occasionally, a player's replay line would not be displayed (e.g., Jab's run on OOS Expo).
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.2.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.2.0 Release - Better Replay Viewer</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-04-06 00:33') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>The replay viewer is now 100% accurate and shows the original meshes for checkpoint/trigger gates.
            </li>
          </ul>
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Fixes a bug where the discord leaderboard bot would post times that were old.
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.1.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.1.0 Release - NEW DOMAIN!</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-03-29 21:45') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <ul>
            <li>The DRL Leaderboard website now has its own domain! https://drl-leaderboards.com</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.0.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.0.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-03-29 17:36') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Bug fixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>When a player set a new pb his old "gotBeatenBy" entries weren't updated/removed.
            </li>
            <li>The links from the track leaderboard of community tracks to profile
              pages were broken.
            </li>
            <li>Fixes the cache for the latest top10 entries, it wasn't updated at all.
            </li>
            <li>Fixed a bug in the leaderboard Discord bot where it incorrectly announced a player's improvement
              even though the player had not actually advanced in position. This issue occurred when an entry was
              initially submitted without a replay; as a result, the bot did not post an update due to the submission
              appearing invalid. Later, once a replay was added, the bot failed to recognize any position improvement
              because it had already recorded the player's standing, leading to a false notification of progress.
            </li>
            <li>'Lastest Activity' board entries correctly link to community profile leaderboards when its a community track
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 3.0.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">3.0.0 Release - Community Season</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-03-29 02:00') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>Community Seasons:</b><br/>This release introduces Community Seasons.<br/>
              A new section has been added to the navigation for this purpose.
              A Community Season has the same cycle as the Tournament Season and is in sync with it.<br/>
              Per season, players compete for 1st place on 30 tracks created by the community.
              The selection of tracks is based on an algorithm that selects the tracks per season,
              and manual adjustments are made if necessary.<br/>
              You can read details about this algorithm in the FAQs<br/><br/>

              The leaderboard entries for the community tracks and the ranking are only displayed and counted
              if a player has at least one best time on these tracks which took place within the season.<br/>
              As a rule, there should always be one of the 30 tracks on which a player has no time or can easily
              get a better time. The advantage is that the ranking ignores old entries from players who are
              longer active, especially because the season also includes well-known but older community tracks.
              <br/><br/>
              A heartfelt appreciation goes to Mr. Persister &lt;3, whose boundless enthusiasm and meticulous testing of the Community Tracks
              have greatly enriched our seasonal dynamics. The invaluable insights gained from our extensive discussions and collaborations
              have directly influenced the seasonal offerings, enhancing the competitive spirit and community involvement on the website.
              <br/><br/>
            </li>
            <li><b>More Leaderboard Entries per Track:</b><br/>
              Up to 200 leaderboard entries are now displayed in the track leaderboards,
              which helps newer and less experienced players to be included on the website.
              <br/><br/>
            </li>
            <li><b>Faster crawling and more up-to-date data:</b><br/>
              Before, all tracks in the pool were crawled one after the other. Now there are 2 pools that crawl tracks in parallel.
              A high-priority pool and a low-priority pool.<br/>
              The highpriority pool contains 15% of the tracks with the most recent entries and the other pool contains the rest.<br/>
              Mostly players fly tracks that have been flown by others recently or they reach several PBs in a row and with this system
              the times should now appear much faster on the website.<br/><br/>
              In addition, caches previously existed for all rankings and leaderboard entries, which were only updated once all
              tracks had been crawled. These caches are now updated after each crawled track.
              <br/><br/>
            </li>
            <li><b>Track thumbnails:</b><br/>
              In the track leaderboard header or on the tracks page (community section),
              the real thumbnails created by the track creator are now displayed (if any exist).
              <br/><br/>
            </li>
            <li><b>Latest Activity:</b><br/>
              The rankings pages now include a new "Latest Activity" column to give you a better idea of who is active.
              <br/><br/>
            </li>
            <li><b>Featured Twitch Streams:</b><br/>
              DRL Twitch streams are now displayed at the bottom of the navigation, sorted by most viewers.
              Here is a preview (Because there was no DRL live stream at the time of this post, I took the
              famous game "Pools, Hot Tubs, and Beaches" as an example)
              <br/>
              <q-img
                src="~assets/featured_twitch_streams.png"
                spinner-color="white"
                height="250px"
                fit="contain"
                position="0 0"
              />
              <br/><br/>
            </li>
            <li><b>Styling:</b><br/>
              The styling has been slightly optimised for the respective light and dark modes. In addition,
              there are now customised scrolling bars, unless you use Firefox.
            </li>
          </ul>
          <div class="text-h6">Changes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>"Tracks" got removed:</b><br/>
              The navigation entry "Track" has been removed.
              It did not really offer any added value and has now been removed for the time being
              until it becomes more useful and more information is displayed there or functions are added.
            </li>
            <li><b>Navigation:</b><br/>
              A few less frequently used items have now been summarised in "Other"
            </li>
          </ul>
          <div class="text-h6">Bug fixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Adds another invalid run reason: WRONG_REPLAY, read more about in the FAQ
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.13.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.13.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-27 00:30') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Bug fixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Adds missing track to leaderboard: CTG / 07: TREEHOUSE TANGO<br/>
              It wasn't added in the past because DRL didn't flag it as an official track.<br/>
              If you just don't see it yet, it takes some time to get picked up by the backend. The backend looks for new tracks every full hour.
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.13.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.13.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-25 23:55') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>DRL Betaflight Calculator Enhancement:</b>
              The calculator has been updated to include support for controller inputs.<br/>
              Now, it dynamically displays the highest absolute value from any axis input across all three graphs for an intuitive and comprehensive visualization of controller interactions.
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.12.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.12.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-25 02:33') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>DRL Betaflight Calculator:</b>
              Based on TwiceFPV's innovative idea, we've introduced the interactive DRL Betaflight Calculator. This feature allows users to simulate adjustments within the application, enhancing your experience. Available now via the navigation menu.
              <q-img
                src="~assets/drl-betaflight-calculator_medium.png"
                spinner-color="white"
                height="250px"
                fit="contain"
                position="0 0"
              />
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.11.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.11.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-21 20:00') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Updates</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>Pointsystem Change:</b> Following extensive feedback, the points system was adjusted again.
              It has been changed back to a position-based system instead of a time-based one.
              It works in a similar way to before, but the underlying formula has been adjusted so that the points increase exponentially with the position.
              The bonus points have also been adjusted.
              <q-btn type="a" flat padding="0"
                     :to="{ name: 'faq', query: { card: 'pointSystem' } }"
              >Read more in the FAQ</q-btn>
              <q-img
                src="~assets/pointsystem.png"
                spinner-color="white"
                height="250px"
                fit="contain"
                position="0 0"
              />
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.11.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.11.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-19 03:40') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>Preparatory Updates:</b> Certain data architecture improvements have been made related to player data relations. This groundwork is in anticipation of substantial features coming in the near future.</li>
            <li><b>New Points Calculation System:</b> A new method of player points calculation is implemented in this update. The system calculates points using a logarithmic relationship between their position milliseconds and the first position milliseconds. This calculation method is designed to offer a better balance of competition and reward for consistent high-level play.</li>
          </ul>
          <div class="text-h6">Updates</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>Updated FAQs:</b> The FAQ section has been updated to reflect the new points calculation system. Check it out for a more extensive understanding of how points are awarded.</li>
          </ul>
          <div class="text-h6">Bug Fixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>Duration Display Correction:</b> A bug in the news section where the duration was displayed incorrectly has been fixed. Now the duration values should represent time periods accurately.</li>
            <li><b>Improved Player Name Encoding:</b> A fix was introduced to handle special characters in player names. This will ensure that the player names are transmitted correctly. This was especially an issue for some players that had a "|" in their name.</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.10.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.10.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-02 21:00') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li><b>Enhanced Player Ranking Logic:</b> The latest update introduces a refined approach to determining player rankings for tournaments. When players accumulate the same points from their best 12 tournaments, rankings are now further decided by examining their overall performance across all tournaments. This ensures that players with more top (lower number) finishes are ranked higher, promoting a fairer and more comprehensive evaluation of player performance, rewarding consistency and excellence across events.</li>
            <li><b>Updated Tournament Point System FAQs:</b> The FAQ section related to the tournament point system has been updated to clearly explain the process of ranking determination. </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.9.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.9.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-01-06 02:20') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>There is now an additional discord bot for tournaments that posts reminders, tournament starts and results.
              You can add it to your server by calling the following URL in your browser:
              <br/>
              <a href="https://discord.com/api/oauth2/authorize?client_id=1192998273594708058&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
              <br/>Commands:
              <ul><li>/tourney-reminder-activate [channel]</li><li>/tourney-reminder-deactivate [channel]</li>
                <li>/tourney-results-activate [channel]</li><li>/tourney-results-deactivate [channel]</li>
              </ul>
            </li>
          </ul>
          <div class="text-h6">Misc</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>The leaderboard discord bot commands were renamed:
              <br/>
              <a href="https://discord.com/api/oauth2/authorize?client_id=1189335274422009978&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
              <br/>Commands:
              <ul><li>/leaderboard-posts-activate [channel]</li><li>/leaderboard-posts-deactivate [channel]</li>
              </ul>
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.8.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.8.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-12-28 23:25') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Adds some additional information (faster by, pb time, diff to leader) to the discord embeds of the leaderboard bot.
            </li>
          </ul>
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Switches from selection menu to autocomplete on commands for the discord bot, because the menu is limited to 25 entries<br/>
              The Command structure is now as follows:
              <ul><li>/drl-leaderboard-posts-activate [channel]</li><li>/drl-leaderboard-posts-deactivate [channel]</li></ul>
            </li>
            <li>Fixes a bug where the discord bot wouldn't post pbs when they didn't have a replay (because of the DRL System still processing the replay) but later on got one.
            </li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 2.8.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.8.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-12-28 01:30') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.7.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-12-12 23:50') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.6.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-07-29 04:12') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.6.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-25 01:30') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.5.2 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-24 01:08') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.5.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-23 12:10') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.5.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-18 23:50') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.4.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-12 02:45') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.3.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-03 02:30') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.2.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-05-24 02:00') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.1.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-05-23 01:20') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">2.0.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-05-01 02:40') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.9.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-16 23:53') }}</div>
        </q-card-section>
        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.9.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-16 03:00') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.8.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-13 02:05') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.8.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-12 02:40') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.7.3 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-03-23 21:12') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.7.2 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-03-06 00:25') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.7.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-03-05 00:12') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.7.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-23 01:10') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.6.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-13 00:30') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.5.3 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-07 21:07') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.5.2 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-02 23:07') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Performance</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Performance optimization for track lb page</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 1.5.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.5.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-02 03:26') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Adds two new statistics to the home page. "Most entries by track last 14 days" and "Most entries by track last month"</li>
            <li>Adds links on each statistics container for player and tracks</li>
            <li>Adds Map background animation on track leaderboard table header</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 1.4.2 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.4.2 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-01 11:10') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>NGINX now includes the charset in the content-type header of the response,
              this helps with some systems to correctly display emojis (e.g. discord url embeds)</li>
            <li>Sets an additional meta tag for encoding too</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 1.4.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.4.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-01 01:55') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Also removes the 0 in front of time differences when comparing against other players when its positive</li>
            <li>Changes label of compare player select</li>
            <li>Fixes bug when comparing against others and then selecting another player</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 1.4.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.4.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-01 01:06') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 1.3.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.3.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-30 02:04') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 1.2.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.2.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-29 24:00') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 1.0.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">1.0.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-29 05:14') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Adds a new page to the nav. Its a random map picker. It picks the worst maps for the selected player using a fancy slotmachine.</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 0.10.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.10.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-22 18:26') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 0.9.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.9.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-22 03:43') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>The tracks page has now images for each map</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 0.8.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.8.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-21 04:09') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">DARKMODE ACTIVATED!!!</div>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 0.7.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.7.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-19 24:10') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Sort by position and then by score for the track leaderboard</li>
            <li>Correctly sets is-custom-map parameter on leaderboard requests depending on
              name which before resulted in invalid times showing up on the leaderboard</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 0.7.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.7.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 19:13') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Enables sorting for the player leaderboard</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 0.6.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.6.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 17:01') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 0.5.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.5.0 Release (PINK IS THE DEVIL)</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 02:11') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 0.4.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.4.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-16 02:22') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 0.3.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.3.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-15 19:01') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Features</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Introduces a new column on the player leaderboard that displays if player personal bests
              have recently been surpassed or are currently performing well since they were submitted.</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 0.2.3 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.2.3 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-15 03:26') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 0.2.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.2.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-14 03:39') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 0.1.2 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.1.2 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-13 02:17') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Bugfixes</div>
          <ul class="q-my-sm" style="font-size: 1rem">
            <li>Fixes: Points weren't displayed anymore on the player leaderboard</li>
          </ul>
        </q-card-section>
      </q-card>
      <q-separator />
      <!-- 0.1.1 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.1.1 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-13 01:52') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
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
      <q-separator />
      <!-- 0.1.0 Release -->
      <q-card>
        <q-card-section class="q-py-none q-px-sm row news-content-header">
          <div class="text-h5 q-py-sm">0.1.0 Release</div><q-space />
          <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-13 01:50') }}</div>
        </q-card-section>

        <q-card-section class="q-py-sm q-pl-lg news-content">
          <div class="text-h6">Initial Release</div>
        </q-card-section>
      </q-card>
    </q-card>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
