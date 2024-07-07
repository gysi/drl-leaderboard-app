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
          <div class="q-table__title q-mr-md q-mb-sm">{{ playerSelect.searchText.value?.toUpperCase() || 'Player' }}'s Rankings</div>
          <div class="row q-gutter-md">
            <q-select
              ref="userselect"
              filled
              v-model="playerSelect.searchText.value"
              use-input
              hide-dropdown-icon
              autofocus
              input-debounce="150"
              :options="playerSelect.searchResults.value"
              @filter="searchSelectPlayer"
              @new-value="onEnterPressedPlayerSelect"
              @update:model-value="onSelectPlayer"
              style="width: 175px"
              class="q-ml-md"
              use-chips
              label="Enter player name"
              popup-content-class="q-menu-dropdown"
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
              ref="compareSelectRef"
              filled
              v-model="compareSelect.searchText.value"
              use-input
              hide-dropdown-icon
              :autofocus="false"
              input-debounce="150"
              max-values="5"
              :options="compareSelect.searchResults.value"
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
              popup-content-class="q-menu-dropdown"
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
        </div>
      </template>
      <template v-slot:header-cell-beatenBy="props">
        <q-th :props="props">
          <q-btn type="a" icon="help" size="1.3rem"
                 fab flat padding="5px"
                 :to="{ name: 'faq', query: { card: 'beatenBy' } }"
          />
          <q-icon
            name="query_stats" color="white" size="sm"/>
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
                    props.row.timePenaltyTotal > 0 ? 'rgba(187,175,44,0.54)':
                      col.name === 'position' ? backGroundColorByPosition(props.row.position) :
                        null,
                paddingLeft: (props.row.isInvalidRun || props.row.timePenaltyTotal > 0) && col.name === 'position' ? '5px' : null,
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
              :label="props.row.position"
              style="width: 52px; position: relative"
            >
              <q-tooltip>
                <div v-html="props.row.invalidRunReason.replaceAll(',', '</br>')"></div>
              </q-tooltip>
            </q-btn>
            <q-btn
              v-if="props.row.timePenaltyTotal > 0 && col.name === 'position'"
              type="button" icon="warning" size="sm"
              fab padding="5px"
              :to="{ name: 'faq', query: { card: 'invalidRuns' } }"
              ripple
              :label="props.row.position"
              style="width: 52px; position: relative"
            >
              <q-tooltip>
<!--                <div v-html="props.row.invalidRunReason.replaceAll(',', '</br>')"></div>-->
              </q-tooltip>
            </q-btn>
            <!-- Position row end-->
            <!-- Track row -->
            <q-item clickable v-if="col.name === 'track'" class="playerlb-track-td"
                    :to="`/track-leaderboard?trackId=${props.row.track.id}`"
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
                  player{{ props.row.beatenBy.length > 1 ? 's' : '' }} since submission
                </div>
                <q-table
                  :columns="beatenByTable.columns"
                  :rows="props.row.beatenBy"
                  dense
                  hide-bottom
                >
                  <template v-slot:body-cell-scoreDiff="props2">
                    <td v-bind="props" class="text-right">
                      {{ substractAndformatMilliSeconds(props2.row.score, props.row.score) }}
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
                {{
                  props.row.compare[col.label] != null && props.row.score != null ? substractAndformatMilliSeconds(props.row.compare[col.label], props.row.score) : ''
                }}
              </div>
            </div>
            <!-- compare end -->
            {{ col.name === 'track' || col.name === 'beatenBy' || col.name.startsWith('compare') || (col.name === 'position' && (props.row.isInvalidRun || props.row.timePenaltyTotal > 0)) ? '' : col.value }}
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </q-page>
</template>

<script setup>
import { ref, shallowRef } from 'vue'
import axios from 'axios';
import {
  backGroundColorByPosition, formatISODateTimeToDate,
  formatMilliSeconds,
  getDateDifference,
  substractAndformatMilliSeconds,
  sortByBeatenBy
} from 'src/modules/LeaderboardFunctions'
import {differenceInDays} from "date-fns"
import { useRouter } from 'vue-router'
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "Player Leaderboards - Offical tracks",
  meta: {
    description: {
      name: 'description',
      content: `Explore individual best times for the offical tracks by player. Find and compare performances seamlessly.`
    }
  }
})

const compareTracks = function (track1, track2) {
  if (track1.mapName !== track2.mapName) {
    return track1.mapName < track2.mapName;
  }
  if (track1.parentCategory !== track2.parentCategory) {
    return track1.parentCategory < track2.parentCategory;
  }
  return track1.name < track2.name;
}

const router = useRouter()
const playerSelect = {
  searchText: ref(router.currentRoute.value.query.playerName),
  searchResults: ref([]),
}

const compareSelectRef = ref();
const compareSelect = {
  searchText: ref(null),
  searchResults: ref([]),
  currentSelectedPlayers: ref([]),
  rowsByPlayer: ref({})
};

const rows = shallowRef([])
const loading = shallowRef(false)
const userSelect = ref()

const columns = ref([
  { name: 'position', label: '#', field: 'position', align: 'right', sortable: true },
  { name: 'beatenBy', label: 'Beaten by', field: 'beatenBy', align: 'center', sortable: true,
    sort: sortByBeatenBy },
  { name: 'track', label: 'Track', field: row => row.track.name, sortable: true, align: 'left' },
  { name: 'score', label: 'Time', field: 'score', align: 'right', sortable: true,
    format: (val, row) => {
      if (val) return formatMilliSeconds(val)
    }
  },
  { name: 'crashes', label: 'Crashes', field: 'crashCount', sortable: true, required: true },
  { name: 'topSpeed', label: 'Top Speed', field: 'topSpeed', sortable: true, required: true,
    format: (val, row) => {
      if (val) return Math.round(val * 10) / 10
    }
  },
  { name: 'points', label: 'Points', field: 'points', sortable: true,
    format: (val, row) => {
      if (val) return row.isInvalidRun ? 0 : Math.round(val)
    } },
  { name: 'createdAt', label: 'Time Set', field: 'createdAt', sortable: true,
    format: (val, row) => getDateDifference(val) },
  { name: 'droneName', label: 'Drone Name', field: 'droneName' },
])

const beatenByTable = {
  columns: [
    { name: "playerName", label: 'Player', field: row => row.player.playerName, align: 'left', required: true },
    { name: "position", label: 'Position', field: 'position', align: 'right', required: true },
    { name: 'score', label: 'Time', field: 'score', align: 'right', required: true,
      format: (val, row) => {
        if (val) return formatMilliSeconds(val)
      } },
    { name: 'scoreDiff', label: 'Time Diff', field: 'score', required: true },
    { name: "createdAt", label: 'Time Set', field: 'createdAt', align: 'right', required: true,
      format: (val, row) => getDateDifference(val) },
  ]
}

const pagination = {
  rowsPerPage: 300,
}

const onEnterPressedPlayerSelect = function (val) {
  userSelect.value.toggleOption(val);
}
const onEnterPressedCompareSelect = function (val) {
  compareSelectRef.value.toggleOption(val);
}

const searchPlayer = async function (val, update, abort) {
  return axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
    + `/players/search?playerName=${encodeURIComponent(val)}`
  );
}

const searchSelectPlayer = async function (val, update, abort) {
  if (val === "") {
    playerSelect.searchResults.value = [];
    abort();
    return;
  }
  const response = await searchPlayer(val, update, abort);
  playerSelect.searchResults.value = response.data;
  update();
}

const searchSelectComparePlayer = async function (val, update, abort) {
  if (val === "") {
    compareSelect.searchResults.value = [];
    abort();
    return;
  }
  const response = await searchPlayer(val, update, abort);
  console.log("RESPONSE!", response)
  compareSelect.searchResults.value = response.data;
  console.log(compareSelect.searchResults);
  update();
}

const fetchData = async function (player, ignoreDoubleTrackEntries = false) {
  if (!player) {
    return [];
  }
  loading.value = true;
  let newArray = [];
  try {
    const baseUrl = `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
    const [responseFinishedTracks, responseMissingTracks] =
      await Promise.all([
        axios.get(`${baseUrl}/leaderboards/official/byplayername?playerName=${encodeURIComponent(player)}`),
        axios.get(`${baseUrl}/tracks/missing-official-tracks-by-playername?playerName=${encodeURIComponent(player)}`)
      ]);
    const finishedTracks = responseFinishedTracks.data;
    const missingTracks = responseMissingTracks.data;
    let i = 0;
    let j = 0;
    while (i < finishedTracks.length && j < missingTracks.length) {
      if (compareTracks(finishedTracks[i].track, missingTracks[j])) {
        if (ignoreDoubleTrackEntries && i < finishedTracks.length - 1) {
          let after = finishedTracks[i + 1];
          if (after.track.id === finishedTracks[i].track.id) {
            if (finishedTracks[i].isInvalidRun) {
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
        newArray.push({isMissing: true, track: missingTracks[j]});
        j++;
      }
    }
    newArray = newArray.concat(finishedTracks.slice(i), missingTracks.slice(j).map(x => {
      return {isMissing: true, track: x}
    }));
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
  return newArray;
}

const onSelectPlayer = async function (player) {
  if (!player) {
    rows.value = [];
    return;
  }
  router.replace({
    query: {
      playerName: player
    }
  });
  const data = await fetchData(player);
  if (!data) {
    rows.value = [];
    return;
  }
  rows.value = data;
  mergeComparePlayers(compareSelect.currentSelectedPlayers.value);
}

const onCompareSelect = async function(players) {
  console.log("onCompareSelect")
  const alreadyExistent = players.filter(p => compareSelect.currentSelectedPlayers.value.includes(p));
  const newPlayers = players.filter(p => !compareSelect.currentSelectedPlayers.value.includes(p));
  const removedPlayers = compareSelect.currentSelectedPlayers.value.filter(p => !players.includes(p));
  compareSelect.currentSelectedPlayers.value = [...alreadyExistent, ...newPlayers];
  console.log(compareSelect.currentSelectedPlayers.value);
  if (removedPlayers.length > 0) {
    removedPlayers.forEach(p => {
      for (let i = 0; i < rows.value.length; i++) {
        delete rows.value[i]['compare'][p];
      }
      const index = columns.value.findIndex(c => c.name === `compare${p}`);
      if (index !== -1) {
        columns.value.splice(index, 1);
      }
    });
  }
  if (newPlayers.length > 0) {
    let promises = [];
    newPlayers.forEach(p => {
      promises.push(fetchData(p, true).then(rows => {
        compareSelect.rowsByPlayer.value[p] = rows;
      }))
    });
    await Promise.all(promises);
    console.log(newPlayers)
    console.log(compareSelect.rowsByPlayer.value)
    mergeComparePlayers(newPlayers);
    const columnCrashesIdx = columns.value.findIndex(c => c.name === 'crashes');
    newPlayers.forEach(p => {
      const newColumn = {
        name: `compare${p}`,
        label: p,
        field: row => row.compare[p],
        format: (val, row) => {
          if (val) return formatMilliSeconds(val)
        },
        align: 'right',
      };
      columns.value.splice(columnCrashesIdx, 0, newColumn);
    });

  }
}

const mergeComparePlayers = function(comparePlayers) {
  if (!comparePlayers) return;
  let offset = 0;
  for (let rowI = 0; rowI < rows.value.length; rowI++) {
    if (!rows.value[rowI]['compare']) {
      rows.value[rowI]['compare'] = {};
    }
    if (rowI > 0 && rows.value[rowI].track.id === rows.value[rowI - 1].track.id) {
      offset++;
    }
    if (rows.value[rowI].isInvalidRun) continue;
    for (let compareI = 0; compareI < comparePlayers.length; compareI++) {
      rows.value[rowI]['compare'][comparePlayers[compareI]] =
        compareSelect.rowsByPlayer.value[comparePlayers[compareI]][rowI - offset].score
    }
  }
}

const beatenByIcon = function (beatenBy, timeSet) {
  if (!timeSet) return '';
  if (beatenBy.length >= 5) return ['arrow_downward', 'red', 'Beaten by 5 or more players'];
  if (beatenBy.length > 0) return ['trending_down', 'red', `Beaten by ${beatenBy.length} players`];
  let days = differenceInDays(new Date(), new Date(timeSet + 'Z'));
  if (days <= 7) {
    return ['check', 'green', `Not beaten by anyone yet for ${days} days`];
  }
  if (days <= 14) {
    return ['auto_graph', 'green', `Not beaten by anyone yet for ${days} days`];
  }
  if (days <= 30) {
    return ['surfing', 'green-6', `Not beaten by anyone yet for ${days} days`];
  }
  return ['military_tech', 'yellow-9', `Not beaten by anyone yet for ${days} days`];
}

if (!!playerSelect.searchText.value) {
  fetchData(playerSelect.searchText.value).then((data) => { rows.value = data})
}
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
