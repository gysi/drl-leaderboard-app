<template>
  <!-- 3.7.1 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Community Summer Season Competition FAQ</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-02 03:40') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        There is now a FAQ Page about the current ongoing competition: <br/>
        <q-btn type="a" flat padding="0"
               :to="{ name: 'communitySeasonCompetitionFaq' }"
        >Click here to go to the FAQ</q-btn>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
