<template>
  <q-page padding style="height: 100%" class="q-pa-md col items-start">
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
        bordered
        flat
      >
        <template v-slot:top-left>
          <div class="row">
            <div class="q-table__title">{{ playerSelect.searchText?.toUpperCase() || 'Player' }}'s Rankings</div>
            <q-select
              ref="userselect"
              filled
              v-model="playerSelect.searchText"
              use-input
              hide-dropdown-icon
              autofocus
              input-debounce="150"
              :options="playerSelect.searchResults"
              @filter="searchSelectPlayer"
              @new-value="onEnterPressedPlayerSelect"
              @update:model-value="onSelectPlayer"
              style="width: 175px"
              class="q-ml-md"
              use-chips
              label="Enter player name"
            >
              <template v-slot:no-option>
                <q-item>
                  <q-item-section class="text-grey">
                    No results
                  </q-item-section>
                </q-item>
              </template>
            </q-select>
            <q-select
              ref="compareSelect"
              filled
              v-model="compareSelect.searchText"
              use-input
              hide-dropdown-icon
              :autofocus="false"
              input-debounce="150"
              max-values="5"
              :options="compareSelect.searchResults"
              @filter="searchSelectComparePlayer"
              @new-value="onEnterPressedCompareSelect"
              @update:model-value="onCompareSelect"
              :style="{
                minWidth: '220px'
              }"
              class="q-ml-md"
              use-chips
              multiple
              label="Compare against players"
            >
              <template v-slot:no-option>
                <q-item>
                  <q-item-section class="text-grey">
                    No results
                  </q-item-section>
                </q-item>
              </template>
            </q-select>
          </div>
        </template>
        <template v-slot:header-cell-beatenBy="props">
            <q-th :props="props">
              <q-btn type="a" icon="help" size="1.3rem"
                     fab flat padding="5px"
                     :to="{ name: 'faq', query: { card: 'beatenBy' } }"
              />
              <q-icon
                name="query_stats" color="white" size="sm" />
            </q-th>
        </template>
        <template v-slot:header-cell-points="props">
          <q-th :props="props">
            {{ props.col.label }}
            <q-btn type="a" icon="help" size="1.3rem"
                   fab flat padding="5px"
                   :to="{ name: 'faq', query: { card: 'pointSystem' } }"
            />
          </q-th>
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
                  props.row.isMissing ? 'var(--app-player-lb-missing-run-background-color)' :
                  props.row.isInvalidRun ? 'rgba(187,44,44,0.54)' :
                    col.name === 'position' ? backGroundColorByPosition(props.row.position) :
                      null,
                paddingLeft: props.row.isInvalidRun && col.name === 'position' ? '5px' : null,
              }"
              :class="['td-borders-font-size16', col.name === 'position' && !props.row.isInvalidRun ?
                props.row.position === 1 ? 'first-place' :
                  props.row.position === 2 ? 'second-place' :
                    props.row.position === 3 ? 'third-place' : '' : '', col.name === 'position' ? 'leaderboard-position-column' : '']"
            >
<!-- Position row-->
              <q-btn
                v-if="props.row.isInvalidRun && col.name === 'position'"
                type="button" icon="warning" size="sm"
                fab padding="5px"
                :to="{ name: 'faq', query: { card: 'invalidRuns' } }"
                ripple
              >
                <q-tooltip>
                  <div v-html="props.row.invalidRunReason.replaceAll(',', '</br>')"></div>
                </q-tooltip>
              </q-btn>
<!-- Position row end-->
<!-- Track row -->
              <q-item clickable v-if="col.name === 'track'" class="playerlb-track-td"
                      :to="`/track-lb/?trackId=${props.row.track.id}`"
              >
                <q-item-section>
                  <q-item-label>{{ props.row.track.name }}</q-item-label>
                  <q-item-label caption>
                    <q-chip dense class="track-chip-map">{{ props.row.track.mapName }}</q-chip>
                    <q-chip dense class="track-chip-parentcategory">{{ props.row.track.parentCategory }}</q-chip>
                  </q-item-label>
                </q-item-section>
              </q-item>
<!-- Track row end -->
<!-- Beaten By row -->
              <q-icon
                v-if="!props.row.isMissing && col.name === 'beatenBy'"
                :name="beatenByIcon(props.row.beatenBy, props.row.createdAt)[0]"
                :color="beatenByIcon(props.row.beatenBy, props.row.createdAt)[1]"
                size="md"
                style="background: rgba(0,0,0,0.07); border-radius: 50%; padding: 2px;"
              >
                <q-tooltip v-if="props.row.beatenBy.length === 0">
                  <div style="font-size: 1rem">
                    {{ beatenByIcon(props.row.beatenBy, props.row.createdAt)[2] }}
                  </div>
                </q-tooltip>
                <q-tooltip v-if="props.row.beatenBy.length > 0">
                  <div style="font-size: 1rem">
                    Got beaten by
                    {{ props.row.beatenBy.length >= 5 ? '5 or more' : props.row.beatenBy.length }}
                    player{{ props.row.beatenBy.length > 1 ? 's' : ''}} since submission
                  </div>
                  <q-table
                    :columns="beatenByTable.columns"
                    :rows="props.row.beatenBy"
                    dense
                    hide-bottom
                  >
                    <template v-slot:body-cell-scoreDiff="props2">
                      <td v-bind="props" class="text-right">
                        {{ this.substractAndformatMilliSeconds(props2.row.score, props.row.score) }}
                      </td>
                    </template>
                  </q-table>
                </q-tooltip>
              </q-icon>
<!-- Beaten By row end -->
<!-- compare -->
              <div v-if="col.name.startsWith('compare')">
                <div>{{ col.value }}</div>
                <div :style="{color: props.row.score < props.row.compare[col.label] ? 'green' : 'red'}">
                  {{ props.row.compare[col.label] != null && props.row.score != null ? this.substractAndformatMilliSeconds(props.row.compare[col.label],props.row.score) : '' }}
                </div>
              </div>
<!-- compare end -->
              {{ col.name !== 'track' && col.name !== 'beatenBy' && !col.name.startsWith('compare') ? col.value : '' }}
            </q-td>
          </q-tr>
        </template>
      </q-table>
  </q-page>
</template>

<script>
import { defineComponent, ref } from 'vue'
import axios from 'axios';
import { formatMilliSeconds, backGroundColorByPosition, getDateDifference, substractAndformatMilliSeconds } from 'src/modules/LeaderboardFunctions'
import { differenceInDays } from "date-fns";

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
  async created(){
    this.playerSelect.searchText = this.$router.currentRoute.value.query.playerName;
    if(!!this.playerSelect.searchText) {
      this.rows = ref(await this.fetchData(this.playerSelect.searchText));
    }
  },
  data() {
    return {
      playerSelect: {
        searchText: null,
        searchResults: [],
      },
      compareSelect: {
        searchText: null,
        searchResults: [],
        currentSelectedPlayers: [],
        rowsByPlayer: {}
      },
      columns: [
        { name: 'position', label: '#', field: 'position', align: 'right', sortable: true},
        { name: 'beatenBy', label: 'Beaten by', field: 'beatenBy', align: 'center', sortable: true, sort: this.sortByBeatenBy},
        { name: 'track', label: 'Track', field: row => row.track.name, sortable: true, align: 'left'},
        { name: 'score', label: 'Time', field: 'score',
          format: (val, row) => { if(val) return this.formatMilliSeconds(val) }, align: 'right', sortable: true },
        { name: 'crashes', label: 'Crashes', field: 'crashCount', sortable: true, required: true },
        { name: 'topSpeed', label: 'Top Speed', field: 'topSpeed',
          format: (val, row) => { if(val) return Math.round(val*10)/10 }, sortable: true, required: true },
        { name: 'points', label: 'Points', field: 'points',
          format: (val, row) => { if (val) return row.isInvalidRun ? 0 : Math.round(val) }, sortable: true },
        { name: 'createdAt', label: 'Time Set', field: 'createdAt', format: (val, row) => this.getDateDifference(val), sortable: true },
        { name: 'droneName', label: 'Drone Name', field: 'droneName' },
      ],
      rows: [],
      loading: false,
      pagination: {
        rowsPerPage: 300,
      },
      beatenByTable: {
        columns: [
          { name: "playerName", label: 'Player', field: 'playerName', align: 'left', required: true },
          { name: "position",  label: 'Position', field: 'position', align: 'right', required: true },
          { name: 'score', label: 'Time', field: 'score',
            format: (val, row) => { if(val) return this.formatMilliSeconds(val) }, align: 'right', required: true },
          { name: 'scoreDiff', label: 'Time Diff', field: 'score', required: true },
          { name: "createdAt", label: 'Time Set', field: 'createdAt', align: 'right', format: (val, row) => this.getDateDifference(val), required: true },
        ],
      }
    }
  },
  methods: {
    onEnterPressedPlayerSelect(val) {
      this.$refs.userselect.toggleOption(val);
    },
    onEnterPressedCompareSelect(val) {
      this.$refs.compareSelect.toggleOption(val);
    },
    async searchSelectPlayer(val, update, abort){
      if(val === ""){
        this.playerSelect.searchResults = [];
        abort();
        return;
      }
      const response = await this.searchPlayer(val, update, abort);
      this.playerSelect.searchResults = response.data;
      update();
    },
    async searchSelectComparePlayer(val, update, abort){
      if(val === ""){
        this.compareSelect.searchResults = [];
        abort();
        return;
      }
      const response = await this.searchPlayer(val, update, abort);
      this.compareSelect.searchResults = response.data;
      update();
    },
    async searchPlayer(val, update, abort) {
      return axios.get(process.env.DLAPP_API_URL+'/leaderboards/find-players?playerName='+val);
    },
    async onSelectPlayer(player) {
      if(!player){
        this.rows = [];
        return;
      }
      this.$router.replace({
        query: {
          playerName: player
        }
      });
      const rows = await this.fetchData(player);
      if(!rows){
        this.rows = [];
        return;
      }
      this.rows = rows;
      this.mergeComparePlayers(this.compareSelect.currentSelectedPlayers);
    },
    async fetchData(player, ignoreDoubleTrackEntries = false) {
      if(!player){
        return [];
      }
      this.loading = true;
      let newArray = [];
      try {
        const [ responseFinishedTracks, responseMissingTracks] =
          await Promise.all([
            axios.get(process.env.DLAPP_API_URL+'/leaderboards/byplayername?playerName='+player),
            axios.get(process.env.DLAPP_API_URL+'/tracks/missing-tracks-by-playername?playerName='+player)
          ]);
        const finishedTracks = responseFinishedTracks.data;
        const missingTracks = responseMissingTracks.data;
        let i = 0;
        let j = 0;
        while (i < finishedTracks.length && j < missingTracks.length) {
          if (compareTracks(finishedTracks[i].track, missingTracks[j])){
            if(ignoreDoubleTrackEntries && i < finishedTracks.length - 1) {
              let after = finishedTracks[i + 1];
              if(after.track.id === finishedTracks[i].track.id){
                if(finishedTracks[i].isInvalidRun){
                  i++;
                  continue;
                } else {
                  newArray.push(finishedTracks[i]);
                  i = i + 2;
                  continue;
                }
              }
            }
            newArray.push(finishedTracks[i]);
            i++;
          } else {
            newArray.push({ isMissing: true, track: missingTracks[j] });
            j++;
          }
        }
        newArray = newArray.concat(finishedTracks.slice(i), missingTracks.slice(j).map(x => { return { isMissing: true, track: x } }));
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
      return newArray;
    },
    async onCompareSelect(players){
      const alreadyExistent = players.filter(p => this.compareSelect.currentSelectedPlayers.includes(p));
      const newPlayers = players.filter(p => !this.compareSelect.currentSelectedPlayers.includes(p));
      const removedPlayers = this.compareSelect.currentSelectedPlayers.filter(p => !players.includes(p));
      // console.log(`Already existent: ${alreadyExistent}`);
      // console.log(`New players: ${newPlayers}`);
      // console.log(`Removed players: ${removedPlayers}`);
      this.compareSelect.currentSelectedPlayers = [...alreadyExistent, ...newPlayers];
      // foreach removedPlayers, remove from compareRows
      // rowsByPlayer: {}
      if(removedPlayers.length > 0) {
        removedPlayers.forEach(p => {
          for (let i = 0; i < this.rows.length; i++) {
            delete this.rows[i]['compare'][p];
          }
          const index = this.columns.findIndex(c => c.name === `compare${p}`);
          if (index !== -1) {
            this.columns.splice(index, 1);
          }
        });
      }
      if(newPlayers.length > 0) {
        let promises = [];
        newPlayers.forEach(p => {
          promises.push(this.fetchData(p, true).then(rows => {
            this.compareSelect.rowsByPlayer[p] = rows;
          }))
        });
        await Promise.all(promises);
        this.mergeComparePlayers(newPlayers);
        // add columns
        const columnCrashesIdx = this.columns.findIndex(c => c.name === 'crashes');
        newPlayers.forEach(p => {
          const newColumn = {
            name: `compare${p}`,
            label: p,
            field: row => row.compare[p],
            format: (val, row) => { if(val) return this.formatMilliSeconds(val) },
            align: 'right',
          };
          this.columns.splice(columnCrashesIdx, 0, newColumn);
        });

      }
    },
    mergeComparePlayers(comparePlayers){
      if(!comparePlayers) return;
      for (let rowI = 0; rowI < this.rows.length; rowI++) {
        if(!this.rows[rowI]['compare']){
          this.rows[rowI]['compare'] = {};
        }
        for (let compareI = 0; compareI < comparePlayers.length; compareI++) {
          this.rows[rowI]['compare'][comparePlayers[compareI]] =
            this.compareSelect.rowsByPlayer[comparePlayers[compareI]][rowI].score
        }
      }
    },
    beatenByIcon(beatenBy, timeSet){
      if(!timeSet) return '';
      if(beatenBy.length >= 5) return ['arrow_downward', 'red', 'Beaten by 5 or more players'];
      if(beatenBy.length > 0) return ['trending_down', 'red', `Beaten by ${beatenBy.length} players`];
      let days = differenceInDays(new Date(), new Date(timeSet+'Z'));
      if(days <= 7){
        return ['check', 'green', `Not beaten by anyone yet for ${days} days`];
      }
      if(days <= 14 ){
        return ['auto_graph', 'green', `Not beaten by anyone yet for ${days} days`];
      }
      if(days <= 30){
        return ['surfing', 'green-6', `Not beaten by anyone yet for ${days} days`];
      }
      return ['military_tech', 'yellow-9', `Not beaten by anyone yet for ${days} days`];
    },
    sortByBeatenBy(a, b, rowa, rowb){
      // shouldn't happen, but just to make sure its not null
      if(a == null && b == null){
        return 0;
      } else if(a == null){
        return -1;
      } else if(!b == null){
        return 1;
      }
      if (a.length === 0 && b.length === 0) {
        const aDate = new Date(rowa.createdAt+'Z');
        const bDate = new Date(rowb.createdAt+'Z');
        if(aDate < bDate) {
          return -1;
        } else {
          return 1;
        }
      } else if (a.length === 0){
        return -1;
      } else if (b.length === 0){
        return 1;
      }

      if (a.length === b.length){
        const aDate = new Date(a[0].createdAt+'Z');
        const bDate = new Date(b[0].createdAt+'Z');
        if(aDate < bDate) {
          return 1;
        } else {
          return -1;
        }
      } else if (a.length < b.length){
        return -1;
      } else {
        return 1;
      }
    },
    formatMilliSeconds,
    backGroundColorByPosition,
    getDateDifference,
    substractAndformatMilliSeconds
  }
})
</script>

<style lang="sass" scoped>
:deep(tbody .q-td)
  font-size: 16px

.playerlb-track-td
  padding: 0

.playerlb-track-td .q-chip
  margin-left: 0
  margin-top: 0
  margin-bottom: 0
</style>
