<template>
  <!-- 3.7.3 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Summer Season Competition - Green Series Release!</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-08 02:00') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        We are thrilled to continue our Summer Season competition with the release of our next series of tracks. 🌞🎉
        <br/><br/>
        After the launch of the Blue series, it's time to introduce the Green Series! We have an incredible lineup of tracks that we can't wait for you to explore and enjoy. Here are the tracks being released this week:
        <ul>
          <li>[SUM24-06] GREEN FLAG</li>
          <li>[SUM24-07] PAPERJAM</li>
          <li>[SUM24-08] CASTLE CRUISE</li>
          <li>[SUM24-09] RIPPIN' THROUGH THE RUINS</li>
          <li>[SUM24-10] TRIPP LITE</li>
        </ul>

        A huge shoutout and thanks to our talented track creators for their amazing work:
        <ul>
          <li>FPVgan</li>
          <li>Green Machine</li>
          <li>TrippFPV</li>
          <li>Mr. Persister</li>
          <li>Shaigne</li>
        </ul>

        We also want to extend our gratitude to the creators who built those maps from scratch:
        <ul>
          <li>Flightless (Collosuem)</li>
          <li>Mr. Persister (Sand Castle)</li>
          <li>Shaigne (Solitude & Bio Modern City)</li>
          <li>XTK FPV (Office Space)</li>
        </ul>
        Thank you all for your incredible contributions and hard work.
        <br/><br/>
        Get ready to race, explore, and compete on these fantastic new tracks. We can't wait to see your best times and epic moments. Let the Green Series begin! 🏁🏆
        <br/><br/>
        Best regards, <br/>
        Your Community Team
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
