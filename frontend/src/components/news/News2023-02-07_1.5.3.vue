<template>
  <!-- 1.5.3 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">1.5.3 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-02-07 21:07') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Fixes the "18:" infront of leaderboard times when being on a timezone different that UTC+1</li>
      </ul>
      <div class="text-h6">Optimizations</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Expire most backend caches after 1 minute and not just after a full recrawl of the DRL api</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
