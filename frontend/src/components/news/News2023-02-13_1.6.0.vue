<template>
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
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
