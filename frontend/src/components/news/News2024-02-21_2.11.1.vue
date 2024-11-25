<template>
  <!-- 2.11.1 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">2.11.1 Release</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-02-21 20:00') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Updates</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>Pointsystem Change:</b> Following extensive feedback, the points system was adjusted again.
          It has been changed back to a position-based system instead of a time-based one.
          It works in a similar way to before, but the underlying formula has been adjusted so that the points increase exponentially with the position.
          The bonus points have also been adjusted.
          <q-btn type="a" flat padding="0"
                 :to="{ name: 'faq', query: { card: 'pointSystem' } }"
          >Read more in the FAQ</q-btn>
          <q-img
            src="~assets/pointsystem.png"
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
