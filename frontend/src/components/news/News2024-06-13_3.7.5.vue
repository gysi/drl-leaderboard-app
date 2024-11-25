<template>
  <!-- 3.7.5 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Prize Pool Update</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-06-13 00:19') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        The prize pool funding and payout is now managed through Matcherino.<br/>
        Check it out: <a href="https://matcherino.com/tournaments/116263" target="_blank">https://matcherino.com/tournaments/116263</a>
        <br/>
        This makes contributions to the prize pool easier and more legitimate.
        Also payouts are way easier to manage through their system.
        <br/><br/>
        Make sure to create an account on Matcherino and join the competition if you want to claim prize money.
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
