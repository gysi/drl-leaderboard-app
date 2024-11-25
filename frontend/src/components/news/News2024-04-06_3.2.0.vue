<template>
  <!-- 3.2.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">3.2.0 Release - Better Replay Viewer</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-04-06 00:33') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>The replay viewer is now 100% accurate and shows the original meshes for checkpoint/trigger gates.
        </li>
      </ul>
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Fixes a bug where the discord leaderboard bot would post times that were old.
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
