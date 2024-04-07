<template>
  <q-select
    :ref="(ref) => trackSelect = ref"
    filled
    v-model="searchText"
    use-input
    :autofocus="false"
    input-debounce="150"
    :options="searchResults"
    @filter="searchTracks"
    @update:model-value="onTrackSelected"
    style="max-width: 600px;"
    class="q-ml-md"
    use-chips
    label="Enter track name"
    :loading="loadingState"
    multiple
    popup-content-style="height: 40dvh"
    popup-content-class="q-menu-dropdown"
  >
    <template v-slot:selected>
      <q-chip
        v-for="(selection, idx) in hardSelectChips"
        clickable
        :key="idx" @click="(e) => {
          e.stopPropagation();
          e.preventDefault();
        }">
        {{ `${selection.name} (${selection.filteredOut.length})` }}
        <q-menu :ref="`menu${idx}`" max-height="30dvh" class="q-menu-dropdown bg-red">
          <q-list>
            <q-item v-for="out in selection.filteredOut" :key="out.id">
              {{ out.name }}
            </q-item>
          </q-list>
        </q-menu>
      </q-chip>
      <q-chip
        v-for="(selection, idx) in searchText"
        :key="selection.id"
        :ripple="false"
        removable
        @remove="onRemoveChip(idx)">{{ selection.name }}</q-chip>
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
        @click="onTrackSelected"
      >
        <q-item-section class="text-grey">
          No results
        </q-item-section>
      </q-item>
    </template>
  </q-select>
</template>

<script>
import axios from 'axios';

export default {
  name: 'WorstTracksSearchSelect',
  props: {
    initialSelection: {
      type: Array,
      required: false,
    },
    hardSelectFilters: {
      type: Array,
      required: false,
    },
  },
  emits: ['onTrackSelected', 'onSelectionChanged', 'onSelectionChangedWithoutHardSelects'],
  beforeCreate() {
  },
  created() {
  },
  beforeMount() {
  },
  mounted() {
  },
  data() {
    return {
      trackSelect: null,
      showTooltip: false,
      tracks: [],
      searchText: this.initialSelection != null ? this.initialSelection : [],
      searchResults: [],
      loadingState: false,
      currentSearchPartsForHighlight: [],
      hardSelectChips: [],
    }
  },
  watch: {
    hardSelectFilters: {
      async handler(newVal, prevVal){
        await this.fetchTracks();
        this.searchResults = this.getFilteredTracks(this.tracks);
        if(prevVal != null) {
          this.$emit('onSelectionChanged', [...(this.hardSelectChips.flatMap((o) => o.filteredOut)), ...this.searchText]);
        }
      },
      immediate: true
    },
    searchText: {
      handler() {
        this.$emit('onSelectionChanged', [...(this.hardSelectChips.flatMap((o) => o.filteredOut)), ...this.searchText]);
        this.$emit('onSelectionChangedWithoutHardSelects', this.searchText);
      },
      deep: true,
    },
    trackSelect(val1, val2) {
    }
  },
  methods: {
    onTrackSelected(track){
      this.$emit('onTrackSelected', track);
    },
    onRemoveChip(chipIndex){
      this.searchText.splice(chipIndex, 1);
    },
    async fetchTracks(){
      if(this.tracks.length > 0) return;
      this.loadingState = true;
      try {
        const response = await axios.get(
          `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
          + `/tracks/official`
        );
        this.tracks = response.data;
        this.searchResults = this.tracks;
      } catch (error) {
        console.error(error);
      } finally {
        this.loadingState = false;
      }
    },
    getFilteredTracks(tracks){
      if(!this.hardSelectFilters){
        return tracks;
      }
      let newHardSelectChips = [];
      this.hardSelectFilters.forEach(filter => {
        let out = filter(tracks)
        tracks = out.filtered;
        newHardSelectChips.push({
          name: out.name,
          filteredOut: out.filteredOut,
        });
      });
      this.hardSelectChips = newHardSelectChips;
      return tracks;
    },
    searchTracks(val, update, abort) {
      if(!val){
        this.searchResults = this.tracks;
      }
      this.loadingState = true;
      this.searchResults = [];
      val = val.toLowerCase().split(' ');
      this.currentSearchPartsForHighlight = val;
      this.getFilteredTracks(this.tracks).filter((track) => {
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
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
