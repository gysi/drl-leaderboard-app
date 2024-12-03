<template>
  <!-- 4.0.4 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">🎉 Qualification Complete!  🚀</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-12-01 01:00') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        <p>The qualification phase of the <b>DRL Community Fall Season 2024</b> is officially over, and the leaderboard is locked! After months of intense racing, we’re excited to announce the top 24 pilots who have secured their spots in the Grand Finals. Congratulations to all participants for their incredible efforts!</p>

        <div class="text-h6">🎯 Qualified Pilots:</div>
        <ol>
          <li>gysi OG</li>
          <li>WestFPV</li>
          <li>Green OG</li>
          <li>THE_B0B!!</li>
          <li>TrippFPV</li>
          <li>Mr.Persister</li>
          <li>Killian</li>
          <li>AnnihilationRobo</li>
          <li>H5R</li>
          <li>Goddess_Randi</li>
          <li>Corrupted</li>
          <li>TWICEFPV</li>
          <li>₸ЯIИ✘</li>
          <li>Ashtray</li>
          <li>itsLeeTV</li>
          <li>sascha.roland</li>
          <li>K9-Wolf</li>
          <li>HaloWalker</li>
          <li>丂 Q ㄩ ㄟ D</li>
          <li>DUTCH</li>
          <li>DefensiveDuck</li>
          <li>2018 Toyota Tacoma</li>
          <li>Trozii</li>
          <li>TurtleCheif</li>
        </ol>

        <div class="text-h6">🛡️ Backup Pilots:</div>
        <ol>
          <li>Dima</li>
          <li>Doctor_Rumack</li>
          <li>Jab</li>
          <li>Giddy Gimbals</li>
          <li>Tydronious</li>
        </ol>

        <p>Backup pilots will step in if any qualified pilots are unable to participate in the Grand Finals. Stay ready!</p>

        <div class="text-h6">📅 What’s Next?</div>
        <ul>
          <li><b>Final Participant List Confirmation:</b> December 4th, 2024.</li>
          <li><b>Grand Finals:</b> December 8th, 2024, 21:00 UTC (16:00 EST, 22:00 CET).</li>
          <li><b>Final Track (#25) Release:</b> December 8th, 2024, 20:00 UTC (15:00 EST, 21:00 CET).</li>
        </ul>

        <p>All qualified pilots and backups must ensure they are registered on <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino</a> and have joined the <a href="https://discord.gg/wtuRmwpEJg" target="_blank">DRL Discord</a>. Failure to do so by December 4th may result in forfeiture of your spot.</p>

        <div class="text-h6">📖 Preparing for the Grand Finals:</div>
        <p>Don’t forget to review the <a href="/grand-finals-guide-rules" target="_blank">Grand Finals Guide & Rules</a> to familiarize yourself with the tournament format, track pools, and rules. This will ensure you’re ready to bring your A-game on race day!</p>

        <p>Congratulations again to all our qualified pilots and backups. We can’t wait to see you all on December 8th for the epic showdown! 🚁</p>
        <p><b>Your Community Team</b></p>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import { getDateDifferenceToNowByGermanTimezone } from "src/modules/LeaderboardFunctions.js";
</script>
