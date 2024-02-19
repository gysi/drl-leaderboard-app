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
        <img v-if="selectedTrack?.mapName != null"
             loading="lazy"
             class="animated-background-image"
             style="mask-image: linear-gradient(to right, rgba(0,0,0,0), rgba(0,0,0,1) 160px, rgba(0,0,0,1));"
             :src="`/maps/map-${selectedTrack.mapName.toLowerCase().replaceAll(/[. ]/g, '-')}-fs8.png`" />
        <div class="row" style="overflow: hidden; position: relative; padding: 12px 16px;">
          <div class="q-table__title">Track Rankings</div>
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
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
                :style="{
                  backgroundColor: props.row.isInvalidRun ?
                    'rgba(187,44,44,0.54)': col.name === 'position' ? backGroundColorByPosition(props.row.position) : null,
                  paddingLeft: props.row.isInvalidRun && col.name === 'position' ? '5px' : null,
                  }"
                :class="['td-borders-font-size16', col.name === 'position' && !props.row.isInvalidRun ?
                          props.row.position === 1 ? 'first-place' :
                          props.row.position === 2 ? 'second-place' :
                          props.row.position === 3 ? 'third-place' : '' : '', col.name === 'position' ? 'leaderboard-position-column' : '']"
          >
            <q-item v-if="col.name === 'playerName'"
                    clickable
                    :to="`/player-lb?playerName=${props.row.playerName}`"
                    class="q-item-player-region"
            >
              <q-item-section avatar side>
                <q-avatar rounded size="50px">
                  <img loading="lazy" :src="props.row.player.profileThumb" />
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="player-item-label">{{ props.row.player.playerName }}</q-item-label>
                <q-item-label caption>
                  <span :class="`fi fi-${this.formatFlagUrl(props.row.player.flagUrl)}`"></span>
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
            >
              <q-tooltip>
                <div v-html="props.row.invalidRunReason.replaceAll(',', '</br>')"></div>
              </q-tooltip>
            </q-btn>
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
import {backGroundColorByPosition, formatMilliSeconds, getDateDifference} from "src/modules/LeaderboardFunctions";
import TracksSearchSelect from "components/TracksSearchSelect.vue";

export default defineComponent({
  name: 'TrackLbPage',
  components: {TracksSearchSelect},
  data(){
    return {
      selectedTrack: null,
      columns: [
        { name: 'position', label: '#', field: 'position' },
        { name: 'playerName', label: 'Player', field: row => row.player.playerName, align: 'left' },
        { name: 'score', label: 'Time', field: 'score', format: (val, row) => this.formatMilliSeconds(val), align: 'left' },
        { name: 'crashes', label: 'Crashes', field: 'crashCount' },
        { name: 'topSpeed', label: 'Top Speed', field: 'topSpeed', format: (val, row) => (Math.round(val*10)/10) },
        { name: 'points', label: 'Points', field: 'points', format: (val, row) => row.isInvalidRun ? 0 : Math.round(val) },
        { name: 'createdAt', label: 'Time Set', field: 'createdAt', format: (val, row) => this.getDateDifference(val) },
        { name: 'droneName', label: 'Drone Name', field: 'droneName' },
      ],
      rows: [],
      loading: false,
      pagination: {
        rowsPerPage: 0,
      },
    }
  },
  methods: {
    onTrackSelection(track) {
      this.selectedTrack = track;
      this.fetchData(track);
    },
    async fetchData(track) {
      if(!track || !track.id) {
        this.rows = [];
        return;
      }
      this.loading = true;
      try {
        const response = await axios.get(`${process.env.DLAPP_API_URL}/leaderboards/bytrack/${track.id}?page=1&limit=150`);
        this.rows = response.data;
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
    },
    formatFlagUrl(flagUrl){
      return flagUrl.substring(flagUrl.length-6, flagUrl.length-4);
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
