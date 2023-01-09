<template>
  <q-page class="q-pa-md">
    <div class="row justify-start items-start q-col-gutter-md content-start">
      <div class="row col-12 justify-start items-start q-gutter-md">
        <q-table
          title="Latest Activity"
          :columns="latestActivity.columns"
          :rows="latestActivity.rows"
          :loading="latestActivity.loading"
          :pagination="latestActivity.pagination"
          :row-key="latestActivity.uniqueFunction"
          table-class="home-tables"
          flat
          bordered
          hide-bottom
          dense
        >
        </q-table>
        <q-table
          title="Latest Top10 Activity"
          :columns="latestActivity.columns"
          :rows="latestActivityTop10.rows"
          :loading="latestActivityTop10.loading"
          :pagination="latestActivityTop10.pagination"
          :row-key="latestActivity.uniqueFunction"
          flat
          bordered
          hide-bottom
          dense
        >
        </q-table>
      </div>
      <div class="row col-12 justify-start items-start q-gutter-md">
        <q-table
          title="Most entries last 7 days"
          :columns="mostPbsLast7Days.columns"
          :rows="mostPbsLast7Days.rows"
          :loading="mostPbsLast7Days.loading"
          :pagination="mostPbsLast7Days.pagination"
          row-key="playerName"
          flat
          bordered
          hide-bottom
          dense
        >
        </q-table>
        <q-table
          title="Most entries last month"
          :columns="mostPbsLast7Days.columns"
          :rows="mostPbsLastMonth.rows"
          :loading="mostPbsLastMonth.loading"
          :pagination="mostPbsLastMonth.pagination"
          row-key="playerName"
          flat
          bordered
          hide-bottom
          dense
        >
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
          { name: 'trackName', label: 'Track / Map / Category', field: (row) => `${row.trackName} / ${row.mapName} / ${row.parentCategory}`, align: 'left'},
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
      }

    }
  },
  methods: {
    async fetchData() {
      this.latestActivity.loading = true;
      this.latestActivityTop10.loading = true;
      this.mostPbsLast7Days.loading = true;
      this.mostPbsLastMonth.loading = true;
      try {
        const responseLatest = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/latestActivity');
        this.latestActivity.rows = responseLatest.data;
        const responseLatestTop10 = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/latestActivityTop10');
        this.latestActivityTop10.rows = responseLatestTop10.data;
        const responseMostPbsLast7Days = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/mostPbsLast7Days');
        this.mostPbsLast7Days.rows = responseMostPbsLast7Days.data;
        const responseMostPbsLastMonth = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/mostPbsLastMonth');
        this.mostPbsLastMonth.rows = responseMostPbsLastMonth.data;
      } catch (error) {
        console.error(error);
      } finally {
        this.latestActivity.loading = false;
        this.latestActivityTop10.loading = false;
        this.mostPbsLast7Days.loading = false;
        this.mostPbsLastMonth.loading = false;
      }
    }, getDateDifference
  },
  created() {
    this.fetchData()
  }
})
</script>

<style lang="sass" scoped>
:deep(.q-table tbody td)
  font-size: 16px

:deep(.q-table thead th)
  font-size: 18px

:deep(.q-table__top)
  background-color: $primary
  color: white
</style>
