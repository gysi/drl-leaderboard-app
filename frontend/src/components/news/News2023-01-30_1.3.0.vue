<template>
  <!-- 1.3.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">1.3.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-30 02:04') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds playerSearch to overall rankings which lets you scroll to the player and highlight the row</li>
      </ul>
      <div class="text-h6">Performance</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds placerholder.png to assets and don't fetch it from the drl api</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
