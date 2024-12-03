<template>
  <!-- 4.0.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">📢 Fall Season 2024 is in Full Swing! New Tracks, Tournament, and More! 🍂🏆</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-09-30 01:00') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        <p>We're thrilled to announce that the <b>Community Fall Season 2024</b> has officially taken flight! While the website is
          catching up, the competition is already underway, with pilots soaring through the first series of tracks. We've got a jam-packed season ahead, filled with fresh challenges, a thrilling tournament, and a growing prize pool!</p>

        <div class="text-h6">What's New This Season?</div>
        <ul>
          <li><b>Dynamic Track Releases:</b> We're shaking things up! Instead of releasing tracks in batches, we'll be unveiling new
            ones alongside the weekly tournaments. This means fresh challenges and exciting racing every week!</li>
          <li><b>Diverse Track Selection:</b> 25 brand-new tracks await, spread across five series of increasing difficulty: Blue, Green,
            Yellow, Orange, and the climactic Red Series. Each series offers a unique challenge, ensuring pilots of all skill levels can enjoy the competition.</li>
          <li><b>Grand Final Tournament:</b> The season culminates in an epic tournament on <b>Sunday, December 8th at 4 PM EST</b>. The
            top 24 pilots will battle it out in a thrilling knockout format, with the final race taking place on a <b>surprise unreleased track</b>!</li>
          <li><b>Growing Prize Pool:</b> Thanks to generous contributions from the community, the prize pool is already at $1000! Help us
            reach our $2000 goal and support the competition by contributing on our <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino page</a>.</li>
        </ul>

        <div class="text-h6">Qualify for the Grand Finals</div>
        <p>The Fall Season kicked off on September 16th, 2024, and the qualification period runs until December 1st, 00:00 UTC. Climb
          the leaderboard by mastering the 25 tracks released throughout the season. The top 24 pilots will secure their spots in the
          Grand Finals, with 5 additional backup pilots ready to step in if needed.</p>

        <div class="text-h6">Grand Final Tournament Format</div>
        <ul>
          <li><b>Standard DRL Golden Heat Structure:</b> Experience the excitement of head-to-head racing with the proven Golden Heat format.</li>
          <li><b>No Tournament Qualifier:</b> The top 24 pilots from the season leaderboard are automatically qualified.</li>
          <li><b>Three Unique Tracks:</b>
            <ul>
              <li>Quarterfinals: A random track from the Green or Yellow Series.</li>
              <li>Semifinals: A random track from the Orange or Red Series.</li>
              <li>Finals: A surprise unreleased track, revealed 1 hour before the grand finals starts!</li>
            </ul>
          </li>
          <li><b>Warmup Sessions:</b> 10-minute warmup sessions will be held between each stage to allow pilots to warm up for the next track.</li>
          <li><b>Live Stream:</b> Catch all the action live on Twitch, with expert commentary by FPVgan and Seltro!</li>
        </ul>

        <div class="text-h6">A Special Thanks</div>
        <p>A massive shoutout to our talented track creators: <b>Drifting, Flightless, FPVcircus, FPVgan, Greenmachine, gysi, Mr. Persister, Seltro, Shaigne, Solace, TrippFPV, and WestFPV</b> for their incredible work on the custom map pack. We also want to thank <b>Mr. Persister</b> for his tireless efforts in organizing the season and <b>FPVgan and Blinkin</b> for their invaluable assistance.</p>

        <div class="text-h6">Get Involved!</div>
        <ul>
          <li><b>Compete:</b> Register on <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino</a> and start flying those tracks!</li>
          <li><b>Contribute:</b> Help us grow the prize pool and support the community on <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino</a>.</li>
          <li><b>Watch:</b> Tune in to the weekly tournaments and cheer on your favorite pilots.</li>
          <li><b>Stay Connected:</b> Join the <a href="https://discord.gg/f6vPZeb7" target="_blank">DRL Discord</a> for updates, discussions, and more.</li>
        </ul>

        <p>We're excited to see what this season brings. Let's fly high and make Fall 2024 unforgettable!</p>

        <p><b>Your Community Team</b></p>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
