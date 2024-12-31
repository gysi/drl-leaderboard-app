<template>
  <!-- 4.0.7 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">🏆 Grand Finals Results: Congratulations to the Winners! 🎉</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-12-09 04:45') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        <p>The <b>DRL Community Fall Season 2024 Grand Finals</b> has officially concluded, and what an incredible event it was! After months of thrilling racing and intense competition, we are proud to announce the winners:</p>

        <div class="text-h6">🥇 Final Standings:</div>
        <ol>
          <li><b>THE B0B!!</b> - Champion!</li>
          <li><b>gysi OG</b> - Runner-up!</li>
          <li><b>HaloWalker</b> - Third Place!</li>
          <li>WestFPV</li>
          <li>Mr. Persister</li>
          <li>2018 Toyota Tacoma</li>
        </ol>
        <p>Congratulations to <b>THE B0B!!</b> for securing the championship title and to all the top pilots for their phenomenal performance. You have made this season unforgettable!</p>

        <div class="text-h6">💬 A Message from the Team:</div>
        <p>We want to thank everyone who participated, contributed, and cheered on the pilots throughout this season. Your support has been incredible, and we couldn’t have done this without the amazing DRL community.</p>
        <p>As we look to the future, we’ll be taking a short break to reflect on this season and figure out how to make the next season even better. Stay tuned for updates as we plan exciting changes to enhance the competition, tracks, and overall experience for everyone involved.</p>

        <p>Enjoy the break, and we’ll see you in the skies soon for another thrilling season! 🚁</p>
        <p><b>Your Community Team</b></p>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import { getDateDifferenceToNowByGermanTimezone } from "src/modules/LeaderboardFunctions.js";
</script>
