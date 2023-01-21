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
    const prefersDarkScheme = window.matchMedia("(prefers-color-scheme: dark)");

    if(darkModeInStorage){
      document.body.classList.add("body--dark")
    } else if(darkModeInStorage === false){

    } else if (prefersDarkScheme.matches) {
      document.body.classList.add("body--dark")
    }
  }
})
</script>
