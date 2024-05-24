<template>
  <q-page class="q-pa-md">
    <div class="grid">
      <div class="grid-sizer"></div>
      <FAQCard
        title="Why isn't my recently submitted time appearing on the leaderboard?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'no-time'"
      >
        <p>The timing of when your new personal best (PB) appears on the leaderboard can vary due
          to the way our website updates its data. Our system periodically scans all the tracks through
          the game's API to fetch the latest times. This process means there's a delay between achieving
          a new PB and its appearance on the website.
        </p>

        <p>Tracks that see a lot of recent activity are updated more frequently than those with less.
          Generally, you can expect your new time to be reflected on the leaderboard within 2 to 10 minutes
          after you've set it.
        </p>
        <p>We appreciate your patience as our system works to bring you the most up-to-date rankings.
        </p>
        <ul>
          <li>Active Tracks: ~1min</li>
          <li>Inactive Tracks: ~5min</li>
          <li>Tournaments: ~30s</li>
        </ul>
      </FAQCard>
      <FAQCard
        title="My placement doesn't match with the in-game leadeboard"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'ingame-mismatch'"
      >
        <p>If you notice discrepancies between your placement on our website and the in-game
          leaderboard, it may be due to the game occasionally recording times that are considered
          invalid due to bugs. While the game might still include these placements, our website
          excludes them to ensure fairness and accuracy in our rankings. For a detailed explanation
          of what constitutes an invalid time and how these are identified by our system, please
          refer to the "Invalid Runs" FAQ card. This will give you a clearer understanding of our
          validation process and why certain times may not be counted in our leaderboard.
        </p>
      </FAQCard>
      <FAQCard
        title="How does the leaderboard track point system work?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'pointSystem'"
      >
        <div>The number of points a player receives is calculated based on their position on the leaderboard using the formula</div>
        <b><span>base points = 1046.1 - 515.88 * log(position + 2)</span></b>
        <div>In addition, bonus points will be awarded to the player below certain positions.</div>
        <div>
          <q-markup-table class="q-ma-sm">
            <thead>
            <tr>
              <th class="text-center">Position</th>
              <th class="text-center">Bonus Points</th>
            </tr>
            </thead>
            <tbody class="text-center">
            <tr>
              <td>&lt;= 75</td>
              <td>+20</td>
            </tr>
            <tr>
              <td>&lt;= 50</td>
              <td>+20</td>
            </tr>
            <tr>
              <td>&lt;= 25</td>
              <td>+20</td>
            </tr>
            <tr>
              <td>&lt;= 10</td>
              <td>+40</td>
            </tr>
            <tr>
              <td>&lt;= 5</td>
              <td>+20</td>
            </tr>
            <tr>
              <td>&lt;= 4</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 3</td>
              <td>+9</td>
            </tr>
            <tr>
              <td>&lt;= 2</td>
              <td>+30</td>
            </tr>
            <tr>
              <td>= 1</td>
              <td>+36</td>
            </tr>
            </tbody>
          </q-markup-table>
        </div>
        <div>Here are some examples:</div>
        <div>
          <q-markup-table class="q-ma-sm">
            <thead>
            <tr>
              <th class="text-center">Position</th>
              <th class="text-center">Points earned</th>
            </tr>
            </thead>
            <tbody class="text-center">
            <tr>
              <td>1</td>
              <td>1000</td>
            </tr>
            <tr>
              <td>2</td>
              <td>900</td>
            </tr>
            <tr>
              <td>3</td>
              <td>820</td>
            </tr>
            <tr>
              <td>4</td>
              <td>770</td>
            </tr>
            <tr>
              <td>5</td>
              <td>730</td>
            </tr>
            <tr>
              <td>10</td>
              <td>589</td>
            </tr>
            <tr>
              <td>25</td>
              <td>368</td>
            </tr>
            <tr>
              <td>50</td>
              <td>201</td>
            </tr>
            <tr>
              <td>75</td>
              <td>93</td>
            </tr>
            <tr>
              <td>100</td>
              <td>10</td>
            </tr>
            </tbody>
          </q-markup-table>
        </div>
        <span>Points are rounded to a whole number for display purposes. However, the total points in the overall ranking is accurate to 14 digits and is only then rounded.</span>
        <q-img
          src="~assets/pointsystem.png"
          spinner-color="white"
        />
      </FAQCard>
      <FAQCard
        title="What does it mean if a run is marked as invalid"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'invalidRuns'"
      >
        <p>
          Invalid runs refer to attempts by a player that do not earn any points and are not considered
          as completed tracks. These runs are displayed in a separate column in the overall rankings and
          are highlighted with a red background and a warning sign in the track and player leaderboards.
          Hovering over the warning sign will display the reason for the invalidation.</p>

        <p>There are multiple possible reasons for a run to be flagged as invalid:</p>
        <div>
          <b>BETTER_ENTRY_WITH_SAME_NAME</b>
        </div>
        <p>
          The unique identifier for a player on this site is their player name.
          This was chosen because nobody knows their player id and it isn't something that can easily be
          remembered.
          Due to the game allowing players to use the same name multiple times, it is possible for
          multiple entries with the same name on the same track. To prevent this, the leaderboards
          only consider the best entry with the same name to be valid.
        </p>
        <div>
          <b>BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT</b>
        </div>
        <p>
          Some players have multiple accounts, which is not allowed and can clutter up the leaderboards.
          The site only considers the best entry from either of the accounts to be valid.
          The list of known double accounts is maintained by the administrator,
          and as soon as a double account is discovered, it is added to the list.<br/>
          If a run is flagged with both <br/>BETTER_ENTRY_WITH_SAME_NAME and<br/>
          BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT,<br/> it is because both accounts use the same name.
        </p>
        <div>
          <b>IMPOSSIBLE_TOP_SPEED</b>
        </div>
        <p>
          Any run that exceeds the top speed of 104 is flagged as invalid as it is impossible to reach
          such a speed with the Racer4. Some players may have a bug in their game that allows them to
          reach such a speed by using a different drone. This bug will be fixed by the game developers
          in the future.
        </p>
        <div>
          <b>NO_REPLAY</b>
        </div>
        <p>
          Any run that does not have a replay attached to it is flagged as invalid as the replay is the
          only way to verify the run. To ensure that your replay is attached to your run, you should
          ensure that the game can successfully upload it to the server. If you have a poor internet
          connection, the game may not be able to upload the replay. To increase the chance of successful
          upload, wait for about 5-10 seconds on the first screen after your run. This will give the
          game enough time to upload the replay.
        </p>
        <div>
          <b>EPIC_BUG</b>
        </div>
        <p>
          There is currently a bug on the epic platform that can mark a run as completed when the player
          has not completed the course, resulting in a 1st place ranking and a huge time difference to the
          2nd place ranking.
        </p>
        <div>
          <b>WRONG_DRONE</b>
        </div>
        <p>
          Any run that uses a drone other than the Racer4 is flagged as invalid.
          There is currently a bug in the game that allows players to use other drones in the time trial mode.
          This bug will be fixed by the game developers in the future.
        </p>
        <div>
          <b>WRONG_REPLAY</b>
        </div>
        <p>
          There is a bug in DRL were sometimes a wrong replay for another track is uploaded.
        </p>
      </FAQCard>
      <FAQCard
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'beatenBy'"
      >
        <template v-slot:title>
          <div class="text-h6">
            What is the "Beaten by" column
            (<q-icon name="query_stats" color="white" size="md" />)
            on the player leaderboard?
          </div>
        </template>
        <p>
          The "Beaten by" column in the player leaderboard shows how many players have beaten the time of the respective line since this time was set.
        </p>
        <p>
          On mouseover, if the time has been beaten, a table of players who have beaten that time is displayed. The table is limited to 5 entries and is sorted by the date of creation. Players who have recently beaten the time are displayed first.
        </p>
        <p>
          If no one has beaten the time since it was created, then the number of days since the time was created is shown.
        </p>
        Here is a table explaining the different icons:
        <q-markup-table class="q-ma-sm">
          <thead>
          <tr>
            <th class="text-center">Icon</th>
            <th class="text-center">Meaning</th>
          </tr>
          </thead>
          <tbody class="text-center">
          <tr>
            <td>
              <q-icon
              name="arrow_downward"
              color="red"
              size="md"
              style="background: rgba(0,0,0,0.07); border-radius: 50%; padding: 2px;"
              />
            </td>
            <td>Beaten by 5 or more players</td>
          </tr>
          <tr>
            <td>
              <q-icon
                name="trending_down"
                color="red"
                size="md"
                style="background: rgba(0,0,0,0.07); border-radius: 50%; padding: 2px;"
              />
            </td>
            <td>Beaten by x players (0 &lt; x &lt; 5)</td>
          </tr>
          <tr>
            <td>
              <q-icon
                name="check"
                color="green"
                size="md"
                style="background: rgba(0,0,0,0.07); border-radius: 50%; padding: 2px;"
              />
            </td>
            <td>Not beaten by anyone yet for x days (x &lt;= 7)</td>
          </tr>
          <tr>
            <td>
              <q-icon
                name="auto_graph"
                color="green"
                size="md"
                style="background: rgba(0,0,0,0.07); border-radius: 50%; padding: 2px;"
              />
            </td>
            <td>Not beaten by anyone yet for x days (7 &lt; x &lt;= 14)</td>
          </tr>
          <tr>
            <td>
              <q-icon
                name="surfing"
                color="green-6"
                size="md"
                style="background: rgba(0,0,0,0.07); border-radius: 50%; padding: 2px;"
              />
            </td>
            <td>Not beaten by anyone yet for x days (14 &lt; x &lt;= 30)</td>
          </tr>
          <tr>
            <td>
              <q-icon
                name="military_tech"
                color="yellow-9"
                size="md"
                style="background: rgba(0,0,0,0.07); border-radius: 50%; padding: 2px;"
              />
            </td>
            <td>Not beaten by anyone yet for x days (x &gt; 30)</td>
          </tr>
          </tbody>
        </q-markup-table>

      </FAQCard>
      <FAQCard
        title="How does the tournament point system work?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'tournamentPointSystem'"
      >
        The tournament points are based on the DRL irl point system and only the best 12 tournaments for each player are counted. In cases where players have equal points from their best 12 tournaments, rankings are determined based on their overall positions across all tournaments played. The player with more lower (better) positions will rank higher. This ensures that consistent top performance across events is rewarded in the final rankings.
        <div>
          <q-markup-table class="q-ma-sm">
            <thead>
            <tr>
              <th class="text-center">Position</th>
              <th class="text-center">Points</th>
            </tr>
            </thead>
            <tbody class="text-center">
            <tr>
              <td>1</td>
              <td>25</td>
            </tr>
            <tr>
              <td>2</td>
              <td>20</td>
            </tr>
            <tr>
              <td>3</td>
              <td>16</td>
            </tr>
            <tr>
              <td>4</td>
              <td>13</td>
            </tr>
            <tr>
              <td>5</td>
              <td>11</td>
            </tr>
            <tr>
              <td>6</td>
              <td>9</td>
            </tr>
            <tr>
              <td>7</td>
              <td>7</td>
            </tr>
            <tr>
              <td>8</td>
              <td>5</td>
            </tr>
            <tr>
              <td>9</td>
              <td>3</td>
            </tr>
            <tr>
              <td>10</td>
              <td>2</td>
            </tr>
            <tr>
              <td>11</td>
              <td>1</td>
            </tr>
            </tbody>
          </q-markup-table>
        </div>
      </FAQCard>
      <FAQCard
        title="What is a season?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'season'"
      >
        <p>A season on this website brings together competition and community, framed within the four
          seasons of the year: spring, summer, fall, and winter. Each three-month period launches a series
          of engaging activities, offering a structured cadence to our community's involvement.
        </p>
        <p>The season's highlights are the Tournaments and Community Tracks. Weekly, players can dive into
          up to six tournaments, with their outcomes and upcoming schedules illustrated on the leaderboard
          through a simple color-coded system: green dots signal completed tournaments, yellow dots indicate
          future competitions, and red dots denote missed ones. This intuitive indicator, complemented by hoverable
          dates, simplifies tracking one's progress and planning for future engagements.
        </p>

        <p>For Community Tracks, each season unveils 30 new challenges, curated from the community's creative outputs.
          These tracks are chosen through a blend of algorithmic selection and manual refinement, ensuring a diverse
          and fair competition field every season. Crucially, the leaderboard for these tracks is exclusively shaped
          by performances within the current season.
        </p>
        <p>Players must secure at least one top time on these tracks to be featured on the rankings, guaranteeing that
          the leaderboard remains vibrant and current. This approach encourages continuous
          participation, as there's always the potential to improve on at least one of the 30 tracks, offering
          everyone—from newcomers to seasoned racers—a shot at bettering their standings. Moreover, this system naturally
          phases out dated achievements, particularly from those no longer actively competing, by focusing on recent
          contributions and successes. This ensures that even well-established tracks maintain their competitive edge,
          keeping the leaderboard fresh and reflective of active community engagement.
        </p>

        <p>A heartfelt appreciation goes to Mr. Persister &lt;3, whose boundless enthusiasm and meticulous testing of the
          Community Tracks have greatly enriched our seasonal dynamics. The invaluable insights gained from our extensive
          discussions and collaborations have directly influenced the seasonal offerings, enhancing the competitive spirit
          and community involvement on the website.
        </p>

        <p>This iteration aims to more accurately represent the significance of current season performances in shaping
          the leaderboard, emphasizing the ongoing opportunities for improvement and the dynamic nature of competition
          on the website.
        </p>
      </FAQCard>
      <FAQCard
        title="Why do I see myself in the Community Rankings only when toggling 'Show Excluded Players'"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'excluded-from-season'"
      >
        <p>In the Community Season rankings, the primary focus is on highlighting the contributions of players
          actively engaged with the current season. This means the leaderboard prominently displays players who
          have secured at least one top time on the 30 designated tracks for the current season, crucially with
          that time being achieved within the season itself. This method ensures the leaderboard is a reflection
          of the current season's active competition, emphasizing recent efforts and achievements.
        </p>

        <p>The 'Show Excluded Players' toggle offers an expanded view, including all players who have logged
          a time on these tracks, regardless of when. This broader view is particularly relevant for understanding
          the entire scope of competition and for acknowledging all contributions to the community tracks over time.
        </p>

        <p>Importantly, once a player records a new top time on any of the current season's tracks within the
          season's timeframe, not only is this new time considered, but all of their previous times on the season's
          tracks are also counted in the rankings. This means that a player does not need to update every single
          one of their times to be reflected in the current standings. A single new time within the season's parameters
          is sufficient to validate and include all their past performances on the season's tracks in the leaderboard.
          This approach is designed to encourage ongoing participation without necessitating the exhaustive effort of
          surpassing every previous record a player holds.
        </p>

        <p>This system acknowledges and rewards both current season activity and past achievements, offering
          a comprehensive and motivating view of each player's standing within the community. It underlines the
          value of consistent participation across seasons, ensuring that the leaderboard captures the full spectrum
          of engagement and success within the competitive landscape.
        </p>
      </FAQCard>
    </div>
  </q-page>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import Masonry from 'masonry-layout'
import FAQCard from 'components/FAQCard.vue'
import { useRouter } from 'vue-router';
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "FAQ - Frequently Asked Questions",
  meta: {
    description: {
      name: 'description',
      content: `Do you have a question about the leaderboard website? Here you will find anything you want to know.`
    }
  }
})

const router = useRouter()
const msnry = ref(undefined)

onMounted(() => {
  msnry.value = new Masonry('.grid', {
    itemSelector: '.grid-item',
    columnWidth: '.grid-sizer',
    percentPosition: true,
    transitionDuration: '0.2s',
    gutter: 10
    // fitWidth: true
  });
})
</script>

<style lang="sass" scoped>
.q-table thead th,
.q-table tbody td
  font-size: 16px

.grid-sizer,
.grid-item
  width: 30%

@media (max-width: 1700px)
  .grid-sizer,
  .grid-item
    width: 49%

@media (max-width: 1200px)
  .grid-sizer,
  .grid-item
    width: 100%


.grid-item
  margin: 0
  margin-bottom: 10px

</style>
