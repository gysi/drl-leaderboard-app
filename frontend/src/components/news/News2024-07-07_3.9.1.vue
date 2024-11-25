<template>
  <!-- 3.9.1 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Prize Pool Grows Again: Thanks, TheSauceror!</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-07-07 04:35') }}</div> </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        A big shoutout to <b>TheSauceror</b> for contributing $10 to the Community Summer Season prize pool! Your generosity is much appreciated and helps make this competition even more rewarding for our top pilots.
        <br/><br/>
        <div class="text-h6">Updated Prize Pool:</div>
        The updated prize pool distribution is as follows:
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>1st Place: $474</li>
          <li>2nd Place: $338</li>
          <li>3rd Place: $230</li>
          <li>4th Place: $176</li>
          <li>5th Place: $135</li>
        </ul>

        Remember, you can still contribute to the prize pool through our <a href="https://matcherino.com/tournaments/116263" target="_blank">Matcherino page</a>!
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
