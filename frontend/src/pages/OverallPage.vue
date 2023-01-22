<template>
  <q-page padding style="height: 100%" class="q-pa-md row items-start">
    <q-table
      title="Overall Rankings"
      :columns="columns"
      :rows="rows"
      :loading="loading"
      :pagination="pagination"
      row-key="position"
      class="col-auto my-sticky-header-table"
      style="max-height: 100%; min-width: 1100px"
      flat
      bordered
      :visible-columns="[]"
      :rows-per-page-options="[50]"
    >
      <template v-slot:body="props">
        <q-tr>
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
            :style="{
              backgroundColor: props.row.isInvalidRun ?
                'rgba(187,44,44,0.54)': col.name === 'position' ? backGroundColorByPosition(props.row.position) : null
              }"
            :class="[col.name === 'position' && !props.row.isInvalidRun ?
              props.row.position === 1 ? 'first-place' :
              props.row.position === 2 ? 'second-place' :
              props.row.position === 3 ? 'third-place' : '' : '', col.name === 'position' ? 'leaderboard-position-column' : '']"
          >
            <q-item v-if="col.name === 'playerName'"
              clickable
              :to="`/playerlb/?playerName=${props.row.playerName}`"
              class="q-item-player-region"
            >
              <q-item-section avatar side>
                <q-avatar rounded size="50px">
<!--                  <img :src="props.row.profileThumb" loading="eager" alt="Avatar"/>-->
                  <q-img :src="props.row.profileThumb" />
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="player-item-label">{{ props.row.playerName }}</q-item-label>
                <q-item-label caption>
                  <span :class="`fi fi-${props.row.flagUrl}`"></span>
<!--                    <q-img loading="lazy" :src="props.row.flagUrl" class="player-avatar-img"/>-->
                  <q-badge
                    :class="`badge-platform q-batch-${props.row.profilePlatform}`"
                  >{{ props.row.profilePlatform }}</q-badge>
                </q-item-label>
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
import { defineComponent } from 'vue'
import axios from 'axios';
import { formatMilliSeconds, backGroundColorByPosition, getDateDifference } from 'src/modules/LeaderboardFunctions'


export default defineComponent({
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
        { name: 'avgPosition', label: 'Average Position', field: 'avgPosition', align: 'right', format: (val, row) => (Math.round(val*100)/100), required: true },
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
        const response = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/overallranking?page=1&limit=500');
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
})
</script>

<style lang="sass" scoped>
tbody .q-td
  font-size: 16px

tbody .q-item
  padding: 0
  padding-right: 10px

.player-item-label
  font-size: 20px

.player-avatar-img
  width: 25px
  height: 13px

.q-item-player-region
  background: rgba(0, 0, 0, 0.05)

.fi
  height: 14px
  width: 19px

.badge-platform
  margin-left: 5px
  font-size: 10px
  line-height: 10px
  padding-right: 2px
  padding-left: 2px

.q-batch-Steam
  background-color: rgb(25,91,127)

.q-batch-Epic
  background-color: black

.q-batch-Playstation
  background-color: rgb(0,65,151)

.q-batch-Xbox
  background-color: rgb(16,120,15)
</style>
