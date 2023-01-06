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
      style="max-height: 100%;"
      flat
      bordered
    >
      <template v-slot:body="props">
        <q-tr>
          <q-td v-for="col in props.cols" :key="col.name" :props="props">
            <q-item v-if="col.name === 'playerName'">
              <q-item-section>
                <q-btn
                  flat
                  dense
                  round
                  icon="person"
                  @click="() => router.push({ name: 'playerlb', query: { playerName: props.row.playerName } })"
                />
              </q-item-section>
              <q-item-section>
                {{ props.row.playerName }}
              </q-item-section>
            </q-item>
            <q-item v-else>
              <q-item-section>
                {{ props.row[col.name] }}
              </q-item-section>
            </q-item>
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </q-page>
</template>

<script>
import axios from 'axios';

export default {
  name: 'OverallPage',
  data() {
    return {
      log: console.log,
      router: this.$router,
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
