<template>
<!--  https://codesandbox.io/s/one-arm-bandit-cbocw?file=/script.js-->
  <q-page padding style="height: 100%" class="slotmachine-page q-pa-md row items-start"
  >
    <div
      class="main-box column items-center"
      :style="{
        borderRadius: '2.5vmin',
        paddingTop: '10px'
        // paddingRight: '10%'
      }"
    >
      <div class="row q-gutter-sm">
        <PlayerSearchSelect @onPlayerSelected="onPlayerSelected" />
        <q-btn
        label="Excluded Tracks"
        @click="(e) => {
          this.menuModelValue = true;
            this.showMenu = !this.showMenu;
            e.stopPropagation();
            e.stopImmediatePropagation();
            e.preventDefault();
            }"
      >
        <q-badge class="q-badge-excluded-tracks" v-if="this.excludedTracksSelect.tracks.length > 0"
          :label="this.excludedTracksSelect.tracks.length"
          rounded floating
        />
        <q-menu
          anchor="bottom middle"
          self="top middle"
          :model-value="this.menuModelValue"
          persistent
        >
          <div :ref="(ref) => menuRef = ref" v-show="showMenu" class="row no-wrap q-pa-md">
            <div class="column">
              <WorstTracksSearchSelect
                :hard-select-filters="toggles.hardSelectFilters"
                @onTrackSelected="onTrackSelected"
                @onSelectionChanged="onWorstTracksSelectionChanged"
              />
            </div>
<!--            <q-separator vertical inset class="q-mx-lg" />-->
            <div class="column items-center">
              <q-toggle
                v-model="toggles.isMultiGpExcluded"
                color="green"
                label="MultiGP"
                @update:model-value="onToggleMultiGp"
              />
              <q-toggle
                label="Micro"
                v-model="toggles.isMicroExcluded"
                color="red"
                @update:model-value="onToggleMicro"
              />
              <q-toggle
                label="Very long"
                v-model="toggles.isVeryLongExcluded"
                color="blue"
                @update:model-value="onToggleLongTracks"
              />
            </div>
          </div>
        </q-menu>
      </q-btn>
        <q-btn
        label="Show Trackpool"
      >
        <q-badge class="q-badge-track-pool" v-if="this.worstTracksTable.rows.length > 0"
                 :label="this.worstTracksTable.rows.length"
                 rounded floating
        />
        <q-menu>
          <q-table
            title="Trackpool"
            :columns="worstTracksTable.columns"
            :rows="worstTracksTable.rows"
            :loading="worstTracksTable.loading"
            row-key="trackId"
            flat
            bordered
          >
            <template v-slot:body-cell-track="props">
              <q-td
                :props="props"
                class="worstTracksTable-track-td"
              >
                <q-item clickable
                        :to="`/track-lb/?trackId=${props.row.trackId}`"
                >
                  <q-item-section>
                    <q-item-label>{{ props.row.trackName }}</q-item-label>
                    <q-item-label caption>
                      <q-chip dense class="track-chip-map">{{ props.row.trackMapName }}</q-chip>
                      <q-chip dense class="track-chip-parentcategory">{{ props.row.trackParentCategory }}</q-chip>
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-td>
            </template>
          </q-table>
        </q-menu>
      </q-btn>
      </div>
      <q-separator spaced size="2px" style="width: 95%"/>
      <SlotMachine :cards="this.worstTracksTable.rows.map((r) => r.trackName)"/>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import axios from 'axios';
import SlotMachine from "components/SlotMachine.vue";
import PlayerSearchSelect from "components/PlayerSearchSelect.vue";
import WorstTracksSearchSelect from "components/WorstTracksSearchSelect.vue";

export default defineComponent({
  name: 'WorstTracksPickerPage',
  components: { SlotMachine, PlayerSearchSelect, WorstTracksSearchSelect },
  watch: {
    menuRef: function (val) {
      window.addEventListener('click', this.onClickToggleIfOutsideOfMenu);
    }
  },
  unmounted() {
    window.removeEventListener('click', this.onClickToggleIfOutsideOfMenu);
  },
  created() {
  },
  data() {
    return {
      console: console,
      menuModelValue: false,
      showMenu: false,
      menuRef: null,
      toggles: {
        isMultiGpExcluded: false,
        isMicroExcluded: false,
        isVeryLongExcluded: false,
        hardSelectFilters: []
      },
      playerSelect: {
        playerName: null,
      },
      excludedTracksSelect: {
        tracks: [],
      },
      worstTracksTable: {
        columns: [
          { name: 'track', label: 'Track', field: row => row.trackName, align: 'left'},

        ],
        rows: [],
        loading: false,
      }
    }
  },
  methods: {
    onClickToggleIfOutsideOfMenu(e){
      let element = e.target
      let targetIsMenu = false;
      while (element) {
        if (element.classList?.contains("q-menu")) {
          targetIsMenu = true;
          return;
        }
        element = element.parentNode;
      }
      if (!targetIsMenu) {
        this.showMenu = false;
      }
    },
    onToggleMultiGp(state){
      this.genericToggle(state, this.multiGpFilter);
    },
    multiGpFilter(tracks){
      return this.genericFilter("MultiGP tracks",
        track => track.mapName === "MULTIGP", tracks);
    },
    onToggleMicro(state){
      this.genericToggle(state, this.microFilter);
    },
    microFilter(tracks){
      return this.genericFilter("Micro tracks", track => {
        return track.name === "MGP 2018 IO MICRO" ||
          track.name === "MGP 2019 IO MICRO";
      }, tracks)
    },
    onToggleLongTracks(state){
      this.genericToggle(state, this.longTrackFilter);
    },
    longTrackFilter(tracks){
      return this.genericFilter("Very long tracks", track => {
        return track.name === "MAD: STONEHENGE UFO" ||
          track.name === "2019 MAYHEM TEAM EVENT";
      }, tracks)
    },
    genericToggle(state, filter){
      if(state === true){
        this.toggles.hardSelectFilters = [...this.toggles.hardSelectFilters, filter];
      }else{
        this.toggles.hardSelectFilters =
          this.toggles.hardSelectFilters.filter(f => f !== filter);
      }
    },
    genericFilter(name, filterFunction, tracks){
      let filteredOut = [];
      let filteredTracks = tracks.filter(track => {
        if(filterFunction(track)){
          filteredOut.push(track);
          return false;
        }
        return true;
      });
      return {
        name: name,
        filteredOut: filteredOut,
        filtered: filteredTracks
      }
    },

    onPlayerSelected(player){
      this.playerSelect.playerName = player;
      this.fetchWorstTracks();
    },
    onTrackSelected(track){
    },
    onWorstTracksSelectionChanged(selectedTracks){
      this.excludedTracksSelect.tracks = selectedTracks;
      this.fetchWorstTracks();
    },
    async fetchWorstTracks(){
      if(!this.playerSelect.playerName){
        this.worstTracksTable.rows = [];
        return;
      }
      this.worstTracksTable.loading = true;
      try {
        const response = await axios.post(process.env.DLAPP_API_URL+'/leaderboards/worst-tracks',
          {
            playerName: this.playerSelect.playerName,
            excludedTracks: this.excludedTracksSelect.tracks.map(track => track.id)
          });
        this.worstTracksTable.rows = response.data;
      } finally {
        this.worstTracksTable.loading = false;
      }
    }
  }
});
</script>

<style lang="scss" scoped>
:deep(.search-highlight) {
  background-color: rgba(255, 255, 255, 0.1);
  font-weight: 900;
  color: rgba(255, 255, 255, 1);
}

:deep(.worstTracksTable-track-td) {
  padding: 0;
}

:deep(.worstTracksTable-track-td .q-chip) {
  margin-left: 0;
  margin-top: 0;
  margin-bottom: 0;
}
</style>
