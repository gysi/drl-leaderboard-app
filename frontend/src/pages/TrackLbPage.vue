<template>
  <q-page padding style="height: 100%" class="col items-start">
    <q-table
      :columns="columns"
      :rows="rows"
      :loading="loading"
      row-key="id"
      class="my-sticky-header-table"
      table-class="col-auto"
      style="max-height: 100%;"
      :pagination="pagination"
      hide-pagination
    >
      <template v-slot:top-left>
        <div class="row">
<!--          <div class="text-h4">{{ searchText?.name?.toUpperCase() || 'Track' }} Rankings</div>-->
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
                  <q-item-label>{{ props.opt.name }}</q-item-label>
                  <q-item-label caption>{{ props.opt.mapName }}</q-item-label>
                  <q-item-label caption>{{ props.opt.parentCategory }}</q-item-label>
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
      <template v-slot:body="props">
        <q-tr>
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
                :style="{
                  backgroundColor: props.row.isInvalidRun ?
                    'rgba(187,44,44,0.54)': col.name === 'position' ? backGroundColorByPosition(props.row.position) : null
                  }"
                :class="col.name === 'position' && !props.row.isInvalidRun ?
                          props.row.position === 1 ? 'first-place' :
                          props.row.position === 2 ? 'second-place' :
                          props.row.position === 3 ? 'third-place' : '' : ''"
          >
            <q-item v-if="col.name === 'playerName'"
                    clickable
                    :to="`/playerlb/?playerName=${props.row.playerName}`"
                    class="q-item-player-region"
            >
              <q-item-section avatar side>
                <q-avatar rounded size="50px">
<!--                  <img :src="props.row.profileThumb" loading="eager" alt="Avatar"/>-->
                  <q-img :src="props.row.profileThumb" />
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
