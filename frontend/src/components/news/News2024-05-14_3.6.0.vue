<template>
  <!-- 3.6.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">3.6.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-05-14 02:40') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds command to discord tournament bot to tag a role for tournament reminders
        </li>
        <li>Adds command to discord tournament bot to test the messages
        </li>
        <li><a href="https://discord.com/api/oauth2/authorize?client_id=1192998273594708058&permissions=2064&scope=bot+applications.commands" target="_blank">Bot Link</a>
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
