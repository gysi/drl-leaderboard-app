<template>
  <q-page class="q-pa-md" style="height: 100%">
    <NewsContent />
  </q-page>
</template>
<script setup>
import NewsContent from "components/NewsContent.vue";
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "News",
  meta: {
    description: {
      name: 'description',
      content: `Here you will mostly find news about new features, changes and bugfixes regarding the DRL Leaderboard website.`
    }
  }
})
</script>
