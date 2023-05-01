<template>
  <template v-if="value.type === 'input'">
    <div>
      <div>{{ value.name }}</div>
      <q-input filled v-model="value.settings.value"/>
    </div>
  </template>
  <template v-if="value.type === 'slider'">
    {{ value.name }}
    <q-slider
      v-model="value.settings.value"
      :min="value.settings.min"
      :max="value.settings.max"
      :step="value.settings.step"
      label
      :label-value="value.settings.value + value.settings.unit"
      color="purple"
    />
  </template>
  <template v-if="value.type === 'color'">
    <div>
      <div>{{ value.name }}</div>
      <ColorPickerButton v-model="value.settings.value"/>
    </div>
  </template>
</template>

<script setup>
import {computed} from "vue";
import ColorPickerButton from "components/ColorPickerButton.vue";

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue'])

const value = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emit('update:modelValue', value)
  }
})
</script>

<style lang="sass">

</style>
