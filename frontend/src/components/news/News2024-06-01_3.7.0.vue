<template>
  <!-- 3.7.0 Release - Summer Season 2024 Start -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">🏁 Summer Season 2024 Has Begun! 🏁</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-01 01:40') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="text-h6">Info:</div>
      <div style="font-size: 1rem">
        The highly anticipated Community Summer Season 2024 is here, and this time we have something truly special lined up. Instead of racing on existing community tracks, we've collaborated with nine exceptional track designers to bring you an exclusive map pack designed just for this season.
        <br/><br/>
        <b>🏆 Prize Pool 🏆</b><br/>
        We are excited to announce a prize pool for the top pilots this season:
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>1st Place: $470</li>
          <li>2nd Place: $336</li>
          <li>3rd Place: $228</li>
          <li>4th Place: $174</li>
          <li>5th Place: $134</li>
        </ul>
        You can also contribute to the prize pool! Any contributions will increase the total prize pool proportionally across all placings based on the current distribution.
        <br/><br/>
        <b>💸 Prize Pool Contributors 💸</b>
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>gysi: $575</li>
          <li>FPVgan: $250</li>
          <li>itsLeeFPV: $200</li>
          <li>THE_BOB!!: $200</li>
          <li>TrippFPV: $100</li>
          <li>Str33tz: $20</li>
        </ul>
        Our talented track designers for this season are:
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>FPVgan</li>
          <li>Green Machine</li>
          <li>Jab</li>
          <li>Mr. Persister</li>
          <li>NoSkillSammy</li>
          <li>Shaigne</li>
          <li>Swoop</li>
          <li>TrippFPV</li>
          <li>WestFPV</li>
        </ul>
        These creators, led by the meticulous planning and organization of Mr. Persister, have developed a series of unique tracks that promise to challenge and engage pilots of all skill levels. The project kicked off with a vision to create 25 brand new tracks across five community maps, each offering varied difficulty and exciting new racing lines.
        <br/><br/>
        Tracks will be released in batches throughout the season (every week), and each one is designed to push your skills to the limit while ensuring a fun and thrilling experience.
        <br/><br/>
        We have organized the tracks into series based on difficulty, each marked with a distinct color. The first to be released is the Blue Series, designed for beginners with 30-40 second tracks featuring basic elements such as chicanes, slaloms, and gentle turns. Here's a breakdown of the series and what to expect:
        <ul class="q-my-sm" style="font-size: 1rem">
          <li><b>🔵 Blue Series - Beginner (30-40 seconds)</b><br/>
            Basic elements, chicane + slalom, gentle turns, lots of space between gates.
          </li>
          <li><b>🟢 Green Series - Easier Intermediate (40-50 seconds)</b><br/>
            Easy elements, split S/Immelmann, sweeper/blaster tunnels.
          </li>
          <li><b>🟡 Yellow Series - Intermediate (50-60 seconds)</b><br/>
            Intermediate elements, inversions/gravity elements, dive gates, hairpin turns.
          </li>
          <li><b>🟠 Orange Series - Easier Advanced (60-70 seconds)</b><br/>
            Intro to technical elements, ladders, corkscrews, hurdles.
          </li>
          <li><b>🔴 Red Series - Advanced (70-80 seconds)</b><br/>
            All elements, technical, precision, gravity.
          </li>
        </ul>
        <div style="font-size: 1.2rem; font-weight: bold; color: #FF5733;">
          A HUGE SHOUTOUT and THANK YOU to everyone who made this possible
        </div>
        <div style="font-size: 1.2rem; font-weight: bold; color: #FF5733;">
          Your dedication and creativity are what make our community thrive!
        </div>
        Stay tuned for more updates and get ready to test your piloting skills on these innovative new tracks. Happy racing!
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
