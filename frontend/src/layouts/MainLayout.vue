<template>
  <q-layout view="hHh LpR lFf">
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
          @update:model-value="toggleDarkMode"
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
      <div style="height: 100%; display: grid; grid-template-rows: auto auto">
        <div>
          <template v-for="(item, index) in linksListTop" :key="index">
            <!-- Render single link -->
            <NavigationLinks v-bind="item"/>
            <!-- Render section -->
          </template>
          <q-card class="q-ma-sm q-pa-none">
            <q-item class="q-ma-none q-pa-none" style="min-height:0">
              <q-item-section>
                <q-item-label header class="q-pt-sm q-px-sm q-pb-none text-overline">
                  COMMUNITY
                </q-item-label>
              </q-item-section>
              <q-item-section side style="padding-left: 5px; padding-right: 5px; padding-top: 5px;">
                <q-badge color="accent" style="" :label="currentSeasonLabel"/>
              </q-item-section>
            </q-item>
            <q-card-section class="q-ma-none q-pa-none">
              <NavigationLinks v-for="link in linksListCommunitySeason" :key="link.title" v-bind="link"/>
            </q-card-section>
            <q-item v-if="currentSeason.details_v1?.matcherino?.promoBannerImageName" :href="currentSeason.details_v1.matcherino.matcherinoEventLink"
                    target="_blank"
                    class="q-pa-none q-ma-xs q-pb-xs"
            >
              <q-item-section style="position: relative">
                <section id="glitch-image-1" :class="currentSeason.details_v1.matcherino.promoBannerImageName"></section>
                <section id="glitch-image-2" :class="currentSeason.details_v1.matcherino.promoBannerImageName"></section>
              </q-item-section>
            </q-item>
          </q-card>

          <q-card class="q-ma-sm" style="">
            <q-item class="q-ma-none q-pa-none" style="min-height:0">
              <q-item-section>
                <q-item-label header class="q-py-sm q-px-sm q-pb-none text-overline">
                  FEATURED TWITCH STREAMS
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-list v-if="twitchStreams.length" class="q-pa-sm" style="max-height: 300px; overflow: auto">
              <q-item clickable :href="`https://twitch.tv/${stream.userLogin}`" target="_blank" v-for="(stream, index) in twitchStreams"
                      :key="index"
                      class="q-mt-none q-pa-none q-mb-sm">
                <q-item-section style="max-width: 90px">
                  <q-avatar size="3rem" class="self-center" style="box-shadow: 4px 4px 8px rgba(0, 0, 0, 1);">
                    <img :src="stream.userThumbnail" loading="eager"/>
                  </q-avatar>
                  <q-item-label lines="1" class="q-pt-sm text-center">{{ stream.userName }}</q-item-label>
                </q-item-section>

                <q-item-section class="q-pr-none">
                  <div style="width: 150px; height:84px; position: relative">
                    <img :src="stream.streamThumbnail" loading="eager"/>
                    <q-badge color="red" class="absolute" style="top: 5px; right: 5px">LIVE</q-badge>
                    <q-badge color="black" class="absolute" style="bottom: 5px; right: 5px; background: rgba(0,0,0,0.7) !important">
                      {{ stream.viewerCount }}
                      <q-icon name="person" color="white" class="q-ml-xs"/>
                    </q-badge>
                  </div>
                </q-item-section>
              </q-item>
            </q-list>
            <q-item v-else class="q-mt-none q-pb-sm q-pt-none">
              <q-item-section>
                <q-item-label>There are currently no DRL streams.</q-item-label>
              </q-item-section>
            </q-item>
          </q-card>
          <!--        AD Banner-->
          <!--          <q-item href=-->
          <!--                    "https://discord.gg/sgps" target="_blank">-->
          <!--            <q-item to=-->
          <!--                    "community-rankings">-->
        </div>
        <div style="align-self: end">
          <template v-for="(item, index) in linksListBottom" :key="index">
            <!-- Render single link -->
            <NavigationLinks v-if="!item.sectionTitle" v-bind="item"/>
            <!-- Render section -->
            <q-card v-else class="q-ma-sm q-pa-none">
              <q-item class="q-ma-none q-pa-none" style="min-height:0">
                <q-item-section>
                  <q-item-label header class="q-pt-sm q-px-sm q-pb-none text-overline">
                    {{ item.sectionTitle }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side style="padding-right: 5px; padding-top: 5px">
                  <q-badge color="accent" style="" v-if="item.badge" :label="item.badge"/>
                </q-item-section>
              </q-item>
              <q-card-section class="q-ma-none q-pa-sm">
                <NavigationLinks v-for="link in item.links" :key="link.title" v-bind="link"/>
              </q-card-section>
            </q-card>
          </template>
          <q-item clickable>
            <q-item-section avatar>
              <q-icon name="widgets"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>Other</q-item-label>
              <q-item-label caption>News / Misc / External links</q-item-label>
            </q-item-section>
            <q-menu auto-close transition-show="jump-up" transition-hide="jump-down" transition-duration="150"
                    anchor="top left" self="bottom left">
              <q-item clickable @click="showNews = true" exact>
                <q-item-section avatar>
                  <q-icon name="feed"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>News</q-item-label>
                  <q-item-label caption>Releases and stuff</q-item-label>
                </q-item-section>
              </q-item>
              <template v-for="(item, index) in linksListMenu" :key="index">
                <!-- Render single link -->
                <NavigationLinks v-if="!item.sectionTitle" v-bind="item"/>
                <!-- Render section -->
                <q-card v-else class="q-ma-sm q-pa-none">
                  <q-card-section class="q-ma-none q-pa-none">
                    <q-item-label header class="q-pt-sm q-px-sm q-pb-none text-overline">{{ item.sectionTitle }}</q-item-label>
                  </q-card-section>
                  <q-card-section class="q-ma-none q-pa-sm">
                    <NavigationLinks v-for="link in item.links" :key="link.title" v-bind="link"/>
                  </q-card-section>
                </q-card>
              </template>
            </q-menu>
          </q-item>
        </div>
      </div>
    </q-drawer>

    <q-page-container style="height: 100dvh">
      <router-view/>
    </q-page-container>
    <NewsDialog v-model:show-dialog="showNews" class="dialog-news"/>
  </q-layout>
</template>

<script setup>
import {nextTick, onBeforeUnmount, onMounted, ref} from 'vue'
import NavigationLinks from 'components/NavigationLinks.vue'
import {version} from '../../package.json'
import {useQuasar} from "quasar"
import NewsDialog from "components/NewsDialog.vue";
import isCrawler from "src/modules/isCrawler";
import axios from "axios";
import {fetchCurrentSeason} from "src/modules/backendApi.js";
import {
  NAVIGATION_QUAL_TIME_TRAIL_FINISH_TOURNAMENT,
  NAVIGATION_QUAL_TIME_TRIAL_AND_TOURNAMENTS_FINISH_TOURNAMENT, NAVIGATION_QUAL_TIME_TRIAL_FINISH_TIME_TRIAL
} from "src/modules/navigation.js";
import { toLocalDateformat } from "src/modules/LeaderboardFunctions.js";

const linksListTop = [
  {
    type: 'link',
    title: 'Home',
    icon: 'home',
    link: '/',
    external: false
  },
  // {
  //   title: 'Tracks',
  //   caption: 'Overview of all tracks',
  //   icon: 'route',
  //   link: '/tracks'
  // },
  {
    type: 'link',
    title: 'Rankings',
    // caption: 'Player rankings by category',
    icon: 'leaderboard', // sports_score as alternative
    link: '/overall-rankings'
  },
  {
    type: 'link',
    title: 'Player Leaderboard',
    // caption: 'Leaderboard for a specific player',
    icon: 'sports_martial_arts',
    link: '/player-leaderboard'
  },
  {
    type: 'link',
    title: 'Track Leaderboard',
    // caption: 'Leaderboard for a specific track',
    icon: 'timeline',
    link: '/track-leaderboard'
  },
  {
    type: 'link',
    title: 'Weekly DRL Tournaments',
    // caption: 'Seasonal Tournament Rankings',
    icon: 'emoji_events',
    link: '/tournaments'
  },
  {
    type: 'link',
    title: 'Replay Viewer',
    // caption: 'Analyse and compare replays',
    icon: 'visibility',
    link: '/replay-viewer',
    // badge: 'PREVIEW'
  }];
const linksListCommunitySeason = ref([])

const linksListBottom = [];

const linksListMenu = [
  {
    type: "link",
    title: 'Gimme a random track!',
    caption: 'Gives you random worst tracks',
    icon: 'casino',
    link: '/worst-tracks-picker'
  },
  {
    type: "link",
    title: 'Stream Card Creator',
    caption: 'Create stream cards for your stream',
    link: '/stream-card-creator',
    icon: 'movie_filter',
    // badge: 'PREVIEW'
  },
  {
    type: "link",
    title: 'FAQ',
    caption: 'Frequently asked questions',
    icon: 'help',
    link: '/faq'
  },
  {
    type: "link",
    title: 'DRL Betaflight Calculator',
    caption: '1:1 Replica, play with it!',
    icon: 'calculate',
    link: process.env.DLAPP_BETAFLIGHT_CALCULATOR,
    external: true,
    openInNew: true
  },
  {
    type: "link",
    title: 'API',
    caption: 'API documentation',
    icon: 'code',
    link: `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
      + `${process.env.DLAPP_SWAGGER_URL_PART}`,
    external: true,
    openInNew: true
  }
];

const $q = useQuasar();
const leftDrawerOpen = ref(false);
const darkMode = ref(false);
const showNews = ref(false);
const twitchStreams = ref([]);

const darkModeInStorage = $q.localStorage.getItem('darkMode');
const prefersDarkScheme = window.matchMedia("(prefers-color-scheme: dark)");
if (darkModeInStorage) {
  darkMode.value = true;
  $q.dark.set(true);

} else if (darkModeInStorage === false) {
} else if (prefersDarkScheme.matches) {
  darkMode.value = true;
  $q.dark.set(true);

}
let newsDialogVersion = $q.localStorage.getItem('newsDialogSeen');
if (newsDialogVersion !== version && isCrawler() === false) {
  $q.localStorage.set('newsDialogSeen', version);
  showNews.value = true;
}

const toggleLeftDrawer = function () {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

const toggleDarkMode = function () {
  darkMode.value = !darkMode.value;
  $q.localStorage.set('darkMode', darkMode.value);
  $q.dark.set(!!darkMode.value)
  if (darkMode.value) {
    document.body.classList.add("body--dark")
  } else {
    document.body.classList.remove("body--dark");
  }
}

const currentSeason = ref({})
const currentSeasonLabel = ref("Loading...")
const fetchCurrentSeasonPromise = fetchCurrentSeason()

let fetchStreamTimer = undefined
const fetchTwitchStreams = async function () {
  console.log("Updating streams...")
  const width = 150;
  const height = Math.round(width / (16 / 9));
  try {
    let response = await axios.get(
      `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
      + `/twitch/streams`
    )
    response.data.forEach(item => {
      item.streamThumbnail = item.streamThumbnail
        .replace("{width}", String(width))
        .replace("{height}", String(height));
    });
    twitchStreams.value = response.data;
  } catch (e) {
    console.error(e)
  } finally {
    fetchStreamTimer = setTimeout(fetchTwitchStreams, 60000);
  }
}

onBeforeUnmount(() => {
  if (fetchStreamTimer) clearTimeout(fetchStreamTimer)
})

fetchTwitchStreams();

var mySection;
// AD BANNER
const setupAdBanner = () => {
    mySection = document.getElementById('glitch-image-2');
    mySection.addEventListener('animationend', function () {
      mySection.classList.remove('glitch-animate');
      var delay = Math.floor(Math.random() * 4 + 4) * 1000;
      setTimeout(startAnimation, delay);
    });
    startAnimation(); // Start the first animation
};

function randomizeVariables() {
  // Randomize each position variable
  for (let i = 1; i <= 14; i++) {
    var randomPos = Math.floor(Math.random() * 30) - 15;
    mySection.style.setProperty(`--pos-${i}`, `${randomPos}px`);
  }
}

function startAnimation() {
  randomizeVariables();
  mySection.classList.add('glitch-animate'); // Start the animation
}

onMounted(() => {
  fetchCurrentSeasonPromise.then((data) => {
    currentSeason.value = data
    if (data.idName === 'NO-SEASON') {
      currentSeasonLabel.value = 'No Active Season'
      linksListCommunitySeason.value = [
        {
          type: 'link',
          title: 'Previous Seasons',
          // caption: 'Summer Season Competition FAQ',
          icon: 'inventory_2',
          link: '/community-previous-season-rankings'
        }
      ]
    } else {
      currentSeasonLabel.value = data.name
    }

    if (data.details_v1?.format === 'QUAL_TIME_TRIAL_FINISH_TIME_TRIAL') {
      console.log(data.details_v1?.format);
      linksListCommunitySeason.value = NAVIGATION_QUAL_TIME_TRIAL_FINISH_TIME_TRIAL
    }
    if (data.details_v1?.format === 'QUAL_TIME_TRAIL_FINISH_TOURNAMENT') {
      linksListCommunitySeason.value = NAVIGATION_QUAL_TIME_TRAIL_FINISH_TOURNAMENT
    }
    if (data.details_v1?.format === 'QUAL_TIME_TRIAL_AND_TOURNAMENTS_FINISH_TOURNAMENT') {
      linksListCommunitySeason.value = NAVIGATION_QUAL_TIME_TRIAL_AND_TOURNAMENTS_FINISH_TOURNAMENT
    }
    if(data.details_v1?.grandFinalStartDate) {
      linksListCommunitySeason.value = linksListCommunitySeason.value.map(link => {
        if (link.title === 'Grand Finals') {
          link.caption = toLocalDateformat(data.details_v1?.grandFinalStartDate)
          if (data.details_v1?.hasPrizePool) {
           // sum the pizepool contained in data.details_v1.prizePool (Arraylist, content looks like "$100")
           let prizePool = 0;
           data.details_v1?.prizePool.forEach(prize => {
             prizePool += Number(prize.replace("$", ""));
           });
           // link.sideHtml += `$${prizePool}`
            let formattedPrizePool = new Intl.NumberFormat('en-US', {
              minimumFractionDigits: 0,
              maximumFractionDigits: 0
            }).format(prizePool);
           link.sideHtml = `<span data-heading="$${formattedPrizePool}" class="prize-money">$${formattedPrizePool}</span>`
          }
        }
        return link;
      });
    }
    if(data.details_v1?.matcherino?.matcherinoEventLink){
      linksListCommunitySeason.value = linksListCommunitySeason.value.map(link => {
        if (link.title === 'Register') {
          link.link = data.details_v1.matcherino?.matcherinoEventLink;
        }
        return link;
      });
    }
    if (data.details_v1?.matcherino?.promoBannerImageName) {
      nextTick(() => {
        setupAdBanner()
      });
    }
  });
})
</script>

<style lang="sass">
.q-drawer
  .q-item__section--side
    padding-right: 16px

  .q-item__section--avatar
    min-width: unset

.first-place
  background-image: url('assets/gold-medal.svg')

.second-place
  background-image: url('assets/silver-medal.svg')

.third-place
  background-image: url('assets/bronze-medal.svg')

.first-place, .second-place, .third-place
  background-position-x: 0.3rem
  background-position-y: -0.1rem
  background-repeat: no-repeat
  background-size: 1.5rem
  background-origin: border-box

.my-sticky-header-table
  /* height or max-height is important */
  //min-height: inherit
  thead th
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

tbody .td-borders-font-size16
  //color: black
  border-left: 1px solid rgb(0, 0, 0, 0.4)
  border-right: 0
  border-top: 1px solid rgb(0, 0, 0, 0.4)
  border-bottom: 0
  font-weight: normal
  font-size: 16px

.leaderboard-position-column
  max-width: 65px
  width: 65px
  font-weight: 900 !important
  color: #f6f6f6
  text-shadow: 1px 0 1px black, -1px 0 1px black, 0 1px 1px black, 0 -1px 1px black

.button-fills-whole-td
  top: 0
  left: 0
  width: 100%
  height: 100%
  position: absolute

body.body--dark .doc-card-title
  background: #475D66
  color: #cbcbcb

  &:after
    border-top-color: scale-color(#475D66, $lightness: -30%)

.doc-card-title
  margin-left: -12px
  padding: 4px 8px 4px 28px
  position: relative
  border-radius: 3px 5px 5px 0
  background: #D8E1E5
  color: #757575
  font-size: 20px
  letter-spacing: .7px

  &:after
    content: ''
    position: absolute
    top: 100%
    left: 0
    width: 0
    height: 0
    //border: 0 solid transparent
    border-width: 9px 0 0 11px
    border-top-color: scale-color(#D8E1E5, $lightness: -15%)

// AssetStudioGUI -> Filter Type = Texture2D -> Export -> "Filtered assets"
// exported maps are named like background*
// montage -resize 120 map-* -auto-orient -geometry +1+1 -background none -format png maps.png
// http://www.spritecow.com/
// then use the tool pngquant to make the source pngs smaller
.map-2017-world-championship
  object-fit: none
  object-position: -1px -1px
  width: 120px !important
  height: 68px !important

.map-adventuredome
  object-fit: none
  object-position: -123px -1px
  width: 120px !important
  height: 68px !important

.map-allianz-riviera
  object-fit: none
  object-position: -245px -1px
  width: 120px !important
  height: 68px !important

.map-atlanta-aftermath
  object-fit: none
  object-position: -367px -1px
  width: 120px !important
  height: 68px !important

.map-biosphere-2
  object-fit: none
  object-position: -489px -1px
  width: 120px !important
  height: 68px !important

.map-bmw-welt
  object-fit: none
  object-position: -611px -1px
  width: 120px !important
  height: 68px !important

.map-boston-foundry
  object-fit: none
  object-position: -1px -71px
  width: 120px !important
  height: 68px !important

.map-bridge
  object-fit: none
  object-position: -123px -71px
  width: 120px !important
  height: 68px !important

.map-california-nights
  object-fit: none
  object-position: -245px -71px
  width: 120px !important
  height: 68px !important

.map-campground
  object-fit: none
  object-position: -367px -71px
  width: 120px !important
  height: 68px !important

.map-championship-kingdom
  object-fit: none
  object-position: -367px -71px
  width: 120px !important
  height: 68px !important

.map-detroit
  object-fit: none
  object-position: -611px -71px
  width: 120px !important
  height: 68px !important

.map-drl-sandbox
  object-fit: none
  object-position: -1px -141px
  width: 120px !important
  height: 68px !important

.map-drone-park
  object-fit: none
  object-position: -123px -141px
  width: 120px !important
  height: 68px !important

.map-gates-of-new-york
  object-fit: none
  object-position: -245px -141px
  width: 120px !important
  height: 68px !important

.map-hard-rock-stadium
  object-fit: none
  object-position: -367px -141px
  width: 120px !important
  height: 68px !important

.map-l-a-pocalypse
  object-fit: none
  object-position: -367px -141px
  width: 120px !important
  height: 68px !important

.map-mardi-gras-world
  object-fit: none
  object-position: -611px -141px
  width: 120px !important
  height: 68px !important

.map-mega-city
  object-fit: none
  object-position: -1px -211px
  width: 120px !important
  height: 68px !important

.map-miami-lights
  object-fit: none
  object-position: -1px -211px
  width: 120px !important
  height: 68px !important

.map-multigp
  object-fit: none
  object-position: -245px -211px
  width: 120px !important
  height: 68px !important

.map-munich-playoffs
  object-fit: none
  object-position: -367px -211px
  width: 120px !important
  height: 68px !important

.map-ohio-crashsite
  object-fit: none
  object-position: -489px -211px
  width: 120px !important
  height: 68px !important

.map-out-of-service
  object-fit: none
  object-position: -611px -211px
  width: 120px !important
  height: 68px !important

.map-project-manhattan
  object-fit: none
  object-position: -1px -281px
  width: 120px !important
  height: 68px !important

.map-silicon-valley
  object-fit: none
  object-position: -123px -281px
  width: 120px !important
  height: 68px !important

.map-skatepark-la
  object-fit: none
  object-position: -245px -281px
  width: 120px !important
  height: 68px !important

.map-the-house
  object-fit: none
  object-position: -367px -281px
  width: 120px !important
  height: 68px !important

.map-u-s--air-force-boneyard
  object-fit: none
  object-position: -489px -281px
  width: 120px !important
  height: 68px !important

.map-u-s--air-force-night-mode
  object-fit: none
  object-position: -611px -281px
  width: 120px !important
  height: 68px !important

.table-with-brackground-image
  .q-table__top .q-table__control
    width: 100%
    position: relative
    align-items: flex-start !important

  .q-table__top
    overflow: hidden
    padding: 0

  .q-field--filled .q-field__control
    background-color: rgb(0, 0, 0, 0.7)
  // for f in map-*.png do convert "$f" -gaussian-blur 5x5 -quality 100 "${f%.*-blurred.png" done
  // pngquant --speed 1 --force --strip map-*-blurred.png
  .animated-background-image
    position: absolute
    object-fit: contain
    box-sizing: content-box
    width: 100%
    //animation: move-track-background 60s ease-in-out 2s infinite
    animation: move-track-background 90s steps(calc(25 * 90), end) 2s infinite
    transform: translateY(-3%)
    will-change: transform

@keyframes move-track-background
  0%
    transform: translateY(-3%)
  50%
    transform: translateY(calc(-97% + 92px))
  100%
    transform: translateY(-3%)

@media (min-width: 600px)
  .q-dialog__inner--minimized > div
    max-width: 700px !important

#glitch-image-1
  position: relative
  width: 100%
  aspect-ratio: 1
  background-size: 100%

#glitch-image-2
  position: absolute
  top: 0
  left: 0
  width: 100%
  aspect-ratio: 1
  background-size: 100%
  opacity: .5
  mix-blend-mode: hard-light
  //animation:glitch-animate 0.4s linear infinite
  --pos-1: 0px
  --pos-2: 0px
  --pos-3: 0px
  --pos-4: 0px
  --pos-5: 0px
  --pos-6: 0px
  --pos-7: 0
  --pos-8: 0px
  --pos-9: 0px
  --pos-10: 0px
  --pos-11: 0px
  --pos-12: 0px
  --pos-13: 0px
  --pos-14: 0px

.glitch-animate
  animation: glitch-animate 0.5s linear


@keyframes glitch-animate
  0%
    background-position: 0 0
    filter: hue-rotate(0deg)
  10%
    background-position: var(--pos-1) var(--pos-2)
  20%
    background-position: var(--pos-3) var(--pos-4)
  30%
    background-position: var(--pos-5) var(--pos-6)
  40%
    background-position: var(--pos-7) var(--pos-8)
  50%
    background-position: var(--pos-9) var(--pos-10)
  60%
    background-position: var(--pos-11) var(--pos-12)
  70%
    background-position: var(--pos-13) var(--pos-14)
  80%
    background-position: var(--pos-1) var(--pos-2)
  81%
    background-position: 0 0
  100%
    background-position: 0 0
    filter: hue-rotate(360deg)

.background-Summer_Series_2024_matcherino_register
  background: url("/assets/matcherino/Summer_Series_2024_matcherino_register.png")

.prize-money
  background: linear-gradient(90deg, #ffd700, #ffdc73, #d4af37, #ffdc73, #ffd700)
  -webkit-background-clip: text
  -webkit-text-fill-color: transparent
  color: #fff
  //text-transform: uppercase
  font-size: 1rem
  //margin: 0
  font-weight: 1000
  position: relative
</style>
