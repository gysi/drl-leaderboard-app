<template>
  <q-page padding style="height: 100%" class="col items-start">
      <q-table
        title="Player's Rankings"
        :columns="columns"
        :rows="rows"
        :loading="loading"
        row-key="track.id"
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
              dark
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
            :key="props.row.track.id"
          >
            <q-td
              v-for="col in props.cols"
              :key="col.name"
              :props="props"
              :style="{
                backgroundColor:
                  props.row.isMissing ? 'rgb(204,204,204)' :
                  props.row.isInvalidRun ? 'rgba(187,44,44,0.54)' :
                    col.name === 'position' ? backGroundColorByPosition(props.row.position) :
                      null,
              }"
              :class="col.name === 'position' && !props.row.isInvalidRun ? props.row.position === 1 ? 'first-place' : props.row.position === 2 ? 'second-place' : props.row.position === 3 ? 'third-place' : '' : ''"
            >
<!-- Position row-->
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
<!-- Position row end-->
<!-- Track row -->
              <q-item clickable v-if="col.name === 'track'" class="playerlb-track-td"
                      :to="`/tracklb/?trackId=${props.row.track.id}`"
              >
                <q-item-section>
                  <q-item-label>{{ props.row.track.name }}</q-item-label>
                  <q-item-label caption>
                    <q-chip dense color="grey-6">{{ props.row.track.mapName }}</q-chip>
                    <q-chip dense color="grey-8" dark>{{ props.row.track.parentCategory }}</q-chip>
                  </q-item-label>
                </q-item-section>
              </q-item>
<!-- Track row end -->
              {{ col.name !== 'track' ? col.value : '' }}
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

function compareTracks(track1, track2) {
  if (track1.mapName !== track2.mapName) {
    return track1.mapName < track2.mapName;
  }
  if (track1.parentCategory !== track2.parentCategory) {
    return track1.parentCategory < track2.parentCategory;
  }
  return track1.name < track2.name;
}

export default defineComponent({
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
        // { name: 'map', label: 'Map', field: row => row.track.mapName, align: 'left', required: true },
        { name: 'score', label: 'Time', field: 'score',
          format: (val, row) => { if(val) return this.formatMilliSeconds(val) }, align: 'left', required: true },
        { name: 'crashes', label: 'Crashes', field: 'crashCount', required: true },
        { name: 'topSpeed', label: 'Top Speed', field: 'topSpeed',
          format: (val, row) => { if(val) return Math.round(val*10)/10 }, required: true },
        { name: 'points', label: 'Points', field: 'points',
          format: (val, row) => { if (val) return row.isInvalidRun ? 0 : Math.round(val) }, required: true },
        { name: 'createdAt', label: 'Time Set', field: 'createdAt', format: (val, row) => this.getDateDifference(val), required: true },
        { name: 'droneName', label: 'Drone Name', field: 'droneName', required: true },
        { name: 'isInvalidRun', label: 'Invalid Run', field: 'isInvalidRun'},
        { name: 'invalidRunReason', label: 'Invalid Run Reason', field: 'invalidRunReason'},
        // { name: 'mapCatetory', label: 'Map Category', field: row => row.track.parentCategory, required: true },
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
        const [ responseFinishedTracks, responseMissingTracks] =
          await Promise.all([
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/byplayername?playerName='+player),
            axios.get(process.env.DLAPP_API_URL+'/tracks/missingtracksbyplayername?playerName='+player)
          ]);
        const finishedTracks = responseFinishedTracks.data;
        const missingTracks = responseMissingTracks.data;
        let newArray = [];
        let i = 0;
        let j = 0;
        while (i < finishedTracks.length && j < missingTracks.length) {
          if (compareTracks(finishedTracks[i].track, missingTracks[j])){
            newArray.push(finishedTracks[i]);
            i++;
          } else {
            newArray.push({ isMissing: true, track: missingTracks[j] });
            j++;
          }
        }
        this.rows = newArray.concat(finishedTracks.slice(i), missingTracks.slice(j).map(x => { return { isMissing: true, track: x } }));
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
    },
    formatMilliSeconds,
    backGroundColorByPosition,
    getDateDifference
  }
})
</script>

<style lang="sass" scoped>
:deep(tbody .q-td)
  font-size: 16px

:deep(.q-field__input)
  color: white

.playerlb-track-td
  padding: 0
  background: rgba(0, 0, 0, 0.05)

.playerlb-track-td .q-chip
  margin-left: 0
  margin-top: 0
  margin-bottom: 0
</style>
