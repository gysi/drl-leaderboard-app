<template>
  <q-page class="index-page q-pa-md">
    <div class="row justify-start items-start q-col-gutter-md content-start">
      <div class="row col-12 justify-start items-start q-gutter-md">
        <q-table
          title="Latest Activity"
          :columns="latestActivity.columns"
          :rows="latestActivity.rows"
          :loading="latestActivity.loading"
          :pagination="latestActivity.pagination"
          :row-key="latestActivity.uniqueFunction"
          class="col-auto"
          flat
          bordered
          hide-bottom
          dense
        >
          <template v-slot:body-cell-trackName="props">
            <td>
              <q-chip dense class="track-chip">{{ props.row.trackName }}</q-chip>
              <q-chip dense class="track-chip-map">{{ props.row.mapName }}</q-chip>
              <q-chip dense class="track-chip-parentcategory">{{ props.row.parentCategory }}</q-chip>
              <q-btn type="a" :to="{ name: 'tracklb', query: { trackId: props.row.trackId } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="`${props.row.name} ${props.row.mapName} ${props.row.parentCategory}`">
              </q-btn>
            </td>
          </template>
          <template v-slot:body-cell-playerName="props">
            <td>
              {{ props.row.playerName }}
              <q-btn type="a" :to="{ name: props.row.parentCategory === 'Community' ? 'playerlb-community' : 'playerlb', query: { playerName: props.row.playerName } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="props.row.playerName">
              </q-btn>
            </td>
          </template>
        </q-table>
        <q-table
          title="Latest Top10 Activity"
          :columns="latestActivity.columns"
          :rows="latestActivityTop10.rows"
          :loading="latestActivityTop10.loading"
          :pagination="latestActivityTop10.pagination"
          :row-key="latestActivity.uniqueFunction"
          class="col-auto"
          flat
          bordered
          hide-bottom
          dense
        >
          <template v-slot:body-cell-trackName="props">
            <td>
              <q-chip dense class="track-chip">{{ props.row.trackName }}</q-chip>
              <q-chip dense class="track-chip-map">{{ props.row.mapName }}</q-chip>
              <q-chip dense class="track-chip-parentcategory">{{ props.row.parentCategory }}</q-chip>
              <q-btn type="a" :to="{ name: 'tracklb', query: { trackId: props.row.trackId } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="`${props.row.name} ${props.row.mapName} ${props.row.parentCategory}`">
              </q-btn>
            </td>
          </template>
          <template v-slot:body-cell-playerName="props">
            <td>
              {{ props.row.playerName }}
              <q-btn type="a" :to="{ name: props.row.parentCategory === 'Community' ? 'playerlb-community' : 'playerlb', query: { playerName: props.row.playerName } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="props.row.playerName">
              </q-btn>
            </td>
          </template>
        </q-table>
      </div>
      <div class="row col-12 justify-start items-start q-gutter-md">
        <q-table
          title="Most entries by player last 7 days"
          :columns="mostPbsLast7Days.columns"
          :rows="mostPbsLast7Days.rows"
          :loading="mostPbsLast7Days.loading"
          :pagination="mostPbsLast7Days.pagination"
          row-key="playerName"
          class="col-auto"
          flat
          bordered
          hide-bottom
          dense
        >
          <template v-slot:body="props">
            <q-tr :props="props" style="position:relative;">
              <q-td v-for="col in props.cols" :key="col.name" :props="props"
                    style="padding-top: 6px; padding-bottom: 7px">
                  {{ props.row[col.name] }}
              </q-td>
              <q-btn type="a" :to="{ name: 'playerlb', query: { playerName: props.row.playerName } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="props.row.playerName">
              </q-btn>
            </q-tr>
          </template>
        </q-table>
        <q-table
          title="Most entries by player last month"
          :columns="mostPbsLast7Days.columns"
          :rows="mostPbsLastMonth.rows"
          :loading="mostPbsLastMonth.loading"
          :pagination="mostPbsLastMonth.pagination"
          row-key="playerName"
          class="col-auto"
          flat
          bordered
          hide-bottom
          dense
        >
          <template v-slot:body="props">
            <q-tr :props="props" style="position:relative;">
              <q-td v-for="col in props.cols" :key="col.name" :props="props"
                    style="padding-top: 6px; padding-bottom: 7px">
                {{ props.row[col.name] }}
              </q-td>
              <q-btn type="a" :to="{ name: 'playerlb', query: { playerName: props.row.playerName } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="props.row.playerName">
              </q-btn>
            </q-tr>
          </template>
        </q-table>
        <q-table
          title="Most entries by track last 14 days"
          :columns="mostTrackEntriesLast14Days.columns"
          :rows="mostTrackEntriesLast14Days.rows"
          :loading="mostTrackEntriesLast14Days.loading"
          :pagination="mostTrackEntriesLast14Days.pagination"
          row-key="id"
          class="col-auto"
          flat
          bordered
          hide-bottom
          dense
        >
          <template v-slot:body="props">
            <q-tr :props="props" style="position:relative;">
              <q-td v-for="col in props.cols" :key="col.name" :props="props">
                <div v-if="col.name === 'trackName'">
                  <q-chip dense class="track-chip">{{ props.row.name }}</q-chip>
                  <q-chip dense class="track-chip-map">{{ props.row.mapName }}</q-chip>
                  <q-chip dense class="track-chip-parentcategory">{{ props.row.parentCategory }}</q-chip>
                </div>
                {{ col.name !== 'trackName' ? props.row[col.name] : '' }}
              </q-td>
              <q-btn type="a" :to="{ name: 'tracklb', query: { trackId: props.row.id } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="`${props.row.name} ${props.row.mapName} ${props.row.parentCategory}`">
              </q-btn>
            </q-tr>
          </template>
        </q-table>
        <q-table
          title="Most entries by track last month"
          :columns="mostTrackEntriesLastMonth.columns"
          :rows="mostTrackEntriesLastMonth.rows"
          :loading="mostTrackEntriesLastMonth.loading"
          :pagination="mostTrackEntriesLastMonth.pagination"
          row-key="id"
          class="col-auto"
          flat
          bordered
          hide-bottom
          dense
        >
          <template v-slot:body="props">
            <q-tr :props="props" style="position:relative;">
              <q-td v-for="col in props.cols" :key="col.name" :props="props">
                <div v-if="col.name === 'trackName'">
                  <q-chip dense class="track-chip">{{ props.row.name }}</q-chip>
                  <q-chip dense class="track-chip-map">{{ props.row.mapName }}</q-chip>
                  <q-chip dense class="track-chip-parentcategory">{{ props.row.parentCategory }}</q-chip>
                </div>
                {{ col.name !== 'trackName' ? props.row[col.name] : '' }}
              </q-td>
              <q-btn type="a" :to="{ name: 'tracklb', query: { trackId: props.row.id } }"
                     rounded flat padding="0" dense class="button-fills-whole-td" :aria-label="`${props.row.name} ${props.row.mapName} ${props.row.parentCategory}`">
              </q-btn>
            </q-tr>
          </template>
        </q-table>
      </div>
    </div>
  </q-page>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { getDateDifference } from 'src/modules/LeaderboardFunctions'
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "🏆 DRL Leaderboards 🏆",
  meta: {
    description: {
      name: 'description',
      content: `See the latest leaderboard activies and rankings. Discover community activies like tournaments and much more.`
    }
  }
})

// Setup reactive data
const latestActivity = ref({
  columns: [
    { name: 'playerName', label: 'Player', field: 'playerName', align: 'left' },
    { name: 'position', label: '#', field: 'position', align: 'right' },
    { name: 'createdAt', label: 'Time Set', field: 'createdAt', format: (val, row) => getDateDifference(val), align: 'right' },
    { name: 'trackName', label: 'Track / Map / Category', align: 'left'},
  ],
  rows: [],
  pagination: {
    rowsPerPage: 10
  },
  loading: false,
  uniqueFunction: row => row.playerName + row.createdAt
})

const latestActivityTop10 = ref({
  rows: [],
  pagination: {
    rowsPerPage: 10
  },
  loading: false,
})

const mostPbsLast7Days = ref({
  columns: [
    { name: 'playerName', label: 'Player', field: 'playerName', align: 'left' },
    { name: 'entries', label: 'Entries', field: 'entries', align: 'center' },
    { name: 'bestPosition', label: 'Best Position', field: 'bestPosition', align: 'center' },
    { name: 'avgPosition', label: 'Average Position', field: 'avgPosition', align: 'center'},
  ],
  rows: [],
  pagination: {
    rowsPerPage: 10
  },
  loading: false,
})

const mostPbsLastMonth = ref({
  rows: [],
  pagination: {
    rowsPerPage: 10
  },
  loading: false,
})

const mostTrackEntriesLast14Days = ref({
  columns: [
    { name: 'trackName', label: 'Track / Map / Category', align: 'left'},
    { name: 'entries', label: 'Entries', field: 'entries', align: 'center' },
  ],
  rows: [],
  pagination: {
    rowsPerPage: 10
  },
  loading: false,
})

const mostTrackEntriesLastMonth = ref({
  columns: [
    { name: 'trackName', label: 'Track / Map / Category', align: 'left'},
    { name: 'entries', label: 'Entries', field: 'entries', align: 'center' },
  ],
  rows: [],
  pagination: {
    rowsPerPage: 10
  },
  loading: false,
})

// fetchData method
const fetchData = async () => {
  // Set all loading states to true
  latestActivity.value.loading = true
  latestActivityTop10.value.loading = true
  mostPbsLast7Days.value.loading = true
  mostPbsLastMonth.value.loading = true
  mostTrackEntriesLast14Days.value.loading = true
  mostTrackEntriesLastMonth.value.loading = true

  try {
    const baseUrl = `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
    const [responseLatest, responseLatestTop10, responseMostPbsLast7Days, responseMostPbsLastMonth, responseMostTrackEntriesLast14Days, responseMostTrackEntriesLastMonth] = await Promise.all([
      axios.get(`${baseUrl}/leaderboards/latest-activity`),
      axios.get(`${baseUrl}/leaderboards/latest-activity-top-10`),
      axios.get(`${baseUrl}/leaderboards/most-pbs-last-7-days`),
      axios.get(`${baseUrl}/leaderboards/most-pbs-last-month`),
      axios.get(`${baseUrl}/leaderboards/most-track-entries-last-14-days`),
      axios.get(`${baseUrl}/leaderboards/most-track-entries-last-month`),
    ])
    latestActivity.value.rows = responseLatest.data
    latestActivityTop10.value.rows = responseLatestTop10.data
    mostPbsLast7Days.value.rows = responseMostPbsLast7Days.data
    mostPbsLastMonth.value.rows = responseMostPbsLastMonth.data
    mostTrackEntriesLast14Days.value.rows = responseMostTrackEntriesLast14Days.data
    mostTrackEntriesLastMonth.value.rows = responseMostTrackEntriesLastMonth.data
  } catch (error) {
    console.error(error)
  } finally {
    // Set all loading states to false
    latestActivity.value.loading = false
    latestActivityTop10.value.loading = false
    mostPbsLast7Days.value.loading = false
    mostPbsLastMonth.value.loading = false
    mostTrackEntriesLast14Days.value.loading = false
    mostTrackEntriesLastMonth.value.loading = false
  }
}

// Lifecycle hook
onMounted(fetchData)
</script>


<style lang="sass" scoped>
:deep(.q-table tbody td)
  font-size: 16px

:deep(.q-table thead th)
  font-size: 18px

// On mobile devices, make the table rows bigger (48px) so that google doesn't complain about clickable elements being too close together
@media (any-pointer: coarse)
  .index-page :deep(tr, td)
    height: 49px !important



</style>
