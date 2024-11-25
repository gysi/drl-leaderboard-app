<template>
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
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
