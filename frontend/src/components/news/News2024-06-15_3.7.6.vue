<template>
  <!-- 3.7.6 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Summer Season Competition - Yellow Series Release!</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-15 02:15') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        We are thrilled to continue our Summer Season competition with the release of our next series of tracks. 🌞🎉
        <br/><br/>
        After the launch of the Green series, it's time to introduce the Yellow Series!
        We have an incredible lineup of tracks that we can't wait for you to explore and enjoy.
        Here are the tracks being released this week:
        <ul>
          <li>[SUM24-11] DRL HEADQUARTERS</li>
          <li>[SUM24-12] KING'S COURT</li>
          <li>[SUM24-13] SANDSTORM</li>
          <li>[SUM24-14] SHADOWS AND DUST</li>
          <li>[SUM24-15] COOL BREEZE</li>
        </ul>

        A huge shoutout and thanks to our talented track creators for their amazing work:
        <ul>
          <li>FPVgan</li>
          <li>Jab</li>
          <li>Mr. Persister</li>
          <li>Shaigne</li>
          <li>WestFPV</li>
        </ul>

        Best regards, <br/>
        Your Community Team
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
