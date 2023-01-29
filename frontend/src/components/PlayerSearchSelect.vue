<template>
  <q-select
    ref="userselect"
    filled
    v-model="searchText"
    use-input
    hide-dropdown-icon
    autofocus
    input-debounce="150"
    :options="searchResults"
    @filter="playerSearch"
    @new-value="onEnterPressedPlayerSelect"
    @update:model-value="onPlayerSelected"
    style="width: 250px"
    class="q-ml-md"
    use-chips
    label="Enter player name"
  >
  <template v-slot:no-option>
    <q-item
      @click="onPlayerSelected"
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
  name: 'PlayerSearchSelect',
  props: {
    initialSelection: {
      type: String,
      default: null
    }
  },
  emits: ['onPlayerSelected'],
  data(){
    return {
      searchText: this.initialSelection !== "null" && this.initialSelection != null ? [this.initialSelection] : null,
      searchResults: [],
      loadingState: false,
    }
  },
  methods: {
    onEnterPressedPlayerSelect(val) {
      this.$refs.userselect.toggleOption(val);
    },
    async playerSearch(val, update, abort) {
      if(val === ""){
        this.searchResults = [];
        abort();
        return;
      }
      this.loadingState = true;
      const response = await axios.get(process.env.DLAPP_API_URL+'/leaderboards/find-players?playerName='+val);
      this.searchResults = response.data;
      this.loadingState = false;
      update();
    },
    onPlayerSelected(player) {
      this.$emit('onPlayerSelected', player);
    },
  }
}
</script>

<style lang="scss" scoped>

</style>
