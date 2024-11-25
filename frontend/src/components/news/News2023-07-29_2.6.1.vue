<template>
  <!-- 2.6.1 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.6.1 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-07-29 04:12') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds new invalid run reason "WRONG_DRONE".
        </li>
        <li>Adds ability to manually flag runs as invalid. If you notice a run that is invalid, please contact "gysi" on Discord.
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
