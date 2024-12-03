<template>
  <!-- 2.13.1 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.13.1 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-27 00:30') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Bug fixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds missing track to leaderboard: CTG / 07: TREEHOUSE TANGO<br/>
          It wasn't added in the past because DRL didn't flag it as an official track.<br/>
          If you just don't see it yet, it takes some time to get picked up by the backend. The backend looks for new tracks every full hour.
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
