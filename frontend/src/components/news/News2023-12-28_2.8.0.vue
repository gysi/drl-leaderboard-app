<template>
  <!-- 2.8.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.8.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-12-28 01:30') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>There is now a discord bot that posts new best times within the top 50 to a discord server.
          You can add it to your server by calling the following URL in your browser:
          <br/>
          <a href="https://discord.com/api/oauth2/authorize?client_id=1189335274422009978&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
          <br/>Commands:
          <ul><li>/drl-leaderboard-posts-activate</li><li>/drl-leaderboard-posts-deactivate</li></ul>
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
