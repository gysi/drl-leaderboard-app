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
              <q-td v-for="col in props.cols" :key="col.name" :props="props">
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
              <q-td v-for="col in props.cols" :key="col.name" :props="props">
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

<script>
import { defineComponent } from 'vue'
import axios from 'axios';
import { getDateDifference } from 'src/modules/LeaderboardFunctions'

export default defineComponent({
  name: 'IndexPage',
  data(){
    return {
      latestActivity: {
        columns: [
          { name: 'playerName', label: 'Player', field: 'playerName', align: 'left' },
          { name: 'position', label: '#', field: 'position', align: 'right' },
          { name: 'createdAt', label: 'Time Set', field: 'createdAt', format: (val, row) => this.getDateDifference(val), align: 'right' },
          { name: 'trackName', label: 'Track / Map / Category', align: 'left'},
        ],
        rows: [],
        pagination: {
          rowsPerPage: 10
        },
        loading: false,
        uniqueFunction: (row) => row.playerName + row.createdAt
      },
      latestActivityTop10: {
        rows: [],
        pagination: {
          rowsPerPage: 10
        },
        loading: false,
      },
      mostPbsLast7Days: {
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
      },
      mostPbsLastMonth: {
        rows: [],
        pagination: {
          rowsPerPage: 10
        },
        loading: false,
      },
      mostTrackEntriesLast14Days: {
        columns: [
          { name: 'trackName', label: 'Track / Map / Category', align: 'left'},
          { name: 'entries', label: 'Entries', field: 'entries', align: 'center' },
        ],
        rows: [],
        pagination: {
          rowsPerPage: 10
        },
        loading: false,
      },
      mostTrackEntriesLastMonth: {
        columns: [
          { name: 'trackName', label: 'Track / Map / Category', align: 'left'},
          { name: 'entries', label: 'Entries', field: 'entries', align: 'center' },
        ],
        rows: [],
        pagination: {
          rowsPerPage: 10
        },
        loading: false,
      },
    }
  },
  methods: {
    async fetchData() {
      try {
        const [ responseLatest, responseLatestTop10, responseMostPbsLast7Days, responseMostPbsLastMonth,
          responseMostTrackEntriesLast14Days, responseMostTrackEntriesLastMonth] =
          await Promise.all([
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/latest-activity'),
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/latest-activity-top-10'),
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/most-pbs-last-7-days'),
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/most-pbs-last-month'),
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/most-track-entries-last-14-days'),
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/most-track-entries-last-month'),
          ]);
        this.latestActivity.rows = responseLatest.data;
        this.latestActivityTop10.rows = responseLatestTop10.data;
        this.mostPbsLast7Days.rows = responseMostPbsLast7Days.data;
        this.mostPbsLastMonth.rows = responseMostPbsLastMonth.data;
        this.mostTrackEntriesLast14Days.rows = responseMostTrackEntriesLast14Days.data;
        this.mostTrackEntriesLastMonth.rows = responseMostTrackEntriesLastMonth.data;
      } catch (error) {
        console.error(error);
      } finally {
        this.latestActivity.loading = false;
        this.latestActivityTop10.loading = false;
        this.mostPbsLast7Days.loading = false;
        this.mostPbsLastMonth.loading = false;
        this.mostTrackEntriesLast14Days.loading = false;
        this.mostTrackEntriesLastMonth.loading = false;
      }
    }, getDateDifference
  },
  created() {
    this.latestActivity.loading = true;
    this.latestActivityTop10.loading = true;
    this.mostPbsLast7Days.loading = true;
    this.mostPbsLastMonth.loading = true;
    this.mostTrackEntriesLast14Days.loading = true;
    this.mostTrackEntriesLastMonth.loading = true;
    this.fetchData()
  }
})
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
