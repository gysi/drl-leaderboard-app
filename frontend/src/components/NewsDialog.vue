<template>
  <q-dialog
    v-model="value"
    transition-show="scale"
    transition-hide="scale"
    maximized
    @show="handleDialogShow">
    <NewsContent />
  </q-dialog>
</template>

<script setup>
import { computed } from 'vue';
import NewsContent from "components/NewsContent.vue";

const props = defineProps(['showDialog']);
const emit = defineEmits(['update:showDialog']);

const value = computed({
  get() {
    return props.showDialog;
  },
  set(value) {
    emit('update:showDialog', value);
  }
});

const handleDialogShow = () => {
  console.log("Dialog opened: Re-initializing content if needed.");
  // You can add any extra logic here if required on dialog open
};
</script>
