<template>
  <!-- 1.8.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">1.8.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-04-12 02:40') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Very very early preview of the Replay Viewer, not all features are implemented yet.<br/>
          It has some bugs, but it's a start
        </li>
      </ul>
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Fixes loading bar on selectors not being visible
        </li>
      </ul>
      <div class="text-h6">Misc</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Removes some unpretty padding for the navbar items
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
