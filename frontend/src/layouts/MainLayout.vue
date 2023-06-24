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

        <q-toggle
          label="Dark Mode"
          :model-value="darkMode"
          @update:model-value="this.toggleDarkMode"
          left-label
          color="accent"
        />
        <div style="margin-left: 15px">by gysi (v{{ version }})</div>
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
        <q-item
          clickable
          @click="showNews = true"
          exact
        >
          <q-item-section
            avatar
          >
            <q-icon name="feed"/>
          </q-item-section>

          <q-item-section>
            <q-item-label>News</q-item-label>
            <q-item-label caption>Releases and stuff</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container style="height: 100dvh">
      <router-view/>
    </q-page-container>
    <NewsDialog v-model:show-dialog="showNews" class="dialog-news"/>
  </q-layout>
</template>

<script>
import {defineComponent, ref} from 'vue'
import NavigationLinks from 'components/NavigationLinks.vue'
import {version} from '../../package.json'
import {useQuasar} from "quasar"
import NewsDialog from "components/NewsDialog.vue";
import isCrawler from "src/modules/isCrawler";

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
    title: 'Rankings',
    caption: 'Player rankings by category',
    icon: 'leaderboard', // sports_score as alternative
    link: '/overall-rankings'
  },
  {
    title: 'Tournaments',
    caption: 'Overview of all tournaments',
    icon: 'emoji_events',
    link: '/tournaments',
    badge: 'PREVIEW'
  },
  {
    title: 'Track Leaderboard',
    caption: 'Leaderboard for a specific track',
    icon: 'timeline',
    link: '/track-lb'
  },
  {
    title: 'Player Leaderboard',
    caption: 'Leaderboard for a specific player',
    icon: 'sports_martial_arts',
    link: '/player-lb'
  },
  {
    title: 'Gimme a random track!',
    caption: 'Gives you random worst tracks',
    icon: 'casino',
    link: '/worst-tracks-picker'
  },
  {
    title: 'Replay Viewer',
    caption: 'Analyse and compare replays',
    icon: 'visibility',
    link: '/replay-viewer',
    badge: 'PREVIEW'
  },
  {
    title: 'Stream Card Creator',
    caption: 'Create stream cards for your stream',
    link: '/stream-card-creator',
    icon: 'movie_filter',
    badge: 'PREVIEW'
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
    link: process.env.DLAPP_API_URL + process.env.DLAPP_SWAGGER_URL_PART,
    external: true
  }
]
export default defineComponent({
  name: 'MainLayout',

  components: {
    NewsDialog,
    NavigationLinks
  },
  setup() {
    const $q = useQuasar()
    const leftDrawerOpen = ref(false)
    const darkModeInStorage = $q.localStorage.getItem('darkMode');
    const prefersDarkScheme = window.matchMedia("(prefers-color-scheme: dark)");
    let useDarkMode = ref(false);
    if (darkModeInStorage) {
      useDarkMode = ref(true);
      $q.dark.set(true);
    } else if (darkModeInStorage === false) {

    } else if (prefersDarkScheme.matches) {
      useDarkMode = ref(true);
      $q.dark.set(true);
    }

    let showNewsDialog = false;
    let newsDialogVersion = $q.localStorage.getItem('newsDialogSeen');
    if (newsDialogVersion !== version && isCrawler() === false) {
      $q.localStorage.set('newsDialogSeen', version);
      showNewsDialog = true;
    }

    return {
      linksList: linksList,
      leftDrawerOpen,
      toggleLeftDrawer() {
        leftDrawerOpen.value = !leftDrawerOpen.value
      },
      quasar: $q,
      darkMode: useDarkMode,
      showNews: ref(showNewsDialog)
    }
  },
  data() {
    return {
      version: version,
    }
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      this.$q.localStorage.set('darkMode', this.darkMode);
      this.$q.dark.set(!!this.darkMode)
      if (this.darkMode) {
        document.body.classList.add("body--dark")
      } else {
        document.body.classList.remove("body--dark");
      }
    }
  },
})
</script>

<style lang="scss">
.q-drawer {
  .q-item__section--side {
    padding-right: 16px;
  }

  .q-item__section--avatar {
    min-width: unset;
  }
}

.first-place {
  background-image: url('assets/gold-medal.svg');
}

.second-place {
  background-image: url('assets/silver-medal.svg');
}

.third-place {
  background-image: url('assets/bronze-medal.svg');
}

.first-place, .second-place, .third-place {
  //background-position-x: 50.5%
  //background-position-y: -0.5rem
  background-position-x: 0.3rem;
  background-position-y: -0.1rem;
  background-repeat: no-repeat;
  background-size: 1.5rem;
  background-origin: border-box;
}

.my-sticky-header-table {
  /* height or max-height is important */
  //min-height: inherit

  thead tr th {
    position: sticky;
    z-index: 1;
    font-size: 18px;
  }

  thead tr:first-child th {
    //color: white
    top: 0;
  }

  td {
    font-size: 200px;
  }

  /* this is when the loading indicator appears */
  &.q-table--loading thead tr:last-child th {
    /* height of all previous header rows */
    top: 48px
  }
}

tbody .td-borders-font-size16 {
  //color: black
  border-left: 1px solid rgb(0, 0, 0, 0.4);
  border-right: 0;
  border-top: 1px solid rgb(0, 0, 0, 0.4);
  border-bottom: 0;
  font-weight: normal;
  font-size: 16px;
}

.leaderboard-position-column {
  max-width: 65px;
  width: 65px;
  font-weight: bolder;
  color: #f6f6f6;
  text-shadow: 1px 0px 0.2px black, -1px 0px 0.2px black, 0px 1px 0.2px black, 0px -1px 0.2px black;
}

.button-fills-whole-td {
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  position: absolute;
}

// AssetStudioGUI -> Filter Type = Texture2D -> Export -> "Filtered assets"
// exported maps are named like background*
// montage -resize 120 map-* -auto-orient -geometry +1+1 -background none -format png maps.png
// http://www.spritecow.com/
// then use the tool pngquant to make the source pngs smaller
.map-2017-world-championship {
  object-fit: none;
  object-position: -1px -1px;
  width: 120px !important;
  height: 68px !important;
}

.map-adventuredome {
  object-fit: none;
  object-position: -123px -1px;
  width: 120px !important;
  height: 68px !important;
}

.map-allianz-riviera {
  object-fit: none;
  object-position: -245px -1px;
  width: 120px !important;
  height: 68px !important;
}

.map-atlanta-aftermath {
  object-fit: none;
  object-position: -367px -1px;
  width: 120px !important;
  height: 68px !important;
}

.map-biosphere-2 {
  object-fit: none;
  object-position: -489px -1px;
  width: 120px !important;
  height: 68px !important;
}

.map-bmw-welt {
  object-fit: none;
  object-position: -611px -1px;
  width: 120px !important;
  height: 68px !important;
}

.map-boston-foundry {
  object-fit: none;
  object-position: -1px -71px;
  width: 120px !important;
  height: 68px !important;
}

.map-bridge {
  object-fit: none;
  object-position: -123px -71px;
  width: 120px !important;
  height: 68px !important;
}

.map-california-nights {
  object-fit: none;
  object-position: -245px -71px;
  width: 120px !important;
  height: 68px !important;
}

.map-campground {
  object-fit: none;
  object-position: -367px -71px;
  width: 120px !important;
  height: 68px !important;
}

.map-championship-kingdom {
  object-fit: none;
  object-position: -367px -71px;
  width: 120px !important;
  height: 68px !important;
}

.map-detroit {
  object-fit: none;
  object-position: -611px -71px;
  width: 120px !important;
  height: 68px !important;
}

.map-drl-sandbox {
  object-fit: none;
  object-position: -1px -141px;
  width: 120px !important;
  height: 68px !important;
}

.map-drone-park {
  object-fit: none;
  object-position: -123px -141px;
  width: 120px !important;
  height: 68px !important;
}

.map-gates-of-new-york {
  object-fit: none;
  object-position: -245px -141px;
  width: 120px !important;
  height: 68px !important;
}

.map-hard-rock-stadium {
  object-fit: none;
  object-position: -367px -141px;
  width: 120px !important;
  height: 68px !important;
}

.map-l-a-pocalypse {
  object-fit: none;
  object-position: -367px -141px;
  width: 120px !important;
  height: 68px !important;
}

.map-mardi-gras-world {
  object-fit: none;
  object-position: -611px -141px;
  width: 120px !important;
  height: 68px !important;
}

.map-mega-city {
  object-fit: none;
  object-position: -1px -211px;
  width: 120px !important;
  height: 68px !important;
}

.map-miami-lights {
  object-fit: none;
  object-position: -1px -211px;
  width: 120px !important;
  height: 68px !important;
}

.map-multigp {
  object-fit: none;
  object-position: -245px -211px;
  width: 120px !important;
  height: 68px !important;
}

.map-munich-playoffs {
  object-fit: none;
  object-position: -367px -211px;
  width: 120px !important;
  height: 68px !important;
}

.map-ohio-crashsite {
  object-fit: none;
  object-position: -489px -211px;
  width: 120px !important;
  height: 68px !important;
}

.map-out-of-service {
  object-fit: none;
  object-position: -611px -211px;
  width: 120px !important;
  height: 68px !important;
}

.map-project-manhattan {
  object-fit: none;
  object-position: -1px -281px;
  width: 120px !important;
  height: 68px !important;
}

.map-silicon-valley {
  object-fit: none;
  object-position: -123px -281px;
  width: 120px !important;
  height: 68px !important;
}

.map-skatepark-la {
  object-fit: none;
  object-position: -245px -281px;
  width: 120px !important;
  height: 68px !important;
}

.map-the-house {
  object-fit: none;
  object-position: -367px -281px;
  width: 120px !important;
  height: 68px !important;
}

.map-u-s--air-force-boneyard {
  object-fit: none;
  object-position: -489px -281px;
  width: 120px !important;
  height: 68px !important;
}

.map-u-s--air-force-night-mode {
  object-fit: none;
  object-position: -611px -281px;
  width: 120px !important;
  height: 68px !important;
}

.table-with-brackground-image {
  .q-table__top .q-table__control {
    width: 100%;
    position: relative;
    align-items: flex-start !important;
  }

  .q-table__top {
    overflow: hidden;
    padding: 0;
  }

  .q-field--filled .q-field__control {
    background-color: rgb(0, 0, 0, 0.7);
  }

  // for f in map-*.png; do convert "$f" -gaussian-blur 5x5 -quality 100 "${f%.*}-blurred.png"; done
  // pngquant --speed 1 --force --strip map-*-blurred.png
  .animated-background-image {
    position: absolute;
    object-fit: contain;
    box-sizing: content-box;
    width: 100%;
    //animation: move-track-background 60s ease-in-out 2s infinite;
    animation: move-track-background 90s steps(calc(25 * 90), end) 2s infinite;
    transform: translateY(-3%);
    will-change: transform;
  }
}

@keyframes move-track-background {
  0% {
    transform: translateY(-3%);
  }
  50% {
    transform: translateY(calc(-97% + 92px));
  }
  100% {
    transform: translateY(-3%);
  }
}

@media (min-width: 600px) {
  .q-dialog__inner--minimized > div {
    max-width: 700px !important;
  }
}
</style>
