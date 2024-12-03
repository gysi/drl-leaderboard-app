<template>
  <!-- 3.7.8 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Summer Season - Orange Series Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-29 03:33') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        We are thrilled to continue our Summer Season competition with the release of our next series of tracks. 🌞🎉
        <br/><br/>
        After the launch of the Yellow Series, it's time to introduce the Orange Series!
        We have an incredible lineup of tracks that we can't wait for you to explore and enjoy.
        Here are the tracks being released this week:
        <ul>
          <li>[SUM24-16] SUMMERFELL (by FPVgan)</li>
          <li>[SUM24-17] ATHENA''S RAMPAGE (by Swoop)</li>
          <li>[SUM24-18] NIGHTWATCH (by Jab)</li>
          <li>[SUM24-19] CUBIC TRIPP (by TrippFPV)</li>
          <li>[SUM24-20] AFTER WORK (by Shaigne)</li>
        </ul>
        A huge shoutout and thanks to our talented track creators for their amazing work.

        Best regards, <br/>
        Your Community Team
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
