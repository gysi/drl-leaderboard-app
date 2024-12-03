<template>
  <!-- 3.7.2 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Prize Pool Update & Fix</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-05 09:35') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        The prize pool got updated, another contribution by THE_BOB!!: $50
      </div>
      <div class="text-h6">Fixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>When Community Season tracks are added to the Featured section,
          they don't count towards the official track rankings.
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
