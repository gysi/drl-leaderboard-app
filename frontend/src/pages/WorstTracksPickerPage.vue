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
        <PlayerSearchSelect :initialSelection="playerSelect.initialPlayerSelection" @onPlayerSelected="onPlayerSelected" />
        <q-btn
          label="Categories"
        >
          <q-badge class="q-badge-excluded-tracks" v-if="toggles.categoryToggles.filter(toggle => toggle.isIncluded).length > 0"
                   :label="toggles.categoryToggles.filter(toggle => toggle.isIncluded).length"
                   rounded floating
          />
          <q-menu
            anchor="bottom middle"
            self="top middle"
            class="q-menu-dropdown"
          >
            <div class="column items-start" style="max-width: 400px">
              <q-list separator bordered>
                <q-item tag="label" v-ripple v-for="toggle in toggles.categoryToggles" :key="toggle.name">
                  <q-item-section avatar>
                    <q-toggle
                      v-model="toggle.isIncluded"
                      :color="toggle.color"
                    />
                  </q-item-section>
                  <q-item-section>
                    <q-item-label>{{ toggle.label }}</q-item-label>
                    <q-item-label caption>{{ toggle.description }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </q-menu>
        </q-btn>
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
          class="q-menu-dropdown"
        >
          <div :ref="(ref) => menuRef = ref" v-show="showMenu" class="row no-wrap q-pa-md">
            <div class="column">
              <WorstTracksSearchSelect
                :initialSelection="excludedTracksSelect.initialTrackSelection"
                :hard-select-filters="toggles.hardSelectFilters"
                @onTrackSelected="onTrackSelected"
                @onSelectionChanged="onWorstTracksSelectionChanged"
                @onSelectionChangedWithoutHardSelects="onWorstTracksSelectionChangedWithoutHardSelects"
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
        <q-menu class="q-menu-dropdown">
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
                        :to="`/track-leaderboard?trackId=${props.row.trackId}`"
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
      <SlotMachine :cards="this.worstTracksTable.rows"/>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import axios from 'axios';
import SlotMachine from "components/SlotMachine.vue";
import PlayerSearchSelect from "components/PlayerSearchSelect.vue";
import WorstTracksSearchSelect from "components/WorstTracksSearchSelect.vue";
import {createMetaMixin} from "src/modules/meta.js"

export default defineComponent({
  name: 'WorstTracksPickerPage',
  components: { SlotMachine, PlayerSearchSelect, WorstTracksSearchSelect },
  mixins: [
    createMetaMixin({
      title: "Random Track Gambling",
      meta: {
        description: {
          name: 'description',
          content: `Discover new tracks with a gamble. Enter your name, choose a category, apply filters if you wish, and spin to fly a randomly selected track.`
        }
      }
    })
  ],
  watch: {
    menuRef: function (val) {
      window.addEventListener('click', this.onClickToggleIfOutsideOfMenu);
    },
    'toggles.categoryToggles': {
      handler(val) {
        for (let categoryToggle of val) {
          this.$q.localStorage.set(`worstTracksPickerPage.${categoryToggle.name}`, categoryToggle.isIncluded);
        }
        this.fetchWorstTracks();
      },
      deep: true
    }
  },
  unmounted() {
    window.removeEventListener('click', this.onClickToggleIfOutsideOfMenu);
  },
  created() {
    if(this.toggles.isMultiGpExcluded){
      this.onToggleMultiGp(this.toggles.isMultiGpExcluded)
    }
    if(this.toggles.isMicroExcluded){
      this.onToggleMicro(this.toggles.isMultiGpExcluded)
    }
    if(this.toggles.isVeryLongExcluded){
      this.onToggleLongTracks(this.toggles.isMultiGpExcluded)
    }
    this.fetchWorstTracks();
  },
  data() {
    let categoryToggles = [
      {
        name: 'includeImprovementIsLongAgo',
        label: 'Improvent is long ago',
        description: 'Includes 10 tracks where no new best time has been submitted for the longest time.',
        color: 'green',
        isIncluded: true,
      },
      {
        name: 'includeWorstPosition',
        label: 'Worst positions',
        description: 'Includes 10 tracks where the player has the worst position.',
        color: 'red',
        isIncluded: true,
      },
      {
        name: 'includeMostBeatenByEntries',
        label: 'Most beaten by entries',
        description: 'Includes 10 tracks where the player has the most beaten by entries.',
        color: 'blue',
        isIncluded: true,
      },
      {
        name: 'includeFarthestBehindLeader',
        label: 'Farthest behind leader',
        description: 'Includes 10 tracks where the player is the farthest behind the leader (1st position of a track).',
        color: 'orange',
        isIncluded: true,
      },
      {
        name: 'includePotentiallyEasyToAdvance',
        label: 'Potentially easy to advance',
        description: 'Includes the first 10 tracks sorted by the formula (player_score - leader_score) / player_position in descending order.',
        color: 'purple',
        isIncluded: true,
      },
      {
        name: 'includeInvalidRuns',
        label: 'Invalid runs',
        description: 'Includes 10 tracks where the player has invalid runs. (See FAQ for more information on what an invalid run is)',
        color: 'yellow',
        isIncluded: true,
      },
      {
        name: 'includeNotCompleted',
        label: 'Not completed',
        description: 'Includes 10 tracks where the player has not completed the track.',
        color: 'grey',
        isIncluded: true,
      },
    ];
    for (let categoryToggle of categoryToggles) {
      let fromStorage = this.$q.localStorage.getItem(`worstTracksPickerPage.${categoryToggle.name}`);
      categoryToggle.isIncluded = fromStorage != null ? fromStorage : categoryToggle.isIncluded
    }
    const isMultiGpExcludedFromStorage = this.$q.localStorage.getItem(`worstTracksPickerPage.isMultiGpExcluded`);
    const isMicroExcludedFromStorage = this.$q.localStorage.getItem(`worstTracksPickerPage.isMicroExcluded`);
    const isVeryLongExcludedFromStorage = this.$q.localStorage.getItem(`worstTracksPickerPage.isVeryLongExcluded`);
    return {
      console: console,
      menuModelValue: false,
      showMenu: false,
      menuRef: null,
      toggles: {
        isMultiGpExcluded: isMultiGpExcludedFromStorage != null ? isMultiGpExcludedFromStorage : false,
        isMicroExcluded: isMicroExcludedFromStorage != null ? isMicroExcludedFromStorage : false,
        isVeryLongExcluded: isVeryLongExcludedFromStorage != null ? isVeryLongExcludedFromStorage : false,
        hardSelectFilters: [],
        categoryToggles: categoryToggles
      },
      playerSelect: {
        playerName: this.$q.localStorage.getItem(`worstTracksPickerPage.initialPlayerSelection`),
        initialPlayerSelection: this.$q.localStorage.getItem(`worstTracksPickerPage.initialPlayerSelection`),
      },
      excludedTracksSelect: {
        tracks: this.$q.localStorage.getItem(`worstTracksPickerPage.selectedTracks`) || [],
        initialTrackSelection: this.$q.localStorage.getItem(`worstTracksPickerPage.selectedTracksWithoutChips`)
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
      this.$q.localStorage.set(`worstTracksPickerPage.isMultiGpExcluded`, state);
      this.genericToggle(state, this.multiGpFilter);
    },
    multiGpFilter(tracks){
      return this.genericFilter("MultiGP tracks",
        track => track.mapName === "MULTIGP", tracks);
    },
    onToggleMicro(state){
      this.$q.localStorage.set(`worstTracksPickerPage.isMicroExcluded`, state);
      this.genericToggle(state, this.microFilter);
    },
    microFilter(tracks){
      return this.genericFilter("Micro tracks", track => {
        return track.name === "MGP 2018 IO MICRO" ||
          track.name === "MGP 2019 IO MICRO";
      }, tracks)
    },
    onToggleLongTracks(state){
      this.$q.localStorage.set(`worstTracksPickerPage.isVeryLongExcluded`, state);
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
      this.$q.localStorage.set(`worstTracksPickerPage.initialPlayerSelection`, player);
      this.fetchWorstTracks();
    },
    onTrackSelected(track){
    },
    onWorstTracksSelectionChanged(selectedTracks){
      this.excludedTracksSelect.tracks = selectedTracks;
      this.$q.localStorage.set(`worstTracksPickerPage.selectedTracks`, selectedTracks);
      console.log('onWorstTRacksSelectionChanged', selectedTracks);
      this.fetchWorstTracks();
    },
    onWorstTracksSelectionChangedWithoutHardSelects(selectedTracks){
      this.$q.localStorage.set(`worstTracksPickerPage.selectedTracksWithoutChips`, selectedTracks);
    },
    async fetchWorstTracks(){
      if(!this.playerSelect.playerName){
        this.worstTracksTable.rows = [];
        return;
      }
      this.worstTracksTable.loading = true;
      try {
        const response = await axios.post(
          `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
          + '/leaderboards/worst-tracks',
          this.toggles.categoryToggles.reduce((obj, toggle) => {
            obj[toggle.name] = toggle.isIncluded;
            return obj;
          }, {
            playerName: this.playerSelect.playerName,
            excludedTracks: this.excludedTracksSelect.tracks.map(track => track.id)
          })
        );
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
