<template>
  <q-page padding style="height: 100%" class="q-pa-md col items-start">
    <q-table
      :columns="columns"
      :rows="rows"
      :loading="loading"
      row-key="id"
      class="my-sticky-header-table table-with-brackground-image"
      table-class="col-auto"
      style="max-height: 100%;"
      :pagination="pagination"
      hide-pagination
      flat
      bordered
    >
      <template v-slot:top-left>
        <img v-if="selectedTrack?.mapThumb != null"
             loading="lazy"
             class="animated-background-image"
             style="mask-image: linear-gradient(to right, rgba(0,0,0,0), rgba(0,0,0,1) 160px, rgba(0,0,0,1));"
             :src="buildImgCacheUrl(selectedTrack.mapThumb)" />
        <img v-else-if="selectedTrack?.mapName != null"
             loading="lazy"
             class="animated-background-image"
             style="mask-image: linear-gradient(to right, rgba(0,0,0,0), rgba(0,0,0,1) 160px, rgba(0,0,0,1));"
             :src="`/maps/map-${selectedTrack.mapName.toLowerCase().replaceAll(/[. ]/g, '-')}-fs8.png`" />
        <div class="row q-pa-md" style="overflow: hidden; position: relative;">
          <div class="q-table__title q-mr-md q-mb-sm">Track Rankings</div>
          <TracksSearchSelect @track-selected="onTrackSelection" />
        </div>
      </template>
      <template v-slot:header-cell-points="props">
        <th :class="props.col.__thClass">
          {{ props.col.label }}
          <q-btn type="a" icon="help" size="1.3rem"
                 fab flat padding="5px"
                 :to="{ name: 'faq', query: { card: 'pointSystem' } }"
          />
        </th>
      </template>
      <template v-slot:header-cell-penalties="props">
        <th :class="props.col.__thClass">
          {{ props.col.label }}
          <q-btn type="a" icon="help" size="1.3rem"
                 fab flat padding="5px"
                 :to="{ name: 'communitySeasonCompetitionFaq', query: { card: 'bounce-penalty' } }"
          />
        </th>
      </template>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
                :style="{
                  backgroundColor: props.row.isInvalidRun ?
                    'rgba(187,44,44,0.54)': props.row.timePenaltyTotal > 0 ?
                    'rgba(187,175,44,0.54)': col.name === 'position' ? backGroundColorByPosition(props.row.position) : null,
                  paddingLeft: (props.row.isInvalidRun || props.row.timePenaltyTotal > 0) && col.name === 'position' ? '5px' : null,
                  }"
                :class="['td-borders-font-size16', col.name === 'position' && !props.row.isInvalidRun ?
                          props.row.position === 1 ? 'first-place' :
                          props.row.position === 2 ? 'second-place' :
                          props.row.position === 3 ? 'third-place' : '' : '', col.name === 'position' ? 'leaderboard-position-column' : '']"
          >
            <q-item v-if="col.name === 'playerName'" clickable
                    :to="`/${targetProfileLink}?playerName=${encodeURIComponent(props.row.player.playerName)}`"
                    class="q-item-player-region">
              <q-item-section avatar side>
                <q-avatar rounded size="50px">
                  <img loading="lazy" :src="props.row.player.profileThumb" />
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="player-item-label">{{ props.row.player.playerName }}</q-item-label>
                <q-item-label caption>
                  <span :class="`fi fi-${formatFlagUrl(props.row.player.flagUrl)}`"></span>
                  <q-badge
                    :class="`badge-platform q-batch-${props.row.player.profilePlatform}`"
                  >{{ props.row.player.profilePlatform }}</q-badge>
                </q-item-label>
              </q-item-section>
            </q-item>
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
              :to="{ name: 'communitySeasonCompetitionFaq', query: { card: 'bounce-penalty' } }"
              ripple
              :label="props.row.position"
              style="width: 52px; position: relative"
            >
              <q-tooltip>
                <div>BOUNCE</div>
              </q-tooltip>
            </q-btn>
            {{ col.name === 'playerName' || (col.name === 'position' && (props.row.isInvalidRun || props.row.timePenaltyTotal > 0)) ? '' : col.value }}
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </q-page>
</template>

<script setup>
import {ref, shallowRef} from 'vue'
import axios from 'axios'
import { backGroundColorByPosition, formatMilliSeconds, getDateDifference } from "src/modules/LeaderboardFunctions"
import TracksSearchSelect from "components/TracksSearchSelect.vue"
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "Track Leaderboards",
  meta: {
    description: {
      name: 'description',
      content: `Discover top times for official and community season tracks. Easily compare performances across the board.`
    }
  }
})

const selectedTrack = ref(null)
const targetProfileLink = ref(null)
const rows = shallowRef([])
const loading = ref(false)
const pagination = ref({
  rowsPerPage: 0,
})

const columns = [
  { name: 'position', label: '#', field: 'position' },
  { name: 'playerName', label: 'Player', field: row => row.player.playerName, align: 'left' },
  { name: 'score', label: 'Time', field: 'score', format: (val, row) => formatMilliSeconds(val), align: 'left' },
  { name: 'penalties', label: 'Penalties', field: 'penalties' },
  { name: 'topSpeed', label: 'Top Speed', field: 'topSpeed', format: (val, row) => (Math.round(val*10)/10) },
  { name: 'points', label: 'Points', field: 'points', format: (val, row) => row.isInvalidRun ? 0 : Math.round(val) },
  { name: 'createdAt', label: 'Time Set', field: 'createdAt', format: (val, row) => getDateDifference(val) },
  { name: 'droneName', label: 'Drone Name', field: 'droneName' },
]

const onTrackSelection = (track) => {
  selectedTrack.value = track
  if (track?.parentCategory === 'Community') {
    targetProfileLink.value = 'player-leaderboard-community'
  } else {
    targetProfileLink.value = 'player-leaderboard'
  }
  fetchData(track)
}

const fetchData = async (track) => {
  if (!track || !track.id) {
    rows.value = []
    return
  }
  loading.value = true
  try {
    const response = await axios.get(
      `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
      + `/leaderboards/bytrack/${track.id}?page=1&limit=250`
    )
    // rows.value = response.data;
    rows.value = response.data.map((row) => {
      row['score'] = row['score'] + (row['timePenaltyTotal'] ? row['timePenaltyTotal'] : 0)
      row['penalties'] = row['penalties'] == null ? 0 : row['penalties'].length
      return row
    })
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const formatFlagUrl = (flagUrl) => {
  return flagUrl.substring(flagUrl.length - 6, flagUrl.length - 4)
}

const buildImgCacheUrl = (url) => {
  if (url) {
    let encodedUrl = encodeURIComponent(url)
    return `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_THUMBOR_PORT}${process.env.DLAPP_THUMBOR_PATH}`
      +`/${encodedUrl}`
  }
}
</script>

<style lang="sass" scoped>
:deep(tbody .q-td)
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
