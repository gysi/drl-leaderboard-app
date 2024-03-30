<template>
    <q-dialog v-model="value" transition-show="scale" transition-hide="scale">
      <NewsContent />
    </q-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getDateDifferenceToNowByGermanTimezone } from 'src/modules/LeaderboardFunctions'
import NewsContent from "components/NewsContent.vue";

const props = defineProps(['showDialog'])
const emit = defineEmits(['update:showDialog'])

const value = computed({
  get() {
    return props.showDialog
  },
  set(value) {
    emit('update:showDialog', value)
  }
})
</script>
