<template>
  <!-- 2.11.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.11.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-19 03:40') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>Preparatory Updates:</b> Certain data architecture improvements have been made related to player data relations. This groundwork is in anticipation of substantial features coming in the near future.</li>
        <li><b>New Points Calculation System:</b> A new method of player points calculation is implemented in this update. The system calculates points using a logarithmic relationship between their position milliseconds and the first position milliseconds. This calculation method is designed to offer a better balance of competition and reward for consistent high-level play.</li>
      </ul>
      <div class="text-h6">Updates</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>Updated FAQs:</b> The FAQ section has been updated to reflect the new points calculation system. Check it out for a more extensive understanding of how points are awarded.</li>
      </ul>
      <div class="text-h6">Bug Fixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>Duration Display Correction:</b> A bug in the news section where the duration was displayed incorrectly has been fixed. Now the duration values should represent time periods accurately.</li>
        <li><b>Improved Player Name Encoding:</b> A fix was introduced to handle special characters in player names. This will ensure that the player names are transmitted correctly. This was especially an issue for some players that had a "|" in their name.</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
