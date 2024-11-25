<template>
  <q-page class="q-pa-md items-start" style="height: 100%">
    <iframe
      :src="pdfDoc"
      width="100%"
      height="100%">
    </iframe>
  </q-page>
</template>

<script setup>
import pdf from 'assets/community-season/fall-season/DRL_Community_FalL_Season_Guide-Public_v1.0.pdf'

const pdfDoc = pdf;
</script>

<style scoped>

</style>
