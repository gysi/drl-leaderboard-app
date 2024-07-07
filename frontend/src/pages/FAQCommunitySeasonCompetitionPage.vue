<template>
  <q-page class="q-pa-md">
    <div class="grid">
      <div class="grid-sizer"></div>
      <FAQCard
        title="How can I compete and join the Summer Season 24 competition?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'how-to-compete'"
      >
        You will join the competition automatically by flying the community season tracks.
        <br/>
        You will find the tracks in the navigation within the Community Season section.
        <br/>
        Additionally you should create an account on matcherino.com and join the competition
        <a href="https://matcherino.com/tournaments/116263" target="_blank">https://matcherino.com/tournaments/116263</a>
      </FAQCard>
      <FAQCard
        title="How do I find the tracks within the DRL Simulator?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'where-are-the-tracks-in-the-sim'"
      >
        You will find the tracks in the featured section (Fly -> Solo Race -> Featured Tracks).
        <br/><br/>
        Or from the main menu click on Fly -> Solo Race -> Community Tracks.
        <br/>
        Then search for the tracks by typing the name in the searchbar.
        <br/>
        On this website here, you find the track names within the navigation in the community season section under "Tracks".
      </FAQCard>
      <FAQCard
        title="Are tournaments also relevant for the competition?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'are-tournaments-relevant'"
      >
        No, the tournaments are divided into seasons, just like the community season ranking, but tournaments don't play a role in the current competition.
      </FAQCard>
      <FAQCard
        title="What can I win?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'what-can-i-win'"
      >
        When the season ends, the top 5 players on the Community Season Ranking win money from the prize pool:
        <ul>
          <li>1st Place: $474</li>
          <li>2nd Place: $338</li>
          <li>3rd Place: $230</li>
          <li>4th Place: $176</li>
          <li>5th Place: $135</li>
        </ul>
        The money for the prize pool got sponsored from passionate players:
        <ul>
          <li>gysi: $575</li>
          <li>FPVgan: $250</li>
          <li>itsLeeFPV: $200</li>
          <li>THE_BOB!!: $200</li>
          <li>TrippFPV: $100</li>
          <li>Str33tz: $20</li>
          <li>TheSauceror: $10</li>
        </ul>
      </FAQCard>
      <FAQCard
        title="How do I get the money?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'how-do-i-get-the-money'"
      >
        gysi manages the prize pool fund through Matcherino. He will message the winners about the details on Discord.
        <br/>
        If you are in the DRL Discord and your name on discord is somewhat like your name within the game, he will find you.
        <br/>
        Please create an Account on Matcherino and join the competition, if you are one of the winners you will
        get assigned to your winning position on Matcherino and can claim the money. <br/>
        <a href="https://matcherino.com/tournaments/116263" target="_blank">https://matcherino.com/tournaments/116263</a>
      </FAQCard>
      <FAQCard
        title="How can I contribute to the prize pool?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'how-to-contribute'"
      >
        gysi manages the prize pool fund through Matcherino, you can contribute there.
        <br/>
        <a href="https://matcherino.com/tournaments/116263" target="_blank">https://matcherino.com/tournaments/116263</a>
      </FAQCard>
      <FAQCard
        title="When does the Season end?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'when-does-the-season-end'"
      >
        The season ends exactly on
        <br/>
        {{ toLocalDateformat('2024-09-01 00:00:00') }}.
        <br/><br/>
        The top 5 players on the Community Season Ranking page are then the winners.
      </FAQCard>
      <FAQCard
        title="I currently only see X tracks, will there be more?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'when-does-the-season-end'"
      >
        There will be 25 tracks in total. The season started with 5 tracks.
        <br/>
        The tracks are divided into series:
        <br/>
        <ul>
          <li><b>🔵 Blue Series - Beginner (30-40 seconds)</b><br/>
            Basic elements, chicane + slalom, gentle turns, lots of space between gates.<br/>
            Release: {{ toLocalDateformat('2024-06-01 00:00:00') }}
          </li>
          <li><b>🟢 Green Series - Easier Intermediate (40-50 seconds)</b><br/>
            Easy elements, split S/Immelmann, sweeper/blaster tunnels.<br/>
            Release: {{ toLocalDateformat('2024-06-08 00:00:00') }}
          </li>
          <li><b>🟡 Yellow Series - Intermediate (50-60 seconds)</b><br/>
            Intermediate elements, inversions/gravity elements, dive gates, hairpin turns.<br/>
            Release: {{ toLocalDateformat('2024-06-15 00:00:00') }}
          </li>
          <li><b>🟠 Orange Series - Easier Advanced (60-70 seconds)</b><br/>
            Intro to technical elements, ladders, corkscrews, hurdles.<br/>
            Release: {{ toLocalDateformat('2024-06-22 00:00:00') }}
          </li>
          <li><b>🔴 Red Series - Advanced (70-80 seconds)</b><br/>
            All elements, technical, precision, gravity.<br/>
            Release: {{ toLocalDateformat('2024-06-29 00:00:00') }}
          </li>
        </ul>
      </FAQCard>
      <FAQCard
        title="What counts as penality?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'bounce-penalty'"
      >
        To maintain fairness and authenticity in the competition, a penalty of 1 second will be applied for each significant bounce detected in a player's run. This decision was made following community feedback and a poll regarding the use of bounce techniques.
        <br/><br/>
        <b>What is a bounce?</b>
        <br/>
        In the context of the DRL Simulator, a bounce occurs when a drone hits a surface, such as the ground or a wall, and gains a speed boost from the collision. This can happen when the drone's angle and velocity allow it to ricochet off the surface, resulting in an unintended acceleration.
        <br/><br/>
        <b>How does it work?</b>
        <ul>
          <li>An algorithm analyzes replays to detect bounces that would result in damage. Bounces with very low angles or velocities are not counted.</li>
          <li>If a bounce is detected, a 1-second penalty is added to the player's time for each bounce.</li>
          <li>This penalty ensures that exploiting bounces for speed boosts is not beneficial.</li>
        </ul>
        <br/>
        The penalty system aims to discourage the use of bounce techniques while still acknowledging runs that adhere to the game's mechanics.
        <br/><br/>
        <b>Feedback and Adjustments:</b>
        <ul>
          <li>The algorithm is continuously refined to reduce false positives. Players are encouraged to report any discrepancies they observe.</li>
          <li>Runs with penalties will be highlighted on the leaderboard for easy identification.</li>
        </ul>
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
import {utcToZonedTime} from "date-fns-tz";
import {format} from "date-fns";

useMeta({
  title: "Community Season Competition FAQ",
  meta: {
    description: {
      name: 'description',
      content: `Do you have a question about the Community Summer Season 2024 competition? Here you will find anything you want to know.`
    }
  }
})

const router = useRouter()
const msnry = ref(undefined)

const toLocalDateformat = (val) => {
  if (val) {
    const date = new Date(val+'Z')
    const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone
    const zonedDate = utcToZonedTime(date, userTimezone);
    const pattern = 'yyyy-MM-dd HH:mm:ss'
    return format(zonedDate, pattern, { timeZone: userTimezone }) + ' ' + userTimezone
  }
};


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
