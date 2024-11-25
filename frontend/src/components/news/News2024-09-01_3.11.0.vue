<template>
  <!-- 3.11.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Summer Season 2024 Concludes! Winners Announced!</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-09-01 02:55') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        We hope everyone has had an amazing summer, flying high and exploring the maps from our Summer Series.
        It’s been an exciting season, with incredible skill and fierce competition on display.
        Pilots have been pushing their limits, and some battles have gone right down to the wire as they fought
        to secure their spots on the leaderboard!
        <br/><br/>

        <b>Now, the moment you've all been waiting for: the final standings and our prize winners!</b>
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>🏆 1st: <b>gysi OG - $509</b></li>
          <li>🥈 2nd: <b>THE_BOB!! - $363</b></li>
          <li>🥉 3rd: <b>WestFPV - $247</b></li>
          <li>4th: <b>Green OG - $189</b></li>
          <li>5th: <b>XBR Gilbert - $145</b></li>
        </ul>

        Congratulations to all our winners! Your dedication, skill, and passion have truly made
        this season one to remember. We’ve seen some incredible flying, and the level of competition
        has been nothing short of spectacular.
        <br/><br/>
        A special shoutout to <b>LSDTripp</b> for the amazing last-minute fight with <b>XBR Gilbert</b> battling for
        5th place, it was amazing to watch.
        <br/><br/>
        But don’t hang up your radios just yet! We’re cooking up something special for the next chapter
        in our journey — the Fall Series. With new maps, challenges, and even more prizes on the horizon,
        this is one season you won’t want to miss. Keep your eyes peeled for more details in the coming weeks!
        <br/><br/>
        A massive thank you to <b>Mr. Persister</b> and all our amazing track creators for their tireless efforts
        in organizing this season. Your creativity and dedication have brought these tracks to life, providing
        endless hours of enjoyment for our community. We also extend our gratitude to <b>FPVgan</b> for his invaluable
        assistance and to the track reviewers for ensuring the quality and fairness of the competition.
        <br/><br/>
        Thank you to everyone who participated and supported the Community Mapping Project. Whether you were
        on the leaderboard or cheering from the sidelines, you’ve all played a part in making this season a success.
        We can’t wait to see what the future holds!
        <br/><br/>
        Stay tuned, and keep those drones ready for action!
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
