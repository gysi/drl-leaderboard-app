<template>
  <!-- 0.2.3 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">0.2.3 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-15 03:26') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Bugfixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Enables cascade on delete for foreignkeys inside the DB (DB Structure needs to be recreated)</li>
        <li>Fixes bug in frontend which would throw an error and not render track entries if mapName or parentCategory is null</li>
        <li>Adjusts to 64 id values inside frontend because javascript cannot handle 64bit integers so we use a string</li>
        <li>Correctly uses docker_compose.env on the backend service and not on the postgres service..</li>
      </ul>
      <div class="text-h6">Performance</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Optimizes crawling / saving of objects from the API</li>
        <li>Switches to TSIDs instead of sequence generated ids so that batch updates can be used</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
