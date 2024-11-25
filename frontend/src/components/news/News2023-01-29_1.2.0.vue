<template>
  <!-- 1.2.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">1.2.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-29 24:00') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>You can click on the content card of the slotmachine</li>
        <li>There has been a button added to the slotmachine page to select the different "worst" categories you want to have within the track pool</li>
        <li>All settings you make on the slotmachine page are saved so that they are still there on page reload</li>
      </ul>
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Fixes some logic bugs where you could play the slotmachine without having tracks loaded</li>
      </ul>
      <div class="text-h6">Improvements</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Slotmachine content cards are now fancy</li>
        <li>Some general styling improvements regarding the slotmachine</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
