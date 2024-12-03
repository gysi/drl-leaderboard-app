<template>
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
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
