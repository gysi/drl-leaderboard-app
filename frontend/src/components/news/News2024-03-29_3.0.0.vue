<template>
  <!-- 3.0.0 Release -->
  <q-card>
    <q-card-section class="q-py-none q-px-sm row news-content-header">
      <div class="text-h5 q-py-sm">3.0.0 Release - Community Season</div><q-space />
      <div style="padding-top: 5px">{{ getDateDifferenceToNowByGermanTimezone('2024-03-29 02:00') }}</div>
    </q-card-section>
    <q-card-section class="q-py-sm q-pl-lg news-content">
      <div class="text-h6">Features</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>Community Seasons:</b><br/>This release introduces Community Seasons.<br/>
          A new section has been added to the navigation for this purpose.
          A Community Season has the same cycle as the Tournament Season and is in sync with it.<br/>
          Per season, players compete for 1st place on 30 tracks created by the community.
          The selection of tracks is based on an algorithm that selects the tracks per season,
          and manual adjustments are made if necessary.<br/>
          You can read details about this algorithm in the FAQs<br/><br/>

          The leaderboard entries for the community tracks and the ranking are only displayed and counted
          if a player has at least one best time on these tracks which took place within the season.<br/>
          As a rule, there should always be one of the 30 tracks on which a player has no time or can easily
          get a better time. The advantage is that the ranking ignores old entries from players who are
          longer active, especially because the season also includes well-known but older community tracks.
          <br/><br/>
          A heartfelt appreciation goes to Mr. Persister &lt;3, whose boundless enthusiasm and meticulous testing of the Community Tracks
          have greatly enriched our seasonal dynamics. The invaluable insights gained from our extensive discussions and collaborations
          have directly influenced the seasonal offerings, enhancing the competitive spirit and community involvement on the website.
          <br/><br/>
        </li>
        <li><b>More Leaderboard Entries per Track:</b><br/>
          Up to 200 leaderboard entries are now displayed in the track leaderboards,
          which helps newer and less experienced players to be included on the website.
          <br/><br/>
        </li>
        <li><b>Faster crawling and more up-to-date data:</b><br/>
          Before, all tracks in the pool were crawled one after the other. Now there are 2 pools that crawl tracks in parallel.
          A high-priority pool and a low-priority pool.<br/>
          The highpriority pool contains 15% of the tracks with the most recent entries and the other pool contains the rest.<br/>
          Mostly players fly tracks that have been flown by others recently or they reach several PBs in a row and with this system
          the times should now appear much faster on the website.<br/><br/>
          In addition, caches previously existed for all rankings and leaderboard entries, which were only updated once all
          tracks had been crawled. These caches are now updated after each crawled track.
          <br/><br/>
        </li>
        <li><b>Track thumbnails:</b><br/>
          In the track leaderboard header or on the tracks page (community section),
          the real thumbnails created by the track creator are now displayed (if any exist).
          <br/><br/>
        </li>
        <li><b>Latest Activity:</b><br/>
          The rankings pages now include a new "Latest Activity" column to give you a better idea of who is active.
          <br/><br/>
        </li>
        <li><b>Featured Twitch Streams:</b><br/>
          DRL Twitch streams are now displayed at the bottom of the navigation, sorted by most viewers.
          Here is a preview (Because there was no DRL live stream at the time of this post, I took the
          famous game "Pools, Hot Tubs, and Beaches" as an example)
          <br/>
          <q-img
            src="~assets/featured_twitch_streams.png"
            spinner-color="white"
            height="250px"
            fit="contain"
            position="0 0"
          />
          <br/><br/>
        </li>
        <li><b>Styling:</b><br/>
          The styling has been slightly optimised for the respective light and dark modes. In addition,
          there are now customised scrolling bars, unless you use Firefox.
        </li>
      </ul>
      <div class="text-h6">Changes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li><b>"Tracks" got removed:</b><br/>
          The navigation entry "Track" has been removed.
          It did not really offer any added value and has now been removed for the time being
          until it becomes more useful and more information is displayed there or functions are added.
        </li>
        <li><b>Navigation:</b><br/>
          A few less frequently used items have now been summarised in "Other"
        </li>
      </ul>
      <div class="text-h6">Bug fixes</div>
      <ul class="q-my-sm" style="font-size: 1rem">
        <li>Adds another invalid run reason: WRONG_REPLAY, read more about in the FAQ
        </li>
      </ul>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {getDateDifferenceToNowByGermanTimezone} from "src/modules/LeaderboardFunctions.js";
</script>
