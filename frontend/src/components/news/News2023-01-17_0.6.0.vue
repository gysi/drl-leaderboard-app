<template>
  <!-- 0.6.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">0.6.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 17:01') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Advanced search highlighting within track leaderboards</li>
      </ul>
      <div class="text-h6">Performance</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds caches for statistics which are requested on the 'home' page</li>
        <li>The ajax calls on the homepage are now executed in parallel</li>
        <li>Limits mostPbsLast7Days/Month to 10 because the frontend only shows 10 entries</li>
      </ul>
      <div class="text-h6">Misc</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds default "catch all" nginx server block which avoids mapping of the ip address
          to the first server block. Adds selfsigned ssl certificate for this, to catch ssl requests too</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
