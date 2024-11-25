<template>
  <!-- 2.12.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.12.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-25 02:33') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>DRL Betaflight Calculator:</b>
          Based on TwiceFPV's innovative idea, we've introduced the interactive DRL Betaflight Calculator. This feature allows users to simulate adjustments within the application, enhancing your experience. Available now via the navigation menu.
          <q-img
            src="~assets/drl-betaflight-calculator_medium.png"
            spinner-color="white"
            height="250px"
            fit="contain"
            position="0 0"
          />
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
