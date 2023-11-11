<template>
  <div id="streamcard" :style="{
    width: decompressedSettings?.general.content.previewBox.content.width.settings.value + decompressedSettings?.general.content.previewBox.content.width.settings.unit,
    height: decompressedSettings?.general.content.previewBox.content.height.settings.value + decompressedSettings?.general.content.previewBox.content.height.settings.unit,
    color: decompressedSettings?.general.content.general.content.fontColor.settings.value,
    backgroundColor: settingsFromUrl ? null : decompressedSettings?.general.content.general.content.backgroundColor.settings.value,
  }">
    <!--    RANKING CARD    -->
    <div v-if="activeCard === 'rankingsCard'" :style="{
      padding: decompressedSettings?.rankingsCard.content.general.content.padding.settings.value + decompressedSettings?.rankingsCard.content.general.content.padding.settings.unit,
    }">
      <div :style='{
      fontSize: decompressedSettings?.rankingsCard.content.header.content.size.settings.value + decompressedSettings?.rankingsCard.content.header.content.size.settings.unit,
      lineHeight: decompressedSettings?.rankingsCard.content.header.content.lineHeight.settings.value + decompressedSettings?.rankingsCard.content.header.content.lineHeight.settings.unit,
      paddingLeft: decompressedSettings?.rankingsCard.content.header.content.paddingLeft.settings.value + decompressedSettings?.rankingsCard.content.header.content.paddingLeft.settings.unit,
      paddingRight: decompressedSettings?.rankingsCard.content.header.content.paddingRight.settings.value + decompressedSettings?.rankingsCard.content.header.content.paddingRight.settings.unit,
      paddingTop: decompressedSettings?.rankingsCard.content.header.content.paddingTop.settings.value + decompressedSettings?.rankingsCard.content.header.content.paddingTop.settings.unit,
      paddingBottom: decompressedSettings?.rankingsCard.content.header.content.paddingBottom.settings.value + decompressedSettings?.rankingsCard.content.header.content.paddingBottom.settings.unit,
    }'>
        {{ decompressedSettings?.rankingsCard.content.header.content.text.settings.value }}
      </div>
      <table :style="{
       width: decompressedSettings?.rankingsCard.content.rankings.content.width.settings.value + decompressedSettings?.rankingsCard.content.rankings.content.width.settings.unit,
       fontSize: decompressedSettings?.rankingsCard.content.rankings.content.fontSize.settings.value + decompressedSettings?.rankingsCard.content.rankings.content.fontSize.settings.unit,
      }">
        <colgroup>
          <col style="width: 15%">
          <col style="width: 45%">
          <col style="width: 40%">
        </colgroup>
        <tr v-for="position in topRankings" :key="position.position">
          <td>{{ position.position }}.</td>
          <td>{{ position.playerName }}</td>
          <td>{{ position.totalPoints }}</td>
        </tr>
      </table>
    </div>
    <!--    TRACK CARD     -->
    <template v-if="activeCard === 'trackCard'">
      <div>
        COMING SOON
      </div>
    </template>
  </div>
</template>

<script setup>
import {computed, onUnmounted, ref, watch} from "vue";
import {useRouter} from "vue-router";
import {decompressJSON} from "src/modules/JsonCompression";
import axios from 'axios';
import {debounce} from "quasar";

const router = useRouter();
const settingsFromUrl = router.currentRoute.value.query.settings;
const decompressedSettings = ref(null);
const activeCard = ref(null);
const updateCardEverySeconds = 10000;

// define vue property
const props = defineProps({
  compressedJson: String,
});

const applySettings = (settings) => {
  decompressedSettings.value = settings;
  activeCard.value = decompressedSettings.value.activeCard;
}

watch(() => props.compressedJson, (newVal) => {
  applySettings(decompressJSON(newVal));
});

watch(() => activeCard.value, (newVal, oldValue) => {
  clearTimeouts();
  if (newVal === 'rankingsCard') {
    fetchRankings();
  }
});

const clearTimeouts = () => {
  if (rankingsTimer) {
    clearTimeout(rankingsTimer);
    rankingsTimer = null;
  }
}

// ---------------------------------------------Rankings Card
let rankingsTimer = null;
const rankings = ref(null);
const topRankings = computed(() => {
  if (!rankings.value) {
    return [];
  }
  const topPositionsToDisplay = decompressedSettings.value.rankingsCard.content.rankings.content.topPositionCount.settings.value;
  const topPositions = rankings.value.slice(0, topPositionsToDisplay);
  const focusedPlayer = decompressedSettings.value.general.content.general.content.player.settings.value;
  const focusedPlayerPosition = rankings.value.findIndex((player) => player.playerName === focusedPlayer);

  const addUniquePlayer = (list, player) => {
    if (list.findIndex((p) => p.playerName === player.playerName) === -1) {
      list.push(player);
    }
  };

  if (focusedPlayerPosition + 1 > topPositionsToDisplay) {
    const topPositionsAboveFocusedPlayer = rankings.value.slice(focusedPlayerPosition - 1, focusedPlayerPosition);
    addUniquePlayer(topPositions, rankings.value[focusedPlayerPosition]);
    const topPositionsBelowFocusedPlayer = rankings.value.slice(focusedPlayerPosition + 1, focusedPlayerPosition + 2);

    topPositionsAboveFocusedPlayer.forEach((player) => {
      addUniquePlayer(topPositions, player);
    });

    topPositionsBelowFocusedPlayer.forEach((player) => {
      addUniquePlayer(topPositions, player);
    });
  }

  // sort positions by position
  topPositions.sort((a, b) => {
    return a.position - b.position;
  });

  return topPositions;
});

const fetchRankings = debounce(async () => {
  const response = await axios.get(process.env.DLAPP_API_URL + '/leaderboards/overall-ranking?page=1&limit=500');
  // const response = await axios.get('http://192.168.178.31:8080/api/leaderboards/overall-ranking?page=1&limit=500');

  rankings.value = response.data;
  rankingsTimer = setTimeout(() => {
    if (activeCard.value === 'rankingsCard') {
      fetchRankings();
    }
  }, updateCardEverySeconds);
}, 300);
// ---------------------------------------------Rankings Card END

// ---------------------------------------------Worst Track Card

// ---------------------------------------------Worst Track Card END

if (props.compressedJson != null) {
  applySettings(decompressJSON(props.compressedJson));
} else if (settingsFromUrl) {
  const decompressedSettingsFromUrl = decompressJSON(decodeURIComponent(settingsFromUrl))
  const style = `
    body {
      background-color: ${decompressedSettingsFromUrl?.general.content.general.content.backgroundColor.settings.value} !important;
    }
  `;
  const styleTag = document.createElement('style');
  styleTag.textContent = style;
  document.head.appendChild(styleTag);
  applySettings(decompressJSON(decodeURIComponent(settingsFromUrl)));
}

onUnmounted(() => {
  clearTimeouts();
});
</script>

<style lang="scss" scoped>
</style>

