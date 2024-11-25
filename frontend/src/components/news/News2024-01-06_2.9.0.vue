<template>
  <!-- 2.9.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.9.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-01-06 02:20') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>There is now an additional discord bot for tournaments that posts reminders, tournament starts and results.
          You can add it to your server by calling the following URL in your browser:
          <br/>
          <a href="https://discord.com/api/oauth2/authorize?client_id=1192998273594708058&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
          <br/>Commands:
          <ul><li>/tourney-reminder-activate [channel]</li><li>/tourney-reminder-deactivate [channel]</li>
            <li>/tourney-results-activate [channel]</li><li>/tourney-results-deactivate [channel]</li>
          </ul>
        </li>
      </ul>
      <div class="text-h6">Misc</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>The leaderboard discord bot commands were renamed:
          <br/>
          <a href="https://discord.com/api/oauth2/authorize?client_id=1189335274422009978&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
          <br/>Commands:
          <ul><li>/leaderboard-posts-activate [channel]</li><li>/leaderboard-posts-deactivate [channel]</li>
          </ul>
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
