<template>
  <!-- 1.9.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">1.9.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-16 03:00') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features & Bugfixes (Replay Viewer)</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Improved performance of the replay viewer
        </li>
        <li>If you select a new track, the old track is now removed from the replay viewer
        </li>
        <li>Lines are now thicker and have a depth due to added lightning
        </li>
        <li>If you select 2nd, 3rd.. player, the race line has a different color
        </li>
        <li>Checkpoint times are now in the correct format and don't overlap anymore (except when two checkpoints overlap)
        </li>
        <li>When a lap ends the checkpoint time has the label 'Lap' after the time.
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
