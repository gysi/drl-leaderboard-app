<template>
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
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
