<template>
  <q-page class="q-pa-md items-start" style="height: 100%">
    <q-card class="doc-api q-mb-md" flat bordered
            style="height: 100%; max-height: 100%; display: grid; grid-template-rows: auto auto auto auto 1fr;"
    >
      <!--      Header-->
      <div class="tournament-header-top header-toolbar row items-center q-pr-sm">
        <div class="doc-card-title q-my-xs q-mr-sm ">{{ seasonTitle }}</div>
        <div id="streamcard-info">
          <q-icon name="event"></q-icon>
          {{ seasonStartDate }} - {{ seasonEndDate }} UTC
        </div>
      </div>
      <div class="tournament-header-middle row q-pa-xs">
        <div v-for="tournament in seasonStatus.tournaments"
              :key="tournament.startTime"
             :class="tournament.status === 'CANCELLED' ? 'bg-red' : tournament.status === 'COMPLETED' ? 'bg-green' : 'bg-yellow'"
             style="width: 12px; height: 12px; margin: 1px"
        >
          <q-tooltip>
            <div>{{ tournament.status }}</div>
            <div>{{ toLocalDateformat(tournament.startTime) }}</div>
          </q-tooltip>
        </div>
      </div>

      <!--      Tabs-->
      <q-tabs class="tournament-header-bottom header-tabs" v-model="currentTab" active-color="brand-primary" indicator-color="brand-primary" align="left" :breakpoint="0">
        <q-tab v-for="tab in tabsList" :key="`api-tab-${tab}`" :name="tab" class="header-btn">
          <div class="row no-wrap items-center">
            <span class="q-mr-xs text-capitalize">{{ tab }}</span>
          </div>
        </q-tab>
      </q-tabs>

      <q-separator />

      <q-tab-panels v-model="currentTab" animated>
        <q-tab-panel class="q-pa-none" name="rankings">
          <q-table
            :columns="rankingsTable.columns"
            :rows="tournamentRanking.rankings"
            :loading="rankingsTable.loading.value"
            row-key="track.id"
            class="my-sticky-header-table"
            style="height: 100%;"
            :pagination="{rowsPerPage: 0}"
            hide-pagination
            bordered
            flat
          >
            <template v-slot:header-cell-pointsBest12Tournaments="props">
              <q-th :props="props">
                {{ props.col.label }}
                <q-btn type="a" icon="help" size="1.3rem"
                       fab flat padding="5px"
                       :to="{ name: 'faq', query: { card: 'tournamentPointSystem' } }"
                />
              </q-th>
            </template>
            <template v-slot:body="props">
              <q-tr :props="props">
                <q-td key="position" :props="props"
                      :style="{
                        backgroundColor: calcBackgroundColorByPosition(props.row.position)
                      }"
                      :class="[
                        props.row.position === 1 ? 'first-place' :
                        props.row.position === 2 ? 'second-place' :
                        props.row.position === 3 ? 'third-place' : '', 'leaderboard-position-column']"
                >
                  {{ props.row.position }}
                </q-td>
                <q-td key="player" :props="props">
                  <q-item class="q-item-player-region"
                  >
                    <q-item-section avatar side>
                      <q-avatar rounded size="50px">
                        <img :src="props.row.profileThumb" loading="eager" alt="Avatar"/>
                        <!--                  <q-img loading="lazy" :src="props.row.profileThumb" />-->
                      </q-avatar>
                    </q-item-section>
                    <q-item-section>
                      <q-item-label class="player-item-label">{{ props.row.commonPlayerName }}</q-item-label>
                      <q-item-label caption>
                        <span :class="`fi fi-${props.row.flagUrl}`"></span>
                        <!--                    <q-img loading="lazy" :src="props.row.flagUrl" class="player-avatar-img"/>-->
                        <q-badge
                          :class="`badge-platform q-batch-${props.row.platform}`"
                        >{{ props.row.platform }}
                        </q-badge>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-td>
                <q-td key="pointsBest12Tournaments" :props="props">
                  {{ props.row.pointsBest12Tournaments }}
                </q-td>
                <q-td key="numberOfTournamentsPlayed" :props="props">
                  {{ props.row.numberOfTournamentsPlayed }}
                </q-td>
                <q-td key="best12Positions" :props="props">
                  <q-badge v-for="(position, index) in props.row.best12Positions" :key="index" :label="position" :style="{
                           backgroundColor: calcBackgroundColorByPosition(position),
                           textShadow: '1px 0px 1px black, -1px 0px 1px black, 0px 1px 1px black, 0px -1px 1px black',
                           fontSize: '16px',
                           lineHeight: '16px',
                           fontWeight: '900'
                           }" rounded class="q-ml-xs" />
                </q-td>
                <q-td key="allPositions" :props="props">
                  <div class="flex-wrap-container">
                  <q-badge v-for="(position, index) in props.row.allPositions" :key="index" :style="{
                           backgroundColor: calcBackgroundColorByPosition(position),
                           textShadow: '1px 0px 1px black, -1px 0px 1px black, 0px 1px 1px black, 0px -1px 1px black',
                           fontSize: '10px',
                           lineHeight: '11px',
                           fontWeight: '900'
                           }" rounded class="q-ml-xs all-position-badge" />
                  </div>
                </q-td>
              </q-tr>
            </template>
          </q-table>
        </q-tab-panel>
        <q-tab-panel class="q-pa-none" name="tournaments">
          <q-table
            :columns="tournamentTable.columns"
            :rows="tournaments"
            :loading="tournamentTable.loading.value"
            row-key="guid"
            class="my-sticky-header-table"
            table-class="col-auto"
            style="max-height: 100%;"
            :pagination="{rowsPerPage: 0}"
            hide-pagination
            bordered
            flat
            grid
          >
            <template v-slot:item="props">
              <div class="q-table__grid-item col-xs-12 col-sm-6 col-md-4 col-lg-3 col-xl-2">
                <q-card class="bg-black">
                <q-img :src="buildImgCacheUrl(props.row.imgUrl)" height="450px" fit="cover" style="height: 450px">
                </q-img>
                <div class="q-img__content absolute-full q-anchor--skip">
                  <q-card-section class="absolute-top" style="background: rgba(0,0,0,70%)">
                    <div class="" style="font-size: 20px">{{props.row.title}}</div>
                    <div class="text-subtitle2 text-grey-5">{{ toLocalDateformat(props.row.startDate) }}</div>
                    <q-chip v-if="props.row.trackName" dense class="track-chip">{{ props.row.trackName }}</q-chip>
                    <q-separator v-if="props.row.top10?.length !== 0" class="bg-grey-6 q-mb-xs" />
                    <div v-for="(player, i) in props.row.top10" v-bind:key="player">
                      {{i+1}}. {{player.profileName}}
                      {{ i === 0 ? '🏆' : '' }}
                      {{ player.goldenPos > 0 ? `🌟 (${formatMilliSeconds(player.score)}${player.score < props.row.top10[0].score ? '⚠️' : ''})` : '' }}
                    </div>
                  </q-card-section>
                  <q-card-section v-if="props.row.status === 'idle'" class="absolute-bottom" style="background: rgba(255,0,0,80%)">
                    <div style="text-align: center; font-weight: 900; font-size: 16px">Starts in</div>
                    <div style="text-align: center; font-weight: 900; font-size: 16px">{{getDateDifferenceToNow(props.row.startDate)}}</div>
                  </q-card-section>
                  <q-card-section v-if="props.row.status === 'active'" class="absolute-bottom" style="background: rgba(197,197,63,0.95)">
                    <div style="text-align: center; font-weight: 900; font-size: 16px">RUNNING</div>
                  </q-card-section>
                  <q-card-section v-if="props.row.status === 'complete'" class="absolute-bottom" style="background: rgba(64,169,60,0.8)">
                    <div style="text-align: center; font-weight: 900; font-size: 16px">COMPLETED</div>
                  </q-card-section>
                </div>
                </q-card>

              </div>
            </template>
          </q-table>
        </q-tab-panel>
      </q-tab-panels>
    </q-card>
  </q-page>
</template>

<script setup>
import axios from 'axios';
import {computed, ref, shallowRef, watch} from "vue";
import { format, parseISO, formatDuration, intervalToDuration } from 'date-fns'
import { utcToZonedTime } from "date-fns-tz";
import {backGroundColorByPosition, formatMilliSeconds} from 'src/modules/LeaderboardFunctions'
import placeholder from 'src/assets/placeholder.png'
import {useMeta} from "src/modules/meta.js"

useMeta({
  title: "Tournaments",
  meta: {
    description: {
      name: 'description',
      content: `Explore current tournament rankings, upcoming events, and past results all in one place.`
    }
  }
})

const calcBackgroundColorByPosition = backGroundColorByPosition;
const tournamentRanking = shallowRef({
  rankings: [],
});

const seasonTitle = computed(() => {
  return tournamentRanking.value.seasonName == null ? 'Tournaments Rankings' : `Tournament Rankings - ${tournamentRanking.value.seasonName}`;
})

const seasonStartDate = computed(() => {
  if (tournamentRanking.value.seasonStartDate == null) return;
  const date = parseISO(tournamentRanking.value.seasonStartDate)
  return format(date, 'MMM do, yyyy');
})

const seasonEndDate = computed(() => {
  if (tournamentRanking.value.seasonEndDate == null) return;
  const date = parseISO(tournamentRanking.value.seasonEndDate)
  return format(date, 'MMM do, yyyy');
})

const currentTab = ref("rankings")
// watch currentTab
watch(currentTab, (val) => {
  if (val === "tournaments") {
    fetchTournaments();
  }
})

const tabsList = ["rankings", "tournaments"]
const rankingsTable = {
  columns: [
    { name: 'position', label: '#', field: 'position', align: 'right' },
    { name: 'player', label: 'Player', field: 'commonPlayerName', align: 'left' },
    { name: 'pointsBest12Tournaments', label: 'Points', field: 'pointsBest12Tournaments', align: 'center' },
    { name: 'numberOfTournamentsPlayed', label: 'Tournaments played', field: 'numberOfTournamentsPlayed', align: 'center' },
    { name: 'best12Positions', label: 'Best 12 positions', field: 'best12Positions', align: 'left',
      format: (val, row) => {
        return val.join(', ');
      }
    },
    { name: 'allPositions', label: 'All Positions', field: 'allPositions', align: 'left',
      format: (val, row) => {
        return val.join(', ');
      }
    },
  ],
  loading: ref(false),
}

const fetchTournamentRankings = async () => {
  rankingsTable.loading.value = true;
  const response = await axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
    + `/tournaments/rankings-current-season`
  )
  if(response.data.rankings){
    response.data.rankings = response.data.rankings.map((row) => {
      if (row['profileThumb'].includes('placeholder.png')) {
        row['profileThumb'] = placeholder;
      }else{
        row['profileThumb'] = buildImgCacheUrlForThumbs(row['profileThumb']);
      }
      return row;
    });
  }
  tournamentRanking.value = response.data;
  rankingsTable.loading.value = false;
}

const tournaments = shallowRef([]);

const tournamentTable = {
  columns: [
    // { name: 'guid', label: 'GUID', field: 'guid' },
    { name: 'title', label: 'Title', field: 'title' },
    { name: 'top10', label: 'Top 10', field: 'top10' },
    { name: 'startDate', label: 'Start Date', field: 'startDate' },
    { name: 'imgUrl', label:'Img Url', field: 'imgUrl' }
  ],
  loading: ref(false),
}

const seasonStatus = shallowRef({});

const toLocalDateformat = (val) => {
  if (val) {
    const date = new Date(val+'Z')
    const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone
    const zonedDate = utcToZonedTime(date, userTimezone);
    const pattern = 'yyyy-MM-dd HH:mm'
    return format(zonedDate, pattern, { timeZone: userTimezone }) + ' ' + userTimezone
  }
};

const buildImgCacheUrl = (url) => {
  if (url) {
    let encodedUrl = encodeURIComponent(url);
    return computed(() =>
      `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_THUMBOR_PORT}${process.env.DLAPP_THUMBOR_PATH}`
      + `/x500/${encodedUrl}`
    ).value
  }
}

const buildImgCacheUrlForThumbs = (url) => {
  if (url) {
    if(url.includes('placeholder.png')) return url;
    let encodedUrl = encodeURIComponent(url);
    return computed(() =>
      `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_THUMBOR_PORT}${process.env.DLAPP_THUMBOR_PATH}`
      + `/50x50/${encodedUrl}`
    ).value;
  }
}

const getDateDifferenceToNow = (dateString) => {
    if(!dateString) return '';
    let duration = intervalToDuration({
      start: new Date(),
      end: new Date(dateString+'Z')
    });
    let units = [];
    if(duration.months > 0 ){
      units.push('months');
    }
    if(duration.days > 0 ){
      units.push('days');
    } else {
      if (duration.hours > 0) {
        units.push('hours');
      }
      if (duration.minutes > 0) {
        units.push('minutes');
      }
      units.push('seconds');
    }
    return formatDuration(duration, { format: units });
}

const fetchTournaments = async () => {
  tournamentTable.loading.value = true;
  const response = await axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
    + `/tournaments/tournaments-current-season`
  )
  tournaments.value = response.data;
  // console.log(tournaments.value);
  tournamentTable.loading.value = false;
}

const fetchSeasonStatus = async () => {
  const response = await axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}`
    + `/tournaments/season-status`
  )
  seasonStatus.value = response.data;
}

fetchSeasonStatus();
fetchTournamentRankings();

</script>

<style lang="sass">

</style>
<style lang="sass" scoped>
tbody td
  //color: black
  border-left: 1px solid rgb(0, 0, 0, 0.4)
  border-right: 0
  border-top: 1px solid rgb(0, 0, 0, 0.4)
  border-bottom: 0
  font-weight: normal
  font-size: 16px

tbody .q-item
  padding: 0
  padding-right: 10px

.player-item-label
  font-size: 20px

.player-avatar-img
  width: 25px
  height: 13px

.q-item-player-region
  background: rgba(0, 0, 0, 0.05)
  min-width: 200px

.fi
  height: 14px
  width: 19px

.badge-platform
  margin-left: 5px
  font-size: 10px
  line-height: 10px
  padding-right: 2px
  padding-left: 2px

.q-batch-Steam
  background-color: rgb(25, 91, 127)

.q-batch-Epic
  background-color: black

.q-batch-Playstation
  background-color: rgb(0, 65, 151)

.q-batch-Xbox
  background-color: rgb(16, 120, 15)

.flex-wrap-container
  display: flex
  flex-wrap: wrap
  /* Adjust the max-width to control when the items should wrap */
  max-width: calc(10 * (17.8px + 4px)) /* This will make the container take full width of its parent, adjust as needed */

.all-position-badge
  flex-basis: calc(10% - 10px)
  margin: 2px

#streamcard-info
  font-size: 18px
  margin: 0px 0px 0px 8px
  p
    margin: 0 0 2px

.q-tab--active
  background-color: rgb(255,255,255,0.1)

.doc-api
  &__container
    max-height: 600px

  &__nothing-to-show
    padding: 16px


:deep(.q-img__content)
  .q-chip
    margin-left: 0
    margin-right: 0
  .q-chip__content
    display: block
    overflow: hidden
    text-overflow: ellipsis
    white-space: nowrap
</style>
