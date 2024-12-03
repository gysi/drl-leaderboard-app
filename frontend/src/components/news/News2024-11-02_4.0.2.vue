<template>
  <!-- 4.0.2 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Prize Pool Grows Again: Thanks, THE_BOB!!!</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-11-02 23:28') }}</div> </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        A big shoutout to <b>THE_BOB!!</b> for contributing $363.89 to the Community Summer Season prize pool! Your generosity is much appreciated and helps make this competition even more rewarding for our top pilots.
        <br/><br/>
        <div class="text-h6">Updated Prize Pool ($1464):</div>
        The updated prize pool distribution is as follows:
        <ul class="q-my-sm" style="font-size: 1rem">
          <li>1st Place: $585.55</li>
          <li>2nd Place: $365.97</li>
          <li>3rd Place: $219.58</li>
          <li>4th Place: $146.38</li>
          <li>5th Place: $73.19</li>
          <li>6th Place: $73.19</li>
        </ul>

        Remember, you can still contribute to the prize pool through our <a href="https://matcherino.com/t/drl-community-fall-season-2024" target="_blank">Matcherino page</a>!
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
