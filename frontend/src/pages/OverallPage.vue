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
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
            :style="{
              backgroundColor: props.row.isInvalidRun ?
                'rgba(187,44,44,0.54)': col.name === 'position' ? backGroundColorByPosition(props.row.position) : null
              }"
            :class="col.name === 'position' && !props.row.isInvalidRun ?
              props.row.position === 1 ? 'first-place' :
              props.row.position === 2 ? 'second-place' :
              props.row.position === 3 ? 'third-place' : '' : ''"
          >
            <q-item v-if="col.name === 'playerName'">
              <q-item-section avatar v-ripple>
                <q-btn
                  outline
                  dense
                  rounded
                  icon="person"
                  @click="() => router.push({ name: 'playerlb', query: { playerName: props.row.playerName } })"
                />
              </q-item-section>
              <q-item-section>
                {{ props.row.playerName }}
              </q-item-section>
            </q-item>
            {{ col.name !== 'playerName' ? col.value : '' }}
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </q-page>
</template>

<script>
import axios from 'axios';
import { formatMilliSeconds, backGroundColorByPosition, getDateDifference } from 'src/modules/LeaderboardFunctions'


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
        { name: 'playerName', label: 'Player', field: 'playerName', align: 'left' },
        { name: 'totalPoints', label: 'Points', field: 'totalPoints', align: 'right' },
        { name: 'invalidRuns', label: 'Invalid Runs', field: 'invalidRuns', align: 'center' },
        { name: 'completedTracks', label: 'Completed Tracks', field: 'completedTracks', align: 'center' },
        { name: 'totalCrashCount', label: 'Crashes', field: 'totalCrashCount', align: 'center' },
        { name: 'totalScore', label: 'Total Time', field: 'totalScore', format: (val, row) => this.formatMilliSeconds(val), align: 'right' },
        { name: 'maxTopSpeed', label: 'Top Speed', field: 'maxTopSpeed', format: (val, row) => (Math.round(val*10)/10), },
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
    },
    formatMilliSeconds, backGroundColorByPosition, getDateDifference
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
  },
}
</script>

<style lang="sass" scoped>
tbody .q-td
  font-size: 16px
</style>
