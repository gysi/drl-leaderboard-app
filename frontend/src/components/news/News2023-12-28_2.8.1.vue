<template>
  <!-- 2.8.1 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.8.1 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-12-28 23:25') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds some additional information (faster by, pb time, diff to leader) to the discord embeds of the leaderboard bot.
        </li>
      </ul>
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Switches from selection menu to autocomplete on commands for the discord bot, because the menu is limited to 25 entries<br/>
          The Command structure is now as follows:
          <ul><li>/drl-leaderboard-posts-activate [channel]</li><li>/drl-leaderboard-posts-deactivate [channel]</li></ul>
        </li>
        <li>Fixes a bug where the discord bot wouldn't post pbs when they didn't have a replay (because of the DRL System still processing the replay) but later on got one.
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
