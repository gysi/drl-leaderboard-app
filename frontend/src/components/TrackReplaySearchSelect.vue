<template>
  <q-select
    ref="refSelect"
    filled
    v-model="state.searchText"
    use-input
    autofocus
    input-debounce="250"
    :options="state.searchResults"
    @filter="search"
    @new-value="onEnterPressed"
    @update:model-value="onSelected"
    style="width: 350px"
    class="q-ml-md"
    use-chips
    label="Select player"
    :loading="state.loading"
    popup-content-class="q-menu-dropdown"
  >
    <template v-slot:selected-item="props">
      <q-chip
        v-if="props.selected"
        :key="props.selected"
        :value="props.selected"
        removable
        @remove="() => { state.searchText = null }">{{ props.opt.playerName }}</q-chip>
    </template>
    <template v-slot:option="props">
      <q-item
        v-bind="props.itemProps"
        clickable
        :active="props.selected"
        :style="props.selected ? 'background: #e0e0e0' : ''"
        @update:model-value="props.toggleOption(props.opt)"
      >
        <q-item-section side>
          <q-badge color="accent" align="middle" :label="props.opt.position" />
        </q-item-section>
        <q-item-section>
          <q-item-label>{{ props.opt.playerName }}</q-item-label>
        </q-item-section>
      </q-item>
    </template>
    <template v-slot:no-option>
      <q-item
        @click="onSelected"
      >
        <q-item-section class="text-grey">
          No results
        </q-item-section>
      </q-item>
    </template>
  </q-select>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const props = defineProps({
  trackId: {
    type: String,
    default: null
  }
});
const emit = defineEmits(['on-selected'])
const refSelect = ref(null);
const state = reactive({
  searchText: null,
  searchResults: [],
  replays: [],
  loading: false,
});

//watch for trackId changes
watch(() => props.trackId, (val) => {
  // console.log("trackId changed", val);
  if(val){
    state.searchText = null;
    state.searchResults = null;
    state.replays = null;
    fetchData(val);
  }
});

const fetchData = async (trackId) => {
  state.loading = true;
  try {
    const response = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/replays/bytrack/'+trackId);
    // console.log("fetching data", response.data);
    state.replays = response.data;
    state.searchResults = response.data;
  } catch(error) {
    console.error(error);
  } finally {
    // console.log("fetching data done")
    state.loading = false;
  }
}

const search = (val, update, abort) => {
  if(!val){
    state.searchResults = state.replays;
  }
  state.loading = true;
  let searchResults = [];
  val = val.toLowerCase().split(' ');
  state.replays.filter((replay) => {
    let found = true;
    val.forEach((v) => {
      if(!replay.playerName.toLowerCase().includes(v)){
        found = false;
      }
    });
    if(found){
      searchResults.push(replay);
    }
  });
  state.searchResults = searchResults;
  state.loading = false;
  update();
}

const onEnterPressed = (val) => {
  refSelect.value.toggleOption(val);
}

const onSelected = (player) => {
  emit('on-selected', player);
};
</script>

<style lang="scss" scoped>

</style>
