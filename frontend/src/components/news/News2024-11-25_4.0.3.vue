<template>
  <!-- 4.0.3 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Countdown to the Grand Finals: Are You Ready?</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-11-25 14:30') }}</div> </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        <p>The <b>Grand Finals</b> of the <b>DRL Community Fall Season 2024</b> are finally here! The top 24 pilots will compete in this thrilling showdown on <b>Sunday, December 8th, 2024</b>, starting at <b>21:00 UTC</b> (16:00 EST, 22:00 CET). Witness the action live on <a href="https://twitch.tv/flitedek" target="_blank">Twitch</a>!</p>

        <div class="text-h6">📅 Important Dates:</div>
        <ul>
          <li><b>Qualification Ends:</b> December 1st, 2024, 00:00 UTC (November 30th, 19:00 EST).</li>
          <li><b>Final Participant List:</b> Announced December 4th, 2024.</li>
          <li><b>Final Track (#25) Release:</b> December 8th, 2024, 20:00 UTC (15:00 EST, 21:00 CET).</li>
          <li><b>Grand Finals Start:</b> December 8th, 2024, 21:00 UTC (16:00 EST, 22:00 CET).</li>
        </ul>

        <div class="text-h6">🎯 Tournament Format:</div>
        <ul>
          <li><b>Quarterfinals:</b> 24 pilots compete in 4 groups of 6. Top 3 from each group advance.</li>
          <li><b>Semifinals:</b> 12 pilots in 2 groups of 6. Top 3 from each group advance.</li>
          <li><b>Grand Finals:</b> 6 pilots face off on the season’s final, unreleased track (#25).</li>
        </ul>
        <p>The quarterfinal and semifinal tracks will be chosen randomly from the track pools and announced just 10 minutes before the round starts. This ensures fairness and adds excitement!</p>

        <div class="text-h6">📖 Guide & Rules:</div>
        <p>All competitors should familiarize themselves with the <a href="/grand-finals-guide-rules" target="_blank">Grand Finals Guide & Rules</a>. This document contains essential details about tournament structure, track information, and prize distribution.</p>

        <div class="text-h6">💰 Prize Pool:</div>
        <p>Prizes will be distributed to the top 6 pilots, with the pool managed through <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino</a>. Make sure you're registered before December 4th, 2024, to claim your winnings!</p>

        <div class="text-h6">🎥 Watch Live:</div>
        <p>Catch the Grand Finals live on <a href="https://twitch.tv/flitedek" target="_blank">Twitch</a> at <b>21:00 UTC</b> (16:00 EST, 22:00 CET). Expert commentary will be provided by FPVgan and Seltro.</p>

        <p>Prepare for an unforgettable event as the best pilots take to the skies for the ultimate title. See you there! 🚁</p>
        <p><b>Your Community Team</b></p>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import { getDateDifferenceToNowByGermanTimezone } from "src/modules/LeaderboardFunctions.js";
</script>
