<template>
  <div>
    <q-btn
      :style="{ background: selectedColor, color: isDark(selectedColor) ? 'white' : 'black' }"
      @click="showColorPicker = !showColorPicker"
    >
      {{ selectedColor }}
      <q-menu>
        <q-color
          v-model="selectedColor"
        />
      </q-menu>
    </q-btn>

  </div>
</template>

<script setup>
import {computed, ref} from 'vue';
import {colors as quasarColors} from 'quasar'

const {luminosity} = quasarColors

const showColorPicker = ref(false);

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue'])

const selectedColor = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emit('update:modelValue', value)
  }
})

function isDark(val) {
  return luminosity(val) <= 0.4
}

</script>
