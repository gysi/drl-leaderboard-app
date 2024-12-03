<template>
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
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
