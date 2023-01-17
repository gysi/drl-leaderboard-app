<template>
  <router-view />
</template>

<script>
import { defineComponent } from 'vue'
import { useQuasar } from 'quasar'

export default defineComponent({
  name: 'App',
  setup() {
    const $q = useQuasar()
    let darkModeInStorage = $q.localStorage.getItem('darkMode');
    if(darkModeInStorage !== null) {
      $q.dark.set(darkModeInStorage);
    }
  }
})
</script>
