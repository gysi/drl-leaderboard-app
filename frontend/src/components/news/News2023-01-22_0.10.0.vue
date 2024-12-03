<template>
  <!-- 0.10.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">0.10.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-01-22 18:26') }}</div>
    </q-card-section>

    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds average position to overall rankings</li>
        <li>Question icons on columns which lead to explanations within the faq page (also links warning icon for invalid runs).
          The section in the FAQ page will automatically expand</li>
        <li>Beaten by column is now sortable!</li>
        <li>Beaten by column has now an entry within the FAQ page explaining it</li>
      </ul>
      <div class="text-h6">Improvements</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Changes wording on beaten by overlay</li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
