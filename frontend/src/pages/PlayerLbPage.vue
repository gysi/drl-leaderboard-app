<template>
  <q-page padding style="height: 100%" class="col items-start">
      <q-table
        title="Player's Rankings"
        :columns="columns"
        :rows="rows"
        :loading="loading"
        row-key="playerName"
        class="my-sticky-header-table"
        table-class="col-auto"
        style="max-height: 100%;"
        :pagination="pagination"
        hide-pagination
        :visible-columns="[]"
      >
        <template v-slot:top-left>
          <div class="row">
            <div class="text-h4">{{ searchText?.toUpperCase() || 'Player' }}'s Rankings</div>
            <q-select
              ref="userselect"
              filled
              v-model="searchText"
              use-input
              hide-dropdown-icon
              autofocus
              input-debounce="150"
              :options="searchResults"
              @filter="search"
              @new-value="onEnterPressed"
              @update:model-value="fetchData"
              style="width: 250px"
              class="q-ml-md"
              use-chips
              label="Enter player name"
            >
              <template v-slot:no-option>
                <q-item
                  @click="fetchData"
                >
                  <q-item-section class="text-grey">
                    No results
                  </q-item-section>
                </q-item>
              </template>
            </q-select>
          </div>
        </template>
        <template v-slot:header="props">
          <q-tr
          >
            <q-th
              v-for="col in props.cols"
              :key="col.name"
              :props="props"
              :style="{
                border: '1px solid black',
                borderLeft: '1px solid black',
                borderRight: 0,
              }"
            >{{ col.label }}</q-th>
          </q-tr>
        </template>
        <template v-slot:body="props">
          <q-tr
            :props="props"
            :key="`m_${props.rowIndex}`"
          >
            <q-td
              v-for="col in props.cols"
              :key="col.name"
              :props="props"
              :style="{
                backgroundColor: props.row.isInvalidRun ?
                  'rgba(187,44,44,0.54)': col.name === 'position' ? backGroundColorByPosition(props.row.position) : null,
                // borderTop: props.row.position === 1 ? rows[props.rowIndex-1]?.position === 1 ? 0 : '2px solid #FFED02' : '1px solid black',
                // borderBottom : props.row.position === 1 ? '2px solid #FFED02' : 0,
              }"
              :class="col.name === 'position' && !props.row.isInvalidRun ? props.row.position === 1 ? 'first-place' : props.row.position === 2 ? 'second-place' : props.row.position === 3 ? 'third-place' : '' : ''"
            >
              <q-icon
                v-if="props.row.isInvalidRun && col.name === 'position'"
                name="warning"
                size="sm"
                left
              >
                <q-tooltip>
                  {{ props.row.invalidRunReason }}
                </q-tooltip>
              </q-icon>
              {{ col.value }}
            </q-td>
          </q-tr>
        </template>
      </q-table>
  </q-page>
</template>

<script>
import axios from 'axios';
import { intervalToDuration, formatDuration } from 'date-fns'

export default {
  name: 'PlayerLbPage',
  created(){
    this.searchText = this.$router.currentRoute.value.query.playerName;
    this.fetchData(this.searchText);
  },
  data() {
    return {
      searchText: null,
      searchResults: [],
      loadingState: true,
      columns: [
        { name: 'position', label: '#', field: 'position', align: 'center', required: true },
        { name: 'track', label: 'Track', field: row => row.track.name, align: 'left', required: true },
        { name: 'map', label: 'Map', field: row => row.track.mapName, align: 'left', required: true },
        { name: 'score', label: 'Time', field: 'score',
          format: (val, row) => this.formatMilliSeconds(val),
          align: 'left', required: true },
        { name: 'crashes', label: 'Crashes', field: 'crashCount', required: true },
        { name: 'topSpeed', label: 'Top Speed', field: 'topSpeed', format: (val, row) => (Math.round(val*10)/10), required: true },
        { name: 'points', label: 'Points', field: 'points', format: (val, row) => row.isInvalidRun ? 0 : Math.round(val), required: true },
        { name: 'createdAt', label: 'Time Set', field: 'createdAt', format: (val, row) => this.getDateDifference(val), required: true },
        { name: 'droneName', label: 'Drone Name', field: 'droneName', required: true },
        { name: 'isInvalidRun', label: 'Invalid Run', field: 'isInvalidRun'},
        { name: 'invalidRunReason', label: 'Invalid Run Reason', field: 'invalidRunReason'},
        { name: 'mapCatetory', label: 'Map Category', field: row => row.track.parentCategory, required: true },
      ],
      rows: [],
      loading: false,
      pagination: {
        rowsPerPage: 300,
      },
    }
  },
  // {
//   "id": 11008,
//   "position": 9,
//   "createdAt": "2022-11-12T20:21:12.137",
//   "score": 62209,
//   "playerName": "gysi",
//   "invalidRunReason": null,
//   "topSpeed": 99.1491547,
//   "isInvalidRun": false,
//   "track": {
//   "name": "LONDON EXPO",
//     "id": 111,
//     "mapName": "2017 WORLD CHAMPIONSHIP",
//     "parentCategory": "DRL MAPS"
// },
//   "crashCount": 0,
//   "points": 179.53115501748678,
//   "droneName": "DRL Racer4",
//   "beatenBy": [
//   {
//     "id": 17708,
//     "position": 7,
//     "createdAt": "2023-01-04T19:50:08.689",
//     "score": 62122,
//     "playerName": "chrism",
//     "points": 183.06079178697712
//   },
//   {
//     "id": 11006,
//     "position": 6,
//     "createdAt": "2022-11-19T19:57:56.912",
//     "score": 61911,
//     "playerName": "sebafpv",
//     "points": 184.8279405441204
//   }
// ]
// }
  methods: {
    onEnterPressed(val) {
      this.$refs.userselect.toggleOption(val);
    },
    async search(val, update, abort) {
      console.log("search triggerd");
      console.log(val);
      if(val === ""){
        console.log("empty search");
        this.searchResults = [];
        abort();
        return;
      }
      this.loadingState = true;
      const response = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/findPlayers?playerName='+val);
      this.searchResults = response.data;
      this.loadingState = false;
      update();
    },
    async fetchData(player) {
      if(!player){
        this.rows = [];
        return;
      }
      console.log("fetching!");
      console.log(player);
      this.$router.replace({
        query: {
          playerName: player
        }
      });
      this.loading = true;
      try {
        const response = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/byplayername?playerName='+player);
        this.rows = response.data;
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
    },
    backGroundColorByPosition(position){
      if (position > 75) {
        return '#4B4B4B'
      }
      if (position > 50) {
        return '#234918'
      }
      if (position > 25) {
        return '#326722'
      }
      if (position > 10) {
        return '#40832d'
      }
      if (position > 5) {
        return '#54ab3b'
      }
      if (position > 2) {
        return 'rgb(187,107,33)' //rgb(221,127,39)
      }
      if (position > 1) {
        return 'rgba(106,105,110,0.5)' //rgb(160,158,165)
      }
      return 'rgba(180,135,22,0.95)'; //rgb(236,160,28)
    },
    formatMilliSeconds(milliseconds){
      // Create a new date object to manipulate the time
      const date = new Date(milliseconds);

      // Get the minutes, seconds, and milliseconds from the date object
      let minutes = date.getMinutes().toString().padStart(2, '0');
      let seconds = date.getSeconds().toString().padStart(2, '0');
      let millis = date.getMilliseconds().toString().padStart(3, '0');

      // Return the formatted string
      return `${minutes}:${seconds}.${millis}`;
    },
    getDateDifference(dateString) {
      // 2022-08-30T02:14:25.042
      let duration = intervalToDuration({
        start: new Date(dateString+'Z'),
        end: new Date()
      });
      let units = [];
      if(duration.months > 0 ){
        units.push('months');
      } else if(duration.days > 0 ){
        units.push('days');
      } else if(duration.hours > 0 ){
        units.push('hours');
      } else if(duration.minutes > 0 ){
        units.push('minutes');
      } else {
        units.push('seconds');
      }
      return formatDuration(duration, { format: units }) + ' ago';
    }
  }
}
</script>

<style lang="sass" scoped>
tbody .q-td
  color: black
  background-color: $secondary
  border-left: 1px solid black
  border-right: 0
  border-top: 1px solid black
  border-bottom : 0
  font-weight: bold
  font-size: 16px

tbody .q-tr .q-td:first-child
  color: #f6f6f6
  text-shadow: 1px 0px 0.2px black, -1px 0px 0.2px black, 0px 1px 0.2px black, 0px -1px 0.2px black
</style>
