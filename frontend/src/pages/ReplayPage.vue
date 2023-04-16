<template>
  <q-page style="height: 100%; width: 100%" class="q-pa-md">
    <replay-viewer
      :track-id="state.selectedTrackId"
      :replay-url="state.selectedReplayUrl"
    >
      <template v-slot:header>
        <div>
          <TracksSearchSelect
            @track-selected="onTrackSelected"
            class="q-mt-md q-ml-md"
          />
          <TrackReplaySearchSelect
            :disable="state.replaySelectionDisabled"
            :track-id="state.selectedTrackId"
            @on-selected="onReplaySelected"
            class="q-mt-md q-ml-md"
          />
        </div>
      </template>
    </replay-viewer>
  </q-page>
</template>

<script setup>
  import { reactive } from 'vue';
  import ReplayViewer from "components/ReplayViewer.vue";
  import TracksSearchSelect from "components/TracksSearchSelect.vue";
  import TrackReplaySearchSelect from "components/TrackReplaySearchSelect.vue";

  const state = reactive({
    replaySelectionDisabled: true,
    selectedTrackId: null,
    selectedReplayUrl: null
  });

  const onTrackSelected = (track) => {
    console.log("track selected", track);
    state.replaySelectionDisabled = false;
    state.selectedReplayUrl = null;
    state.selectedTrackId = track.id;
  }

  const onReplaySelected = (replay) => {
    console.log("replay selected", replay);
    state.selectedReplayUrl = replay.replayUrl;
  }

</script>

<style lang="sass" scoped>
:deep(.q-field__control)
  background: rgba(255,255,255,0.2) !important
</style>
