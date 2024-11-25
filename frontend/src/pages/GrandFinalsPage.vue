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
  --max-image-size: 570px
  display: grid
  gap: 1rem
  grid-template-columns: repeat(3, minmax(0, var(--max-image-size))) // 3 columns that shrink and grow equally
  grid-template-rows: auto // Let rows grow based on content
  align-content: start
  justify-content: start // Center the grid horizontally if it doesn’t take full width

  @media (max-width: $breakpoint-lg)
    grid-template-columns: repeat(2, minmax(0, var(--max-image-size))) // Adjust to 2 columns
    grid-template-rows: auto

  @media (max-width: $breakpoint-md)
    grid-template-columns: var(--max-image-size) // Single column
    grid-template-rows: auto

.promo-img
  max-width: var(--max-image-size) // Prevent overflow
  max-height: var(--max-image-size) // Ensure images fit within grid cells
  width: 100% // Allow images to fill the grid cell
  height: 100% // Allow images to fill the grid cell
  object-fit: contain // Preserve aspect ratio without cropping
  display: block
  margin: auto // Center the image inside its grid cell
</style>
