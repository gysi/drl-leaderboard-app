<template>
  <!-- 1.4.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">1.4.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-01 01:06') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds ability to compare against other player times within the player ranking section</li>
      </ul>
      <div class="text-h6">Optimizations</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Show all tracks on the first page in the tracks section</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
