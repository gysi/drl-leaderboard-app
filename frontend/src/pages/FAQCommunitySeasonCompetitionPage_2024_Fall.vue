<template>
  <q-page class="q-pa-md">
    <div class="grid">
      <div class="grid-sizer"></div>
      <FAQCard
        title="How can I compete and join the Fall Season 24 competition?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'how-to-compete'"
      >
        To participate in the Fall 2024 season, you must register on the Matcherino site.
        <br/>
        First, join the qualification by flying the community season tracks.
        <br/>
        After qualifying, you will compete in the final tournament at the end of the season.
        <br/>
        <strong>Important:</strong> If you don't register on Matcherino, you will still appear in the leaderboard rankings but
        you won't be eligible to participate in the final tournament.
        <br/>
        <a :href="currentSeasonDetails?.matcherino?.matcherinoEventLink" target="_blank">
          Register here on Matcherino
        </a>
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
        No, the tournaments are divided into seasons, just like the community season ranking,
        but normal tournaments don't play a role in the current competition.
        <br/><br/>
        However, there is one tournament that is of particular significance: the Grand Final, which marks the conclusion of the season.
      </FAQCard>
      <FAQCard
        v-if="currentSeasonDetails?.hasPrizePool"
        title="What can I win?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'what-can-i-win'"
      >
        The top 6 players on the Grand finals win money from the prize pool:
        <ul>
          <li v-for="(prize, i) in currentSeasonDetails?.prizePool" :key="i">
            {{ i+1 }}{{ i === 0 ? 'st' : 'nd' }} Place: {{ prize }}
          </li>
        </ul>
        The money for the prize pool got sponsored from passionate players:
        <ul>
          <li>gysi: $1000</li>
        </ul>
      </FAQCard>
      <FAQCard
        v-if="currentSeasonDetails?.hasPrizePool"
        title="How do I get the money?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'how-do-i-get-the-money'"
      >
        gysi manages the prize pool fund through Matcherino. He will message the winners about the details on Discord.
        <br/>
        If you are in the DRL Discord and your name on discord is somewhat like your name within the game, he will find you.
        <br/>
        Create an Account on Matcherino and join the competition, if you are one of the winners you will
        get assigned to your winning position on Matcherino and can claim the money. <br/>
        <a :href="currentSeasonDetails?.matcherino?.matcherinoEventLink" target="_blank">{{ currentSeasonDetails?.matcherino?.matcherinoEventLink }}</a>
      </FAQCard>
      <FAQCard
        v-if="currentSeasonDetails?.hasPrizePool"
        title="How can I contribute to the prize pool?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'how-to-contribute'"
      >
        gysi manages the prize pool fund through Matcherino, you can contribute there.
        <br/>
        <a :href="currentSeasonDetails?.matcherino?.matcherinoEventLink" target="_blank">{{ currentSeasonDetails?.matcherino?.matcherinoEventLink }}</a>
      </FAQCard>
      <FAQCard
        title="When does the Season end?"
        :msnry="msnry"
        :initially-expanded="router.currentRoute.value.query.card === 'when-does-the-season-end'"
      >
        The season ends with the grand finals on
        <br/>
        {{ toLocalDateformat(currentSeason.endDate) }}.
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
            Release start: {{ toLocalDateformat('2024-09-15 00:00:00') }}<br/>
            Release end: {{ toLocalDateformat('2024-09-22 00:00:00') }}
          </li>
          <li><b>🟢 Green Series - Easier Intermediate (40-50 seconds)</b><br/>
            Easy elements, split S/Immelmann, sweeper/blaster tunnels.<br/>
            Release start: {{ toLocalDateformat('2024-09-24 00:00:00') }}<br/>
            Release end: {{ toLocalDateformat('2024-10-03 00:00:00') }}
          </li>
          <li><b>🟡 Yellow Series - Intermediate (50-60 seconds)</b><br/>
            Intermediate elements, inversions/gravity elements, dive gates, hairpin turns.<br/>
            Release start: {{ toLocalDateformat('2024-10-06 00:00:00') }}<br/>
            Release end: {{ toLocalDateformat('2024-10-15 00:00:00') }}
          </li>
          <li><b>🟠 Orange Series - Easier Advanced (60-70 seconds)</b><br/>
            Intro to technical elements, ladders, corkscrews, hurdles.<br/>
            Release start: {{ toLocalDateformat('2024-10-17 00:00:00') }}<br/>
            Release end: {{ toLocalDateformat('2024-06-27 00:00:00') }}
          </li>
          <li><b>🔴 Red Series - Advanced (70-80 seconds)</b><br/>
            All elements, technical, precision, gravity.<br/>
            Release start: {{ toLocalDateformat('2024-10-29 00:00:00') }}<br/>
            Release end: {{ toLocalDateformat('2024-11-05 00:00:00') }}
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
import {nextTick, onMounted, ref, shallowRef, watch} from 'vue'
import Masonry from 'masonry-layout'
import FAQCard from 'components/FAQCard.vue'
import { useRouter } from 'vue-router';
import {useMeta} from "src/modules/meta.js"
import {utcToZonedTime} from "date-fns-tz";
import {format} from "date-fns";
import {fetchCurrentSeason} from "src/modules/backendApi.js";

useMeta({
  title: "Community Season Competition FAQ",
  meta: {
    description: {
      name: 'description',
      content: `Do you have a question about the Community Fall Season 2024 competition? Here you will find anything you want to know.`
    }
  }
})

const router = useRouter()
const msnry = ref(undefined)
const currentSeason = shallowRef({})
const currentSeasonDetails = shallowRef({})

const toLocalDateformat = (val) => {
  if (val) {
    const date = new Date(val+'Z')
    const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone
    const zonedDate = utcToZonedTime(date, userTimezone);
    const pattern = 'yyyy-MM-dd HH:mm:ss'
    return format(zonedDate, pattern, { timeZone: userTimezone }) + ' ' + userTimezone
  }
};

const initializeMasonry = () => {
  msnry.value = new Masonry('.grid', {
    itemSelector: '.grid-item',
    columnWidth: '.grid-sizer',
    percentPosition: true,
    transitionDuration: '0.2s',
    gutter: 10
  });
};

const resetMasonryLayout = () => {
  if (msnry.value) {
    msnry.value.destroy(); // Destroy the existing Masonry instance
  }
  nextTick(() => {
    initializeMasonry(); // Re-initialize Masonry after the DOM update
  });
};

onMounted(() => {
  fetchCurrentSeason().then(data => {
    currentSeason.value = data;
    currentSeasonDetails.value = data.details_v1;
    console.log(currentSeason.value);

    nextTick(() => {
      initializeMasonry();
    });
  });
});

watch(
  () => currentSeasonDetails.value?.hasPrizePool,
  (newVal, oldVal) => {
    if (newVal !== oldVal) {
      resetMasonryLayout(); // Re-layout Masonry when hasPrizePool changes
    }
  }
);


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
