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
      style="max-height: 100%; min-width: 1100px"
      flat
      bordered
      :visible-columns="[]"
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
            <q-item v-if="col.name === 'playerName'"
              clickable
              :to="`/playerlb/?playerName=${props.row.playerName}`"
              style="background: rgba(0, 0, 0, 0.05);"
            >
              <q-item-section avatar side>
                <q-avatar rounded size="50px">
                  <img :src="props.row.profileThumb" />
                </q-avatar>
              </q-item-section>
              <q-item-section>
                {{ props.row.playerName }}
                <q-badge floating
                         :style="{backgroundColor: props.row.profilePlatform === 'Steam' ? 'rgb(25,91,127)' :
                                  props.row.profilePlatform === 'Epic' ? 'black' :
                                   props.row.profilePlatform === 'Playstation' ? 'rgb(0,65,151)' : 'rgb(16,120,15)'}"
                >{{ props.row.profilePlatform }}</q-badge>
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
        { name: 'position', label: '#', field: 'position', required: true },
        { name: 'playerName', label: 'Player', field: 'playerName', align: 'left', required: true },
        { name: 'totalPoints', label: 'Points', field: 'totalPoints', align: 'right', required: true },
        { name: 'invalidRuns', label: 'Invalid Runs', field: 'invalidRuns', align: 'center', required: true },
        { name: 'completedTracks', label: 'Completed Tracks', field: 'completedTracks', align: 'center', required: true },
        { name: 'totalCrashCount', label: 'Crashes', field: 'totalCrashCount', align: 'center', required: true },
        { name: 'totalScore', label: 'Total Time', field: 'totalScore', format: (val, row) => this.formatMilliSeconds(val), align: 'right', required: true },
        { name: 'maxTopSpeed', label: 'Top Speed', field: 'maxTopSpeed', format: (val, row) => (Math.round(val*10)/10), required: true},
        { name: 'profileThumb', label: 'Profile Thumb', field: 'profileThumb'}
      ],
      rows: [],
      loading: false
    }
  },

  methods: {
    async fetchData() {
      this.loading = true;
      try {
        const response = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/overallranking?page=1&limit=1000');
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

tbody .q-item
  padding: 0
  padding-right: 10px
</style>
