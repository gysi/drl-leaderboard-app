<template>
  <!-- 3.9.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">🚨 Summer Season Heats Up: Red Series Tracks & New Bounce Penalty 🚨</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-07-07 03:20') }}</div> </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        The Community Summer Season is reaching its peak, and we've got a double dose of excitement to share!
        <br/><br/>
        <div class="text-h6">🔥 Red Series Unleashed! 🔥</div>
        Get ready to test your piloting mettle with the final five tracks of the season, the <b>Red Series</b>. Designed for the most advanced pilots, these tracks are a technical tour de force, featuring:
        <ul>
          <li>[SUM24-21] HOT LAP, by NoSkillSammy</li>
          <li>[SUM24-22] LAST ROUND, by Shaigne</li>
          <li>[SUM24-23] ROASTED AND TOASTED, by WestFPV</li>
          <li>[SUM24-24] CRUNCH TIME, by GreenMachine</li>
          <li>[SUM24-25] HEAD IN THE SAND, by Mr. Persister</li>
        </ul>
        Expect a symphony of gravity elements, precision turns, and every other challenge the track designers could throw your way. It's time to leave it all on the track!
        <br/><br/>
        <div class="text-h6">⚠️ Bounce Penalty Incoming ⚠️</div>
        Following extensive community discussion and a poll, we're introducing a new bounce penalty system to ensure fair play and discourage unintended speed boosts. Here's the lowdown:
        <ul>
          <li><b>What's a Bounce?</b> A bounce occurs when your drone hits a surface and gains a speed advantage.</li>
          <li><b>The Penalty:</b> A 1-second penalty will be added to your time for each significant bounce detected in your replay.</li>
          <li><b>Why the Change?</b> To promote skillful piloting and maintain the integrity of the competition.</li>
          <li><b>How It Works:</b> A smart algorithm analyzes replays for bounces that would cause damage. Minor bumps won't be penalized.</li>
          <li><b>Transparency:</b> Runs with penalties will be clearly marked on the leaderboards.</li>
        </ul>
        We're committed to refining the algorithm to minimize false positives. If you believe a penalty was applied in error, please report it.
        <br/><br/>
        <div class="text-h6">🚀 Race On!</div>
        We can't wait to see you master the Red Series tracks and adapt to the new penalty system. The competition is fiercer than ever, so give it your all and make your mark on the Summer Season!
        <br/><br/>
        <div class="text-h6">Additional Notes:</div>
        <ul>
          <li><b>FAQ Update:</b> Check out the <q-btn type="a" flat padding="0" :to="{ name: 'communitySeasonCompetitionFaq' }">FAQ</q-btn> section on the website for a detailed breakdown of the bounce penalty system.</li>
          <li><b>Community Feedback:</b> As always, we value your feedback! Share your thoughts on the new tracks and penalty system on the DRL Discord.</li>
        </ul>
        Let's see who will claim victory in the final stretch of the Community Summer Season! 🏆
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
