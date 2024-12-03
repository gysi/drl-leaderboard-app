<template>
  <!-- 2.10.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.10.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-02 21:00') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>Enhanced Player Ranking Logic:</b> The latest update introduces a refined approach to determining player rankings for tournaments. When players accumulate the same points from their best 12 tournaments, rankings are now further decided by examining their overall performance across all tournaments. This ensures that players with more top (lower number) finishes are ranked higher, promoting a fairer and more comprehensive evaluation of player performance, rewarding consistency and excellence across events.</li>
        <li><b>Updated Tournament Point System FAQs:</b> The FAQ section related to the tournament point system has been updated to clearly explain the process of ranking determination. </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
