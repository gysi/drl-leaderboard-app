<template>
  <!-- 1.7.2 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">1.7.2 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-03-06 00:25') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Stops setting BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT or BETTER_ENTRY_WITH_SAME_NAME on player run if the entry it references is invalid, for example because of NO_REPLAY
        </li>
        <li>Comparing entries didn't match the actual runs. This happened when the compared entries contained multiple entries for the same track (this can happen when there are multiple entries for the same player for a track)
        </li>
        <li>Comparing entries wouldn't match if the player had multiple entries for the same track
        </li>
      </ul>
      <div class="text-h6">Misc</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Stops showing comparing times for invalid runs
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
