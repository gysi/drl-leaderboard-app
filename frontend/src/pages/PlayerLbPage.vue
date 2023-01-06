<template>
  <q-page padding style="height: 100%" class="col items-start">
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
      <q-table
        title="Player's Rankings"
        :columns="columns"
        :rows="rows"
        :loading="loading"
        row-key="playerName"
        class="my-sticky-header-table"
        table-class="col-auto"
        style="max-height: 80%;"
        :pagination="pagination"
        hide-pagination
        :visible-columns="[]"
        separator="cell"
      >
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
            :style="{
              boxShadow: props.row.position === 1 ? 'inset 2px 2px 1px 1px red': '',
              border: props.row.position === 1 ? '4px solid red' : '',
            }">
            <q-td
              v-for="col in props.cols"
              :key="col.name"
              :props="props"
              :style="{
                backgroundColor: props.row.isInvalidRun ?
                  'rgba(187,44,44,0.54)': backGroundColorByPosition(props.row.position),
                color: '#f6f6f6', // #f6f6f6
                borderLeft: '1px solid black',
                borderRight: 0,
                borderTop: props.row.position === 1 ? rows[props.rowIndex-1].position === 1 ? 0 : '2px solid #FFED02' : '1px solid black',
                borderBottom : props.row.position === 1 ? '2px solid #FFED02' : 0,
                fontWeight: 'bold',
                textShadow: '1px 0px 0px black, -1px 0px 0px black, 0px 1px 0px black, 0px -1px 0px black', // black
                fontSize: '16px',
              }"
            >
              <q-icon
                v-if="props.row.isInvalidRun && col.name === 'track'"
                name="warning"
                size="sm"
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

export default {
  name: 'PlayerLbPage',
  data() {
    return {
      searchText: null,
      searchResults: [],
      loadingState: true,
      columns: [
        { name: 'track', label: 'Track', field: row => row.track.name, align: 'left', required: true },
        { name: 'map', label: 'Map', field: row => row.track.mapName, align: 'left', required: true },
        { name: 'position', label: '#', field: 'position', required: true },
        { name: 'crashes', label: 'Crashes', field: 'crashCount', required: true },
        { name: 'topSpeed', label: 'Top Speed', field: 'topSpeed', required: true },
        { name: 'points', label: 'Points', field: 'points', format: (val, row) => row.isInvalidRun ? 0 : Math.round(val), required: true },
        { name: 'createdAt', label: 'Time Set', field: 'createdAt', required: true },
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
      if(player === ""){
        this.rows = [];
        return;
      }
      console.log("fetching!");
      console.log(player);
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
      if (position > 3) {
        return '#6ad94b'
      }
      if (position > 1) {
        return '#7cfa58'
      }
      return '#d3c202';
    }
  }
}
</script>

<style lang="sass" scoped>
tbody .q-td
  color: #f6f6f6
  border-left: 1px solid black
  border-right: 0
  font-weight: bold
  text-shadow: 1px 0px 0.2px black, -1px 0px 0.2px black, 0px 1px 0.2px black, 0px -1px 0.2px black
  font-size: 16px

  td
    font-size: 200px

  /* this is when the loading indicator appears */
  &.q-table--loading thead tr:last-child th
    /* height of all previous header rows */
    top: 48px
</style>
