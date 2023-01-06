<template>
  <q-page padding style="height: 100%" class="row items-start">
    <q-table
      title="Overall Rankings"
      :columns="columns"
      :rows="rows"
      :loading="loading"
      :pagination="pagination"
      row-key="playerName"
      class="col-auto my-sticky-header-table"
      style="max-height: 80%;"
      flat
      bordered
    />
  </q-page>
</template>

<script>
import axios from 'axios';

export default {
  name: 'OverallPage',
  data() {
    return {
      pagination: {
        rowsPerPage: 50,
      },
      columns: [
        { name: 'position', label: '#', field: 'position' },
        { name: 'playerName', label: 'Player', field: 'playerName' },
        { name: 'totalPoints', label: 'Points', field: 'totalPoints' },
        { name: 'invalidRuns', label: 'Invalid Runs', field: 'invalidRuns' },
        { name: 'completedTracks', label: 'Completed Tracks', field: 'completedTracks' },
        { name: 'totalCrashCount', label: 'Crashes', field: 'totalCrashCount' },
        { name: 'totalScore', label: 'Total Time', field: 'totalScore' },
        { name: 'maxTopSpeed', label: 'Top Speed', field: 'maxTopSpeed' },
      ],
      rows: [],
      loading: false
    }
  },

  methods: {
    async fetchData() {
      this.loading = true;
      try {
        const response = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/overallranking?page=1&limit=50');
        this.rows = response.data;
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
    }
  },
  created() {
    this.fetchData();
    // this.interval = setInterval(() => {
    //   this.fetchData();
    // }, 10000); // refresh data every 10 seconds
  },
  mounted() {
  },
  beforeUnmount() {
    clearInterval(this.interval);
  }
}
</script>

<style lang="sass">
.my-sticky-header-table
  /* height or max-height is important */
  //min-height: inherit

  .q-table__top,
  .q-table__bottom,
  thead tr:first-child th
    /* bg color is important for th; just specify one */
    background-color: $primary
    color: white

  thead tr th
    position: sticky
    z-index: 1
  thead tr:first-child th
    top: 0

  /* this is when the loading indicator appears */
  &.q-table--loading thead tr:last-child th
    /* height of all previous header rows */
    top: 48px
</style>
