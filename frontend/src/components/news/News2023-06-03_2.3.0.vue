<template>
  <!-- 2.3.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.3.0 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2023-06-03 02:30') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>(Preview) The Tournaments page is now available. You can see the current tournament rankings.<br/>
          Each tournament lasts 3 months and the top 12 tournaments for each player are counted.<br/>
          There are four seasons per year: Spring, Summer, Fall and Winter.<br/>
          Spring: March 1st - May 31th<br/>
          Summer: June 1st - August 31th<br/>
          Fall: September 1st - November 30th<br/>
          Winter: December 1st - February 28th (or February 29th in a leap year)<br/>
          Tournament points are based on the DRL irl point system.<br/>
          The current season (Summer) started on 2023-05-01 instead of normally 2023-06-01, because otherwise
          there would only be 3 recorded tournaments for the Fall season, as the crawling of tournaments only started
          a few weeks ago.<br/>
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
