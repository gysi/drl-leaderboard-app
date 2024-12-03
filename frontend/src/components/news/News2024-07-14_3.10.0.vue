<template>
  <!-- 3.10.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">Prize Pool Contributions and Rewards!</div>
      <q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-07-14 22:30') }}</div>
    </q-card-section>
    <q-card-section class="q-pa-sm news-content">
      <div class="q-my-sm" style="font-size: 1rem">
        We're excited to introduce a new feature to reward our contributors! From now on, contributing to the prize pool will earn you special awards on the website and a unique Discord role.
        <br/><br/>
        <div class="text-h6">Website Awards</div>
        Contributors will have a special icon displayed beside their name on the leaderboards, showcasing their support for the community.
        <br/>
        <q-img
          src="~assets/news/24_summer_season_awards_preview.png"
          spinner-color="white"
          height="300px"
          fit="contain"
          position="0 0"
        />
        <br/><br/>
        <div class="text-h6">Discord Role</div>
        Contributors will also receive a special role in our Discord server, making it easy for everyone to recognize their support.
        <br/>
        <q-img
          src="~assets/news/24_summer_season_awards_discord_preview.png"
          spinner-color="white"
          height="100px"
          fit="contain"
          position="0 0"
        />
        <br/><br/>
        <div class="text-h6">Micro Contributions</div>
        You can now make micro contributions to the prize pool by subscribing to the Matcherino YouTube channel. Each subscription contributes 28 cents to the prize pool.
        <br/>
        <q-img
          src="~assets/news/24_summer_season_matcherino_prize_pool_contribution_subscription.png"
          spinner-color="white"
          height="250px"
          fit="contain"
          position="0 0"
        />
        <br/><br/>
        <div class="text-h6">Contribution Items</div>
        Check out the items you can purchase to contribute to the prize pool and earn these rewards. <br/>
        You will find them here: <a href="https://matcherino.com/tournaments/116263" target="_blank">Matcherino Page</a>
        <br/>
        <q-img
          src="~assets/news/24_summer_season_matcherino_marketplace_items.png"
          spinner-color="white"
          height="300px"
          fit="contain"
          position="0 0"
        />
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
