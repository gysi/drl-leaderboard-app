<template>
  <q-page padding class="q-pa-md items-start"
          style="height: 100%; max-height: 100%; display: grid; grid-template-rows: auto auto auto auto 1fr;">
      <div class="rounded-borders tournament-header-top header-toolbar row items-center q-pa-xs">
        <div class="doc-card-title q-my-xs q-mr-sm ">
          <span>{{ seasonDetails.hasQualification ? 'Qualification' : 'Rankings'}} - {{ season.name }}</span>
          <div class="text-caption text-right">
            <q-icon name="event"></q-icon>
            {{ formatISODateTimeToDate(season.startDate) }} -
            {{ formatISODateTimeToDate(seasonDetails.hasQualification ? seasonDetails.qualificationEndDate : season.endDate) }} UTC
          </div>
        </div>
        <PlayerSearchSelect @onPlayerSelected="onPlayerSelected" label="Jump to player name" class="q-ma-sm"
                            style="margin-left: 8px"/>
        <q-list bordered>
          <q-item class="q-px-sm">
            <q-item-section>
              <q-item-label>
                <q-toggle v-model="showExcludedPlayers" dense :color="Dark.isActive ? 'accent' : 'deep-purple'"/>
              </q-item-label>
              <q-item-label caption :style="{ color: Dark.isActive ? '' : 'rgba(255,255,255,0.98)'}">Show Excluded Players</q-item-label>
            </q-item-section>
            <q-item-section side top>
              <q-btn type="a" icon="help" size="1.3rem"
                     fab flat padding="0" :style="{ color: Dark.isActive ? '' : 'rgba(255,255,255,0.92)'}"
                     :to="{ name: 'faq', query: { card: 'excluded-from-season' } }"
              />
            </q-item-section>
          </q-item>

        </q-list>

      </div>
      <q-table
        ref="overallTable"
        :columns="columns"
        :rows="rows"
        :loading="loading"
        row-key="playerName"
        class="my-sticky-header-table"
        style="overflow: hidden; max-height: 100%"
        flat
        bordered
        :visible-columns="[]"
        :rows-per-page-options="[0]"
        virtual-scroll
        virtual-scroll-item-size="65"
        virtual-scroll-slice-ratio-before="5"
        virtual-scroll-slice-ratio-after="5"
        virtual-scroll-sticky-size-start="49"
        hide-bottom
        :filter="{ showExcludedPlayers: showExcludedPlayers }"
        :filter-method="filterMethod"
      >
        <template v-slot:header-cell-invalidRuns="props">
          <th :class="props.col.__thclass">
            {{ props.col.label }}
            <q-btn type="a" icon="help" size="1.3rem"
                   fab flat padding="5px"
                   :to="{ name: 'faq', query: { card: 'invalidRuns' } }"
            />
          </th>
        </template>
        <template v-slot:header-cell-prize="props">
          <th v-if="showPrizeColumn" :class="props.col.__thclass">
            {{ props.col.label }}
            <q-btn type="a" icon="help" size="1.3rem"
                   fab flat padding="5px"
                   href="https://matcherino.com/tournaments/116263" target="_blank"
            />
          </th>
        </template>
        <template v-slot:header-cell-totalPoints="props">
          <th :class="props.col.__thClass">
            {{ props.col.label }}
            <q-btn type="a" icon="help" size="1.3rem"
                   fab flat padding="5px"
                   :to="{ name: 'faq', query: { card: 'pointSystem' } }"
            />
          </th>
        </template>
        <template v-slot:header-cell-totalPenalties="props">
          <th :class="props.col.__thClass">
            {{ props.col.label }}
            <q-btn type="a" icon="help" size="1.3rem"
                   fab flat padding="5px"
                   :to="{ name: 'communitySeasonCompetitionFaq', query: { card: 'bounce-penalty' } }"
            />
          </th>
        </template>
        <template v-slot:body="props">
          <q-tr :props="props"
                :class="[
                  selectedPlayer === props.row.playerName ? 'highlight-td' : '',
                  props.row.isEligible ? '' : 'greyed-out-row'
                ]
          ">
            <q-td :props="props" key="position" class="leaderboard-position-column"
            >
              {{ props.row.position }}
            </q-td>
            <q-td :props="props" key="playerName" class="td-borders-font-size16">
              <q-item clickable
                      :to="`/player-leaderboard-community?playerName=${props.row.encodedPlayerName}`"
                      class="q-item-player-region"
              >
                <q-item-section avatar side>
                  <q-avatar rounded size="50px">
                    <img :src="props.row.profileThumb" loading="lazy" alt="Avatar"/>
<!--                    <q-img fetchpriority="low" loading="lazy" :src="props.row.profileThumb" />-->
                  </q-avatar>
                </q-item-section>
                <q-item-section>
                  <q-item-label class="player-item-label">{{ props.row.playerName }}</q-item-label>
                  <q-item-label caption>
                    <span :class="`fi fi-${props.row.flagUrl}`"></span>
                    <!--                    <q-img loading="lazy" :src="props.row.flagUrl" class="player-avatar-img"/>-->
                    <q-badge
                      :class="`badge-platform q-batch-${props.row.profilePlatform}`"
                    >
                      {{ props.row.profilePlatform }}
                    </q-badge>
                  </q-item-label>
                </q-item-section>
                <q-item-section side v-for="(award, i) in props.row.awards" :key="i">
                  <img :src="award.asset" loading="lazy" alt="Award"
                       style="width: 25px; height:42px"/>
                  <q-tooltip>
                    {{ award.tooltip }}
                  </q-tooltip>
                </q-item-section>
              </q-item>
            </q-td>
            <q-td v-if="showPrizeColumn" :props="props" key="prize">
              {{ props.cols[props.colsMap['prize'].index].value  }}
            </q-td>
            <q-td :props="props" key="totalPoints">
              {{ props.cols[props.colsMap['totalPoints'].index].value  }}
            </q-td>
            <q-td :props="props" key="avgPosition">
              {{ props.cols[props.colsMap['avgPosition'].index].value }}
            </q-td>
            <q-td :props="props" key="bestPosition">
              {{ props.cols[props.colsMap['bestPosition'].index].value }}
            </q-td>
            <q-td :props="props" key="completedTracks">
              {{ props.row.completedTracks }}
            </q-td>
            <q-td :props="props" key="invalidRuns">
              {{ props.row.invalidRuns }}
            </q-td>
            <q-td :props="props" key="totalPenalties">
              {{ props.row.totalPenalties }}
            </q-td>
            <q-td :props="props" key="totalScore">
              {{ props.cols[props.colsMap['totalScore'].index].value }}
            </q-td>
            <q-td :props="props" key="maxTopSpeed">
              {{ props.cols[props.colsMap['maxTopSpeed'].index].value }}
            </q-td>
            <q-td :props="props" key="latestActivity">
              {{ props.cols[props.colsMap['latestActivity'].index].value }}
            </q-td>
          </q-tr>
        </template>
      </q-table>
  </q-page>
</template>

<script setup>
import {markRaw, ref, shallowRef} from 'vue'
import axios from 'axios';
import {
  backGroundColorByPosition, formatMilliSeconds, getDateDifference,
  formatISODateTimeToDate
} from 'src/modules/LeaderboardFunctions'
import {fetchCurrentSeason} from 'src/modules/backendApi.js'
import { playerIdToAwardMap } from "src/modules/awards.js";
import PlayerSearchSelect from "components/PlayerSearchSelect.vue";
import placeholder from 'src/assets/placeholder.png'
import {useQuasar} from "quasar";
import {Dark} from 'quasar'
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "Community Rankings",
  meta: {
    description: {
      name: 'description',
      content: `Rankings of the current community season which consists of 30 Community tracks.`
    }
  }
})

const $q = useQuasar()
let darkModeInStorage = $q.localStorage.getItem('darkMode');
console.log('darkMode', darkModeInStorage);
const columns = [
  {
    index: 0, name: 'position', label: '#', field: 'position', required: true,
    style: row => {
      return {
        backgroundColor: !row.isEligible ? 'var(--app-player-lb-missing-run-background-color)' :
          backGroundColorByPosition(row.position)
      }
    },
    classes: row => {
      if (!row.isEligible) return ''
      return row.position === 1 ? 'first-place' : row.position === 2 ? 'second-place' : row.position === 3 ? 'third-place' : ''
    }
  },
  {index: 1, name: 'playerName', label: 'Player', field: 'playerName', align: 'left', required: true,},
  {index: 2, name: 'prize', label: 'Prize', field: 'prize', align: 'center', required: true,},
  {
    index: 3, name: 'totalPoints', label: 'Points', field: 'totalPoints', align: 'right', required: true,
    format: (val, row) => Math.round(val)
  },
  {
    index: 4, name: 'avgPosition', label: 'Avg Position', field: 'avgPosition', align: 'right', required: true,
    format: (val, row) => (Math.round(val * 100) / 100),
  },
  {index: 5, name: 'bestPosition', label: 'Best Position', field: 'bestPosition', align: 'right', required: true},
  {index: 6, name: 'completedTracks', label: 'Completed Tracks', field: 'completedTracks', align: 'center', required: true},
  {index: 7, name: 'invalidRuns', label: 'Invalid Runs', field: 'invalidRuns', align: 'center', required: true},
  {index: 8, name: 'totalPenalties', label: 'Pentalties', field: 'totalPenalties', align: 'center', required: true},
  {
    index: 9, name: 'totalScore', label: 'Total Time', field: 'totalScore', align: 'right', required: true,
    format: (val, row) => formatMilliSeconds(val),
  },
  {
    index: 10, name: 'maxTopSpeed', label: 'Top Speed', field: 'maxTopSpeed', required: true,
    format: (val, row) => (Math.round(val * 10) / 10),
  },
  {index: null, name: 'profileThumb', label: 'Profile Thumb', field: 'profileThumb'},
  {
    index: 11, name: 'latestActivity', label: 'Latest Activity', field: 'latestActivity', required: true,
    format: (val, row) => getDateDifference(val)
  }
];

const rows = shallowRef([]);
const loading = ref(true);
const selectedPlayer = ref(null);
const overallTable = shallowRef(null);
const season = shallowRef({});
const seasonDetails = shallowRef({});
const showExcludedPlayers = shallowRef(false)
const showPrizeColumn = ref(false);

const seasonPromise = fetchCurrentSeason();
seasonPromise.then((season_) => {
  seasonDetails.value = season_.details_v1
  if(seasonDetails.value.hasPrizePool && !seasonDetails.value.hasQualification){
    showPrizeColumn.value = true
  }
  season.value = season_
})

const fetchData = async function () {
  try {
    const response = await axios.get(
      `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
      + `/seasons/ranking-current-season?page=1&limit=500`
    );
    rows.value = markRaw(response.data.map((row, i) => {
      row['encodedPlayerName'] = encodeURIComponent(row['playerName']);
      if (row['profileThumb'].includes('placeholder.png')) {
        row['profileThumb'] = placeholder;
      } else {
        row['profileThumb'] = buildImgCacheUrl(row['profileThumb']);
      }
      row['awards'] = playerIdToAwardMap[row['playerId']]
      return row;
    }));
    seasonPromise.then((season) => {
      const seasonDetails = season.details_v1
      rows.value = markRaw(response.data.map((row, i) => {
        row['prize'] = seasonDetails?.hasPrizePool ? seasonDetails.prizePool[i] : null
        return row
      }))
    })
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
}

const onPlayerSelected = function (playerName) {
  selectedPlayer.value = playerName;
  const filtered = filterMethod(rows.value, {showExcludedPlayers: showExcludedPlayers.value})
  const index = filtered.findIndex((row) => row['playerName'] === playerName)
  if (index >= 0) overallTable.value.scrollTo(index, 'center-force');
}

const fixedImgUrlPart = `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_THUMBOR_PORT}`
  + `${process.env.DLAPP_THUMBOR_PATH}/50x50/`
const buildImgCacheUrl = function (url) {
  if (url) {
    if (url.includes('placeholder.png')) return url;
    let encodedUrl = encodeURIComponent(url);
    return `${fixedImgUrlPart}${encodedUrl}`;
  }
}

const filterMethod = function (rows, terms) {
  console.log("filterMethod");
  if (!terms.showExcludedPlayers)
    return rows.filter(row => row.isEligible)
  return rows;
}

fetchData();

</script>

<style lang="sass" scoped>
td
  border-left: 1px solid rgba(0, 0, 0, 0.4)
  border-right: 0
  border-top: 1px solid rgba(0, 0, 0, 0.4)
  border-bottom: 0
  font-weight: normal
  font-size: 16px

.greyed-out-row
  background: var(--app-player-lb-missing-run-background-color)

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
  min-width: 200px

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
  background-color: rgb(25, 91, 127)

.q-batch-Epic
  background-color: black

.q-batch-Playstation
  background-color: rgb(0, 65, 151)

.q-batch-Xbox
  background-color: rgb(16, 120, 15)

.highlight-td td::after
  content: ''
  background: rgba(34, 1, 133, 0.2)
</style>
