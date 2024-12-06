<template>
  <!-- 4.0.6 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">💰 Prize Pool Update: $1600 Reached! 🚀</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-12-06 03:08') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        <p>We are thrilled to announce that the prize pool has grown yet again, thanks to the generous contribution of <b>phil__ppp</b> ($50.00)! This brings the total prize pool to an incredible <b>$1,600.00</b>. A huge thank you to all contributors for supporting the DRL Community Fall Season 2024.</p>

        <div class="text-h6">🏆 Updated Prize Distribution:</div>
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>1st Place: <b>$640.00</b></li>
          <li>2nd Place: <b>$400.00</b></li>
          <li>3rd Place: <b>$240.00</b></li>
          <li>4th Place: <b>$160.00</b></li>
          <li>5th Place: <b>$80.00</b></li>
          <li>6th Place: <b>$80.00</b></li>
        </ul>

        <p>The continued generosity of the community makes this season even more rewarding for our top pilots. If you’d like to join the contributors and help grow the prize pool even further, you can do so through our <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino page</a>.</p>

        <p>Stay tuned for more updates as we approach the Grand Finals. Thank you for your incredible support! 🚁</p>
        <p><b>Your Community Team</b></p>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import { getDateDifferenceToNowByGermanTimezone } from "src/modules/LeaderboardFunctions.js";
</script>
