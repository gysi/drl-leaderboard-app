<template>
  <q-page class="q-pa-md">
    <div class="grid">
      <div class="grid-sizer"></div>
      <FAQCard
        title="How does the leaderboard track point system work?"
        :msnry="msnry"
        :initially-expanded="this.$router.currentRoute.value.query.card === 'pointSystem'"
      >
        <div>The number of points a player receives is calculated based on their position on the leaderboard using the formula</div>
        <b><span>points = 101 - position</span></b>
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
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 50</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 25</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 10</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 5</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 3</td>
              <td>+2</td>
            </tr>
            <tr>
              <td>= 1</td>
              <td>+2</td>
            </tr>
            </tbody>
          </q-markup-table>
        </div>
        <span>The final score is then raised to the power of <b>1.1</b></span>
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
              <td>(100 + 26)<sup>1.1</sup> = <b>204.365...</b></td>
            </tr>
            <tr>
              <td>2</td>
              <td>(99 + 24)<sup>1.1</sup> = <b>199.019...</b></td>
            </tr>
            <tr>
              <td>3</td>
              <td>(98 + 24)<sup>1.1</sup> = <b>197.240...</b></td>
            </tr>
            <tr>
              <td>4</td>
              <td>(97 + 22)<sup>1.1</sup> = <b>191.911...</b></td>
            </tr>
            <tr>
              <td>5</td>
              <td>(96 + 22)<sup>1.1</sup> = <b>190.138...</b></td>
            </tr>
            <tr>
              <td>10</td>
              <td>(91 + 20)<sup>1.1</sup> = <b>177.768...</b></td>
            </tr>
            <tr>
              <td>25</td>
              <td>(76 + 15)<sup>1.1</sup> = <b>142.871...</b></td>
            </tr>
            <tr>
              <td>50</td>
              <td>(51 + 10)<sup>1.1</sup> = <b>92.015...</b></td>
            </tr>
            <tr>
              <td>75</td>
              <td>(26 + 5)<sup>1.1</sup> = <b>43.701...</b></td>
            </tr>
            <tr>
              <td>100</td>
              <td>1<sup>1.1</sup> = <b>1</b></td>
            </tr>
            </tbody>
          </q-markup-table>
        </div>
        <span>Points are rounded to a whole number for display purposes. However, the total points in the overall ranking is accurate to 14 digits and is only then rounded.</span>
      </FAQCard>
      <FAQCard
        title="Invalid Runs"
        :msnry="msnry"
        :initially-expanded="this.$router.currentRoute.value.query.card === 'invalidRuns'"
      >
        <p>
          Invalid runs refer to attempts by a player that do not earn any points and are not considered
          as completed tracks. These runs are displayed in a separate column in the overall rankings and
          are highlighted with a red background and a warning sign in the track and player leaderboards.
          Hovering over the warning sign will display the reason for the invalidation.</p>

        <p>There are four possible reasons for a run to be flagged as invalid:</p>
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
      </FAQCard>
      <FAQCard
        :msnry="msnry"
        :initially-expanded="this.$router.currentRoute.value.query.card === 'beatenBy'"
      >
        <template v-slot:title>
          <div class="text-h6">
            "Beaten by" column
            (<q-icon name="query_stats" color="white" size="md" />)
            on player leaderboard
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
        :initially-expanded="this.$router.currentRoute.value.query.card === 'tournamentPointSystem'"
      >
        The tournament points are based on the DRL irl point system and only the best 12 tournaments for each player are counted.
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
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import Masonry from 'masonry-layout'
import FAQCard from 'components/FAQCard.vue'

export default defineComponent({
  name: 'FAQPage',
  components: {
    FAQCard
  },
  data () {
    return {
      console: console,
      msnry: undefined
    }
  },
  mounted () {
    let msnry = new Masonry('.grid', {
      itemSelector: '.grid-item',
      columnWidth: '.grid-sizer',
      percentPosition: true,
      transitionDuration: '0.2s',
      gutter: 10
      // fitWidth: true
    });
    this.msnry = msnry;
  }
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
