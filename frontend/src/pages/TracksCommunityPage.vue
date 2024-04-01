<template>
  <q-page padding class="q-pa-md"
          style="height: 100%; max-height: 100%; display: grid; grid-template-rows: auto auto auto auto 1fr;">
    <div class="rounded-borders tournament-header-top header-toolbar row items-center q-pa-xs">
      <div class="doc-card-title q-my-xs q-mr-sm ">
        <span>Tracks - {{ season.name }}</span>
        <div class="text-caption text-right">
          <q-icon name="event"></q-icon>
          {{ formatISODateTimeToDate(season.startDate) }} - {{ formatISODateTimeToDate(season.endDate) }} UTC
        </div>
      </div>
    </div>
    <q-table
      :columns="columns"
      :rows="rows"
      row-key="id"
      class="my-sticky-header-table"
      style="max-height: 100%; overflow: hidden"
      :loading="loading"
      loading-label="Loading tracks..."
      no-data-label="Tracks couldn't be loaded, please try again"
      flat
      bordered
      hide-pagination
      :pagination="{rowsPerPage: 0}"
    >
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="name" :props="props" class="td-borders-font-size16" style="padding: 5px 5px 5px 5px">
            <q-item style="padding:0" clickable :to="`/track-leaderboard?trackId=${props.row.id}`"
                    class="q-item-player-region">
              <q-item-section thumbnail style="padding-right: 10px; margin: 0;">
                <img v-if="props.row.mapThumb != null" :src="buildImgCacheUrl(props.row.mapThumb)" width="120" height="68"
                     style="width: 120px; height: 68px" />
                <img v-else src="~assets/maps/maps-120x68.png"
                     :class="'map-'+props.row.mapName.replaceAll(/[. ]/g, '-').toLocaleLowerCase()"
                     width="120" height="68" style="width: 120px; height: 68px" />
              </q-item-section>
              <q-item-section >
                <q-item-label class="track-item-label">{{ props.row.name }}</q-item-label>
                <q-item-label caption>
                  <q-badge class="track-chip-map">{{ props.row.mapName }}</q-badge>
                </q-item-label>
                <q-item-label caption>
                  <q-badge class="track-chip-parentcategory">{{ props.row.parentCategory }}</q-badge>
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-td>
          <q-td key="trackCreator" :props="props" class="td-borders-font-size16"
                style="padding: 5px 5px 5px 5px">
            {{ props.row.trackCreator }}
          </q-td>
          <q-td key="ratingScore" :props="props" class="td-borders-font-size16"
                style="padding: 5px 16px 5px 5px">
            ({{ props.row.ratingCount }})
            <q-icon v-for="n in ratingScoreToStarCount(props.row.ratingScore)" name="star" color="yellow-9" size="sm" :key="n" />
            <q-icon v-for="n in (5 - ratingScoreToStarCount(props.row.ratingScore))" name="star_border" size="sm" :key="n" />
          </q-td>
          <q-td key="trackDifficulty" :props="props" class="td-borders-font-size16"
                style="padding: 5px 5px 5px 5px">
            {{ difficultyToText(props.row.customTrackDifficulty !== null ? props.row.customTrackDifficulty : props.row.trackDifficulty) }}
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </q-page>
</template>

<script setup>
import axios from 'axios';
import {computed, ref, shallowRef} from 'vue';
import {formatISODateTimeToDate} from "src/modules/LeaderboardFunctions.js";
import {fetchCurrentSeason} from "src/modules/backendApi.js";
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "Community Season Tracks",
  meta: {
    description: {
      name: 'description',
      content: `Dive into the latest 30 tracks of the community season. Discover new challenges.`
    }
  }
})

const sortRating = (a, b, rowA, rowB) => {
  const scoreDiff = rowA.ratingScore - rowB.ratingScore;
  if (scoreDiff !== 0) return scoreDiff;
  return rowA.ratingCount - rowB.ratingCount;
};

const columns = [
  { name: 'name', label: 'Name', field: 'name', align: 'left', sortable: true },
  { name: 'trackCreator', label: 'Creator', field: 'trackCreator', align: 'center', sortable: true },
  { name: 'ratingScore', label: 'Rating', field: 'ratingScore', sortable: true, sort: sortRating},
  { name: 'trackDifficulty', label: 'Difficulty', field: 'trackDifficulty', align: 'center', sortable: true }
]

const season = shallowRef({});
const rows = shallowRef([]);
const loading = ref(true);

const fetchData = async () => {
  try {
    const response = await axios.get(process.env.DLAPP_API_URL+'/tracks/community-season/current');
    rows.value = response.data;
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};
const buildImgCacheUrl = function(url) {
  if (url) {
    let encodedUrl = encodeURIComponent(url);
    return computed(() => `${process.env.DLAPP_THUMBOR_URL}/120x68/${encodedUrl}`).value;
  }
}

const ratingScoreToStarCount = function (ratingScore) {
  return computed(() => {
    return ratingScore * 100 / 20
  }).value;
}

const difficultyToText = function (difficulty) {
  const mapping = [
    'Basic',
    'Easy',
    'Medium',
    'Hard',
    'Very hard'
  ]
  return computed(() => {
    return mapping[difficulty]
  }).value;
}

fetchCurrentSeason().then((data) => {
  season.value = data;
})
fetchData();
</script>

<style lang="scss">
</style>

<style lang="sass" scoped>
:deep(.track-item-label)
  font-size: 19px

tbody .q-td
  font-size: 16px
</style>
