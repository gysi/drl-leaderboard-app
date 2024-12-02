<template>
  <!-- 4.0.5 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">📰 Updates: Prize Pool, Missing Pilots, and Seeding Draw 🎯</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-12-03 01:00') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        <div class="text-h6">💰 Updated Prize Pool</div>
        <p>The prize pool has grown again! Thanks to the generous contributions of <b>FPVgan</b> ($36.11) and <b>Corrupted</b> ($50.00), the total prize pool now stands at <b>$1550</b>.</p>
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>1st Place: $620.00</li>
          <li>2nd Place: $387.50</li>
          <li>3rd Place: $232.50</li>
          <li>4th Place: $155.00</li>
          <li>5th Place: $77.50</li>
          <li>6th Place: $77.50</li>
        </ul>
        <p>If you’d like to contribute and support the competition, you can still do so via our <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino page</a>.</p>

        <div class="text-h6">❓ Missing Pilots</div>
        <p>We still haven’t heard back from <b>five qualified pilots</b>. If you see your name below, please contact <b>Mr. Persister</b> or <b>gysi</b> immediately. It’s also crucial to complete your registration on <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino</a> and join our <a href="https://discord.gg/wtuRmwpEJg" target="_blank">Discord server</a>.</p>
        <ul>
          <li>TWICEFPV</li>
          <li>₸ЯIИ✘</li>
          <li>sascha.roland</li>
          <li>K9-Wolf</li>
          <li>DefensiveDuck</li>
        </ul>
        <p>If we don’t hear from you soon, your spot may be forfeited, and a backup pilot will take your place. Don’t miss your chance to compete in the Grand Finals!</p>

        <div class="text-h6">🎲 Seeding Draw for the Grand Finals</div>
        <p>As outlined in the <a href="/grand-finals-guide-rules" target="_blank">Season Guide</a>, seeding for the Grand Finals involves some randomness for fairness. The first 4 pilots are directly assigned to their groups, while the remaining pilots are divided into tiers. Pilots in each tier are then randomly assigned to groups 1-4.</p>
        <p>The seeding draw will take place on:</p>
        <ul>
          <li><b>5th of December, 2024, at 00:00 UTC (4th 19:00 EST, 5th 01:00 CET)</b></li>
        </ul>
        <p>We’ll host the draw live on our <a href="https://discord.gg/wtuRmwpEJg" target="_blank">Discord server</a> to ensure transparency and fairness. Feel free to join and watch the process unfold in real-time!</p>

        <p>Stay tuned for more updates as we get closer to the Grand Finals. Thank you all for your participation and enthusiasm. Let’s make this an unforgettable event! 🚁</p>
        <p><b>Your Community Team</b></p>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import { getDateDifferenceToNowByGermanTimezone } from "src/modules/LeaderboardFunctions.js";
</script>
