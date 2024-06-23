<template>
  <q-page padding style="height: 100%" class="q-pa-md items-start">
    <q-table
      ref="overallTable"
      title="Overall Rankings"
      :columns="columns"
      :rows="rows"
      :loading="loading"
      row-key="position"
      class="my-sticky-header-table"
      style="max-height: 100%;"
      flat
      bordered
      virtual-scroll
      :visible-columns="[]"
      :rows-per-page-options="[0]"
      hide-bottom
      virtual-scroll-item-size="65"
      virtual-scroll-slice-ratio-before="5"
      virtual-scroll-slice-ratio-after="5"
      virtual-scroll-sticky-size-start="49"
    >
      <template v-slot:top-left>
        <div class="row">
          <div class="q-table__title q-mr-md q-mb-sm">Rankings</div>
          <div class="row q-gutter-md">
            <q-select v-model="selectedParentCategory"
                      :options="parentCategories"
                      option-label="description"
                      filled label="Category" popup-content-class="q-menu-dropdown"
                      style="width: 250px"></q-select>
            <PlayerSearchSelect @onPlayerSelected="onPlayerSelected" label="Jump to player name" />
          </div>
        </div>
      </template>
      <template v-slot:header-cell-invalidRuns="props">
        <th :class="props.col.__thclass">
          {{ props.col.label }}
          <q-btn type="a" icon="help" size="1.3rem"
                 fab flat padding="5px"
                 :to="{ name: 'faq', query: { card: 'invalidRuns' } }"
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
      <template v-slot:body="props">
        <q-tr :props="props" :class="selectedPlayer === props.row.playerName ? 'highlight-td' : ''">
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
                :style="{
              backgroundColor: col.name === 'position' ? backGroundColorByPosition(props.row.position) : null
              }"
                :class="['td-borders-font-size16', col.name === 'position' && !props.row.isInvalidRun ?
              props.row.position === 1 ? 'first-place' :
              props.row.position === 2 ? 'second-place' :
              props.row.position === 3 ? 'third-place' : '' : '', col.name === 'position' ? 'leaderboard-position-column' : '']"
          >
            <q-item v-if="col.name === 'playerName'"
                    clickable
                    :to="`/player-leaderboard?playerName=${encodeURIComponent(props.row.playerName)}`"
                    class="q-item-player-region"
            >
              <q-item-section avatar side>
                <q-avatar rounded size="50px">
                  <img :src="props.row.profileThumb" loading="lazy" alt="Avatar"/>
                  <!--                  <q-img loading="lazy" :src="props.row.profileThumb" />-->
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="player-item-label">{{ props.row.playerName }}</q-item-label>
                <q-item-label caption>
                  <span :class="`fi fi-${props.row.flagUrl}`"></span>
                  <!--                    <q-img loading="lazy" :src="props.row.flagUrl" class="player-avatar-img"/>-->
                  <q-badge
                    :class="`badge-platform q-batch-${props.row.profilePlatform}`"
                  >{{ props.row.profilePlatform }}
                  </q-badge>
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

<script setup>
import {ref, watch, shallowRef} from 'vue'
import axios from 'axios'
import { backGroundColorByPosition, formatMilliSeconds, getDateDifference } from 'src/modules/LeaderboardFunctions'
import PlayerSearchSelect from "components/PlayerSearchSelect.vue";
import placeholder from 'src/assets/placeholder.png'
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "Overall Rankings",
  meta: {
    description: {
      name: 'description',
      content: `Explore rankings across the 201 official DRL drone racing tracks.`
    }
  }
})

const columns = [
  {name: 'position', label: '#', field: 'position', required: true},
  {name: 'playerName', label: 'Player', field: 'playerName', align: 'left', required: true},
  {name: 'totalPoints', label: 'Points', field: 'totalPoints', align: 'right', required: true},
  {
    name: 'avgPosition',
    label: 'Average Position',
    field: 'avgPosition',
    align: 'right',
    format: (val, row) => (Math.round(val * 100) / 100),
    required: true
  },
  {
    name: 'bestPosition',
    label: 'Best Position',
    field: 'bestPosition',
    align: 'right',
    required: true
  },
  {name: 'invalidRuns', label: 'Invalid Runs', field: 'invalidRuns', align: 'center', required: true},
  {name: 'completedTracks', label: 'Completed Tracks', field: 'completedTracks', align: 'center', required: true},
  {name: 'totalCrashCount', label: 'Crashes', field: 'totalCrashCount', align: 'center', required: true},
  {
    name: 'totalScore',
    label: 'Total Time',
    field: 'totalScore',
    format: val => formatMilliSeconds(val),
    align: 'right',
    required: true
  },
  {
    name: 'maxTopSpeed',
    label: 'Top Speed',
    field: 'maxTopSpeed',
    format: (val, row) => (Math.round(val * 10) / 10),
    required: true
  },
  {name: 'profileThumb', label: 'Profile Thumb', field: 'profileThumb'},
  { name: 'latestActivity', label: 'Latest Activity', field: 'latestActivity', format: val => getDateDifference(val), required: true }
]
const overallTable = ref(null);
const rows = shallowRef([])
const loading = ref(false)
const selectedPlayer = ref(null)
const parentCategories = ref([{id: null, name: 'Overall', description: 'Overall'}])
const selectedParentCategory = ref({id: null, name: 'Overall', description: 'Overall'})

watch(selectedParentCategory, (newCategory) => {
  fetchData(newCategory)
})

const fetchData = async (parentCategory = selectedParentCategory.value) => {
  loading.value = true
  try {
    const response = await axios.get(
      `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
      + `/leaderboards/official/overall-ranking?page=1&limit=500${parentCategory != null && parentCategory.name !== 'Overall' ? `&parentCategory=${parentCategory.name}` : ''}`)
    rows.value = response.data.map((row) => {
      row['profileThumb'] = row['profileThumb'].includes('placeholder.png') ? placeholder : buildImgCacheUrl(row['profileThumb'])
      return row
    })
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const onPlayerSelected = (playerName) => {
  selectedPlayer.value = playerName
  const row = rows.value.find(row => row['playerName'] === playerName)
  if (row) overallTable.value.scrollTo(row['position'], 'center-force')
}

const fetchParentCategories = async () => {
  const response = await axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.
      DLAPP_API_PATH}`
    + `/tracks/parent-categories`
  )
  parentCategories.value = [{id: null, name: 'Overall', description: 'Overall'}, ...response.data]
}

const buildImgCacheUrl = (url) => {
  if (url && !url.includes('placeholder.png')) {
    let encodedUrl = encodeURIComponent(url)
    return `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_THUMBOR_PORT}${process.env.DLAPP_THUMBOR_PATH}`
      + `/50x50/${encodedUrl}`
  }
  return url
}

fetchData()
fetchParentCategories()
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
