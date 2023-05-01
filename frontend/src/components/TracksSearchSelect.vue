<template>
  <q-select
    ref="refTrackSelect"
    filled
    v-model="state.searchText"
    use-input
    autofocus
    input-debounce="250"
    :options="state.searchResults"
    @filter="search"
    @new-value="onEnterPressed"
    @update:model-value="onOptionSelected"
    style="width: 350px"
    class="q-ml-md"
    use-chips
    label="Enter track name"
    :loading="state.loading"
    popup-content-class="q-menu-dropdown"
  >
    <template v-slot:selected-item="props">
      <q-chip
        v-if="props.selected"
        :key="props.selected"
        :value="props.selected"
        removable
        @remove="() => { state.searchText = null }">{{ props.opt.name }}</q-chip>
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
          <q-item-label><span v-html="highlightBySubstrings(props.opt.name).value"></span></q-item-label>
          <q-item-label caption><span v-html="highlightBySubstrings(props.opt.mapName).value"></span></q-item-label>
          <q-item-label caption ><span v-html="highlightBySubstrings(props.opt.parentCategory).value"></span></q-item-label>
        </q-item-section>
      </q-item>
    </template>
    <template v-slot:no-option>
      <q-item
        @click="onOptionSelected"
      >
        <q-item-section class="text-grey">
          No results
        </q-item-section>
      </q-item>
    </template>
  </q-select>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from "axios";

const emit = defineEmits(['track-selected']);

const refTrackSelect = ref(null);

const state = reactive({
  searchText: null,
  searchResults: [],
  currentSearchPartsForHighlight: [],
  tracks: [],
  loading: false,
});

const router = useRouter();

const fetchTracks = async () => {
  try {
    state.loading = true;
    const response = await axios.get(process.env.DLAPP_API_URL+'/tracks');
    state.tracks = response.data;
  } catch (error) {
    console.error(error);
  } finally {
    state.loading = false;
  }
}

fetchTracks().then(() => {
  if(router.currentRoute.value.query.trackId){
    state.searchText = state.tracks.find(track => track.id === router.currentRoute.value.query.trackId);
    onOptionSelected(state.searchText);
  }
});

const search = async (val, update, abort) => {
  if(!val){
    state.searchResults = state.tracks;
  }
  state.loading = true;
  let searchResults = [];
  val = val.toLowerCase().split(' ');
  state.tracks.filter((track) => {
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
    searchResults.push(track);
  });
  state.searchResults = searchResults;
  state.currentSearchPartsForHighlight = val;
  state.loading = false;
  update();
}

const onEnterPressed = (val) => {
  refTrackSelect.value.toggleOption(val);
}

let computedValuesCache = {};
const highlightBySubstrings = (stringToHighlight) => {
  if(computedValuesCache[stringToHighlight]){
    return computedValuesCache[stringToHighlight];
  }
  let computedValue = computed(() => {
    if(!state.currentSearchPartsForHighlight || state.currentSearchPartsForHighlight.length === 0 || !stringToHighlight){
      return stringToHighlight;
    }
    let searchPartsRegex = state.currentSearchPartsForHighlight.filter(part => !!part).join(')|(');
    if(!searchPartsRegex){
      return stringToHighlight;
    }
    let reg = new RegExp('('+searchPartsRegex+')', 'gi');
    let highligtedString = stringToHighlight.replace(reg, function(str) {
      return '<b class="search-highlight">'+str+'</b>'
    });
    return highligtedString;
  })
  computedValuesCache[stringToHighlight] = computedValue;
  return computedValue;
}

const onOptionSelected = (val) => {
  if(!val){
    return;
  }
  router.replace({
    query: {
      trackId: val.id
    }
  });
  emit('track-selected', val);
}

</script>

<style lang="sass" scoped>
:deep(.search-highlight)
  background-color: rgba(255,255,255,0.1)
  font-weight: 900
  color: rgba(255,255,255,1)
</style>
