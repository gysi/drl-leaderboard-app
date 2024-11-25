<template>
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
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
