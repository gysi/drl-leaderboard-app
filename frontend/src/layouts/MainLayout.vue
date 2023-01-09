<template>
  <q-layout view="hHh lpR lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />

        <q-toolbar-title>
          <router-link style="text-decoration: none; color: inherit;" to="/">
            DRL Leaderboards
          </router-link>
        </q-toolbar-title>

        <div>by gysi (v.{{ version }})</div>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
    >
      <q-list>
        <NavigationLinks
          v-for="link in linksList"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>

    <q-page-container style="height: 100vh">
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent, ref } from 'vue'
import NavigationLinks from 'components/NavigationLinks.vue'
import {version} from '../../package.json'

const linksList = [
  {
    title: 'Home',
    icon: 'home',
    link: '/',
    external: false
  },
  {
    title: 'Tracks',
    caption: 'Overview of all tracks',
    icon: 'public',
    link: '/tracks'
  },
  {
    title: 'Overall Rankings',
    caption: 'Overall rankings of all tracks',
    icon: 'leaderboard', // sports_score as alternative
    link: '/overallrankings'
  },
  {
    title: 'Track Leaderboard',
    caption: 'Leaderboard for a specific track',
    icon: 'timeline',
    link: '/tracklb'
  },
  {
    title: 'Player Leaderboard',
    caption: 'Leaderboard for a specific player',
    icon: 'sports_martial_arts',
    link: '/playerlb'
  },
  {
    title: 'FAQ',
    caption: 'Frequently asked questions',
    icon: 'help',
    link: '/faq'
  },
  {
    title: 'API',
    caption: 'API documentation',
    icon: 'code',
    link: process.env.DLAPP_API_URL+process.env.DLAPP_SWAGGER_URL_PART,
    external: true
  }
]
export default defineComponent({
  name: 'MainLayout',

  components: {
    NavigationLinks
  },
  data() {
    return {
      version: version
    }
  },
  setup () {
    const leftDrawerOpen = ref(false)

    return {
      linksList: linksList,
      leftDrawerOpen,
      toggleLeftDrawer () {
        leftDrawerOpen.value = !leftDrawerOpen.value
      }
    }
  }
})
</script>

<style lang="sass">
.first-place
  background-image: url('assets/gold-medal.svg')

.second-place
  background-image: url('assets/silver-medal.svg')

.third-place
  background-image: url('assets/bronze-medal.svg')

.first-place, .second-place, .third-place
  //background-position-x: 50.5%
  //background-position-y: -0.5rem
  background-position-x: 0.3rem
  background-position-y: -0.1rem
  background-repeat: no-repeat
  background-size: 1.5rem
  background-origin: border-box

.q-field--filled .q-field__control
  background: rgba(0, 0, 0, 0.2)
  color: white

.q-field__label
  color: $secondary

.my-sticky-header-table
  /* height or max-height is important */
  //min-height: inherit

  .q-table__top,
  thead tr:first-child th
    /* bg color is important for th; just specify one */
    background-color: $primary
    color: white

  thead tr th
    position: sticky
    z-index: 1
    font-size: 18px

  thead tr:first-child th
    //color: white
    top: 0

  td
    font-size: 200px

  /* this is when the loading indicator appears */
  &.q-table--loading thead tr:last-child th
    /* height of all previous header rows */
    top: 48px

tbody .q-td
  color: black
  background-color: $secondary
  border-left: 1px solid black
  border-right: 0
  border-top: 1px solid black
  border-bottom : 0
  font-weight: normal
  font-size: 16px
  margin-right: 100px

tbody .q-tr .q-td:first-child
  min-width: 70px
  font-weight: bolder
  color: #f6f6f6
  text-shadow: 1px 0px 0.2px black, -1px 0px 0.2px black, 0px 1px 0.2px black, 0px -1px 0.2px black
</style>
