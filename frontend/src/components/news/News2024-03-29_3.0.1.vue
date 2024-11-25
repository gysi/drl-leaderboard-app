<template>
  <!-- 3.0.1 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">3.0.1 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-03-29 17:36') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Bug fixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>When a player set a new pb his old "gotBeatenBy" entries weren't updated/removed.
        </li>
        <li>The links from the track leaderboard of community tracks to profile
          pages were broken.
        </li>
        <li>Fixes the cache for the latest top10 entries, it wasn't updated at all.
        </li>
        <li>Fixed a bug in the leaderboard Discord bot where it incorrectly announced a player's improvement
          even though the player had not actually advanced in position. This issue occurred when an entry was
          initially submitted without a replay; as a result, the bot did not post an update due to the submission
          appearing invalid. Later, once a replay was added, the bot failed to recognize any position improvement
          because it had already recorded the player's standing, leading to a false notification of progress.
        </li>
        <li>'Lastest Activity' board entries correctly link to community profile leaderboards when its a community track
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
