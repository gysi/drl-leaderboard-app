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
        <img v-if="searchText?.mapName != null"
             loading="lazy"
             class="animated-background-image"
             :src="`/maps/map-${searchText.mapName.toLowerCase().replaceAll(' ', '-')}-fs8.png`" />
        <div class="row" style="overflow: hidden; position: relative; padding: 12px 16px;">

          <div class="q-table__title">Track Rankings</div>
          <q-select
            ref="trackselect"
            filled
            v-model="searchText"
            use-input
            autofocus
            input-debounce="150"
            :options="searchResults"
            @filter="search"
            @new-value="onEnterPressed"
            @update:model-value="fetchData"
            style="width: 350px"
            class="q-ml-md"
            use-chips
            label="Enter track name"
          >
            <template v-slot:selected-item="props">
              <q-chip
                v-if="props.selected"
                :key="props.selected"
                :value="props.selected"
                removable
                @remove="() => { searchText = null }">{{ props.opt.name }}</q-chip>
            </template>
            <template v-slot:option="props">
              <q-item
                v-bind="props.itemProps"
                clickable
                :active="props.selected"
                :style="props.selected ? 'background: #e0e0e0' : ''"
                @update:model-value="props.toggleOption(props.opt)"
              >
                <q-item-section>
                  <q-item-label><span v-html="this.highlightBySubstrings(this.currentSearchPartsForHighlight, props.opt.name)"></span></q-item-label>
                  <q-item-label caption><span v-html="this.highlightBySubstrings(this.currentSearchPartsForHighlight, props.opt.mapName)"></span></q-item-label>
                  <q-item-label caption ><span v-html="this.highlightBySubstrings(this.currentSearchPartsForHighlight, props.opt.parentCategory)"></span></q-item-label>
                </q-item-section>
              </q-item>
            </template>
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
                    :to="`/player-lb/?playerName=${props.row.playerName}`"
                    class="q-item-player-region"
            >
              <q-item-section avatar side>
                <q-avatar rounded size="50px">
                  <img loading="lazy" :src="props.row.profileThumb" />
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="player-item-label">{{ props.row.playerName }}</q-item-label>
                <q-item-label caption>
                  <span :class="`fi fi-${this.formatFlagUrl(props.row.flagUrl)}`"></span>
                  <q-badge
                    :class="`badge-platform q-batch-${props.row.profilePlatform}`"
                  >{{ props.row.profilePlatform }}</q-badge>
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

export default defineComponent({
  name: 'TrackLbPage',
  async created(){
    let trackId = this.$router.currentRoute.value.query.trackId;
    await this.fetchTracks();
    if(!!trackId) {
      let track = this.tracks.filter(track => track.id === trackId)[0];
      if(!!track) {
        this.searchText = track;
      }
    }
    await this.fetchData(this.searchText);
  },
  data(){
    return {
      tracks: [],
      searchText: null,
      currentSearchPartsForHighlight: [],
      searchResults: [],
      loadingState: true,
      columns: [
        { name: 'position', label: '#', field: 'position' },
        { name: 'playerName', label: 'Player', field: 'playerName', align: 'left' },
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
        rowsPerPage: 100,
      },
    }
  },
  methods: {
    onEnterPressed(val) {
      this.$refs.trackselect.toggleOption(val);
    },
    async fetchTracks(){
      try {
        const response = await axios.get(process.env.DLAPP_API_URL+'/tracks');
        this.tracks = response.data;
      } catch (error) {
        console.error(error);
      } finally {
      }
    },
    search(val, update, abort) {
      if(!val){
        this.searchResults = this.tracks;
      }
      this.loadingState = true;
      this.searchResults = [];
      val = val.toLowerCase().split(' ');
      this.currentSearchPartsForHighlight = val;
      this.tracks.filter((track) => {
        let foundCount = 0;
        for (let i = 0; i < val.length; i++) {
          let foundInPart = 0;
          if(!!track.name && track.name.toLowerCase().indexOf(val[i]) > -1){
            foundInPart = 1;
          }
          if(!!track.mapName && track.mapName.toLowerCase().indexOf(val[i]) > -1){
            foundInPart = 1;
          }
          if(!!track.parentCategory && track.parentCategory.toLowerCase().indexOf(val[i]) > -1){
            foundInPart = 1;
          }
          foundCount += foundInPart;
        }
        return foundCount === val.length;
      }).forEach((track) => {
        this.searchResults.push(track);
      });
      this.loadingState = false;
      update();
    },
    async fetchData(track) {
      console.log("start", new Date().getTime());
      if(!track || !track.id) {
        this.rows = [];
        return;
      }
      this.$router.replace({
        query: {
          trackId: track.id
        }
      });
      this.loading = true;
      try {
        const response = await axios.get(`${process.env.DLAPP_API_URL}/leaderboards/bytrack/${track.id}?page=1&limit=100`);
        this.rows = response.data;
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
      console.log("end", new Date().getTime());
    },
    formatFlagUrl(flagUrl){
      return flagUrl.substring(flagUrl.length-6, flagUrl.length-4);
    },
    highlightBySubstrings(searchParts, stringToHighlight){
      if(!searchParts || searchParts.length === 0 || !stringToHighlight){
        return stringToHighlight;
      }
      let searchPartsRegex = searchParts.filter(part => !!part).join(')|(');
      if(!searchPartsRegex){
        return stringToHighlight;
      }
      let reg = new RegExp('('+searchPartsRegex+')', 'gi');
      stringToHighlight = stringToHighlight.replace(reg, function(str) {
        return '<b class="search-highlight">'+str+'</b>'
      });
      return stringToHighlight;
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

:deep(.search-highlight)
  background-color: rgba(255,255,255,0.1)
  font-weight: 900
  color: rgba(255,255,255,1)
</style>
