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

        <div>by gysi</div>
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
