<template>
  <q-page class="q-pa-md promo-grid" style="height: 100%">
    <img src="~assets/community-season/fall-season/Fall_Series_2024_-_Grand_Finals_-_Page_1.jpg" class="promo-img"/>
    <img src="~assets/community-season/fall-season/Fall_Series_2024_-_Grand_Finals_-_Page_2.jpg" class="promo-img"/>
    <img src="~assets/community-season/fall-season/Fall_Series_2024_-_Grand_Finals_-_Page_3.jpg" class="promo-img"/>
    <img src="~assets/community-season/fall-season/Fall_Series_2024_-_Grand_Finals_-_Page_4.jpg" class="promo-img"/>
    <img src="~assets/community-season/fall-season/Fall_Series_2024_-_Grand_Finals_-_Page_5.jpg" class="promo-img"/>
    <img src="~assets/community-season/fall-season/Fall_Series_2024_-_Grand_Finals_-_Page_6.jpg" class="promo-img"/>
  </q-page>
</template>

<script setup>
</script>

<style lang="sass" scoped>
.promo-grid
  display: grid
  gap: 1rem
  grid-template-columns: repeat(3, auto)
  grid-template-rows: auto auto
  align-content: start
  justify-content: start

  @media (max-width: $breakpoint-lg)
    grid-template-columns: repeat(2, auto)
    grid-template-rows: repeat(3, auto)

  @media (max-width: $breakpoint-md)
    grid-template-columns: auto
    grid-template-rows: repeat(6, auto)

.promo-img
  max-width: 570px
  max-height: 570px
  display: block
  object-fit: contain
</style>
