<template>
  <!-- 3.4.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">3.4.0 Release - Better Replay Viewer #3</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-04-08 04:42') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Its now possible to move arround with wasd within the replay viewer
        </li>
      </ul>
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>It was possible to zoom in too much and then panning using the right mouse was too slow
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
