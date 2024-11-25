<template>
  <!-- 0.5.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">0.5.0 Release (PINK IS THE DEVIL)</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-17 02:11') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>The main theme of the page is now pink (If you don't like it then send your complaint to Devils)</li>
      </ul>
      <div class="text-h6">Improvements</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Better styling separation. One step closer to an easy configurable theme.
          Darkmode is a little bit tricky though. For now darkmode is disabled</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
