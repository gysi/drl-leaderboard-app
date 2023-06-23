<template>
  <q-page class="q-pa-md items-start" style="height: 100%">
    <q-card class="doc-api q-mb-md" flat bordered style="height: 100%; max-height: 100%; display: grid; grid-template-rows: auto auto auto 1fr;">
      <!--      Header-->
      <div class="header-toolbar row items-center q-pr-sm">
        <div class="doc-card-title q-my-xs q-mr-sm ">{{ seasonTitle }}</div>
        <div id="streamcard-info">
          <p>Started on {{ seasonStartDate }} UTC and will end at {{ seasonEndDate }} UTC</p>
          <p>The tournament points are based on the DRL irl point system and only the top 12 tournaments for each player are counted.</p>
        </div>
      </div>

      <!--      Tabs-->
      <q-tabs class="header-tabs" v-model="currentTab" active-color="brand-primary" indicator-color="brand-primary" align="left" :breakpoint="0">
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
                <q-img :src="buildImgCacheUrl(props.row.imgUrl)" height="450px" fit="cover">
                    <q-card-section class="absolute-top" style="background: rgba(0,0,0,70%)">
                        <div class="" style="font-size: 20px">{{props.row.title}}</div>
                        <div class="text-subtitle2 text-grey-5">{{ toLocalDateformat(props.row.startDate) }}</div>
                        <q-separator v-if="props.row.top3.length !== 0" class="bg-grey-6" />
                        <div v-for="(player, i) in props.row.top3" v-bind:key="player">{{i+1}}. {{player}}</div>
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
                </q-img>
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
import {format, parseISO, formatDuration, intervalToDuration} from 'date-fns'
import {utcToZonedTime} from "date-fns-tz";
import {getDateDifference} from 'src/modules/LeaderboardFunctions'

const tournamentRanking = shallowRef([]);

const seasonTitle = computed(() => {
  return tournamentRanking.value.seasonName == null ? 'Tournaments Rankings' : `Tournament Rankings - ${tournamentRanking.value.seasonName}`;
})

const seasonStartDate = computed(() => {
  if (tournamentRanking.value.seasonStartDate == null) return;
  const date = parseISO(tournamentRanking.value.seasonStartDate)
  return format(date, 'yyyy-MM-dd');
})

const seasonEndDate = computed(() => {
  if (tournamentRanking.value.seasonEndDate == null) return;
  const date = parseISO(tournamentRanking.value.seasonEndDate)
  return format(date, 'yyyy-MM-dd');
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
    { name: 'position', label: '#', field: 'position', align: 'left' },
    { name: 'player', label: 'Player', field: 'commonPlayerName', align: 'left' },
    { name: 'pointsBest12Tournaments', label: 'Points', field: 'pointsBest12Tournaments', align: 'right' },
    { name: 'numberOfTournamentsPlayed', label: 'Tournaments played', 'field': 'numberOfTournamentsPlayed', align: 'right' },
    { name: 'best12Positions', label: 'Best 12 positions', 'field': 'best12Positions', align: 'center',
      format: (val, row) => {
        if (val) return val.join(', ');
      }
    }
  ],
  loading: ref(false),
}

const fetchTournamentRankings = async () => {
  rankingsTable.loading.value = true;
  const response = await axios.get(`${process.env.DLAPP_API_URL}/tournaments/rankings-current-season`);
  tournamentRanking.value = response.data;
  rankingsTable.loading.value = false;
}

const tournaments = shallowRef([]);

const tournamentTable = {
  columns: [
    // { name: 'guid', label: 'GUID', field: 'guid' },
    { name: 'title', label: 'Title', field: 'title' },
    { name: 'top3', label: 'Top 3', field: 'top3' },
    { name: 'startDate', label: 'Start Date', field: 'startDate' },
    { name: 'imgUrl', label:'Img Url', field: 'imgUrl' }
  ],
  loading: ref(false),
}

const toLocalDateformat = (val) => {
  if (val) {
    const date = new Date(val+'Z')
    const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone
    const zonedDate = utcToZonedTime(date, userTimezone);
    const pattern = 'yyyy-MM-dd HH:mm:ss'
    return format(zonedDate, pattern, { timeZone: userTimezone }) + ' ' + userTimezone
  }
};

const buildImgCacheUrl = (url) => {
  if (url) {
    let encodedUrl = encodeURIComponent(url);
    return computed(() => `${process.env.DLAPP_THUMBOR_URL}/x500/${encodedUrl}`).value;
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
  const response = await axios.get(`${process.env.DLAPP_API_URL}/tournaments/tournaments-current-season`);
  tournaments.value = response.data;
  console.log(tournaments.value);
  tournamentTable.loading.value = false;
}

fetchTournamentRankings();

</script>

<style lang="sass" scoped>
#streamcard-info
  font-size: 18px
  margin: 0px 0px 0px 8px
  p
    margin: 0 0 2px

///// DOC CARD /////
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
    border: 0 solid transparent
    border-width: 9px 0 0 11px
    border-top-color: scale-color(#D8E1E5, $lightness: -15%)

body.body--dark .doc-card-title
  background: #475D66
  color: #cbcbcb
  &:after
    border-top-color: scale-color(#475D66, $lightness: -30%)

.doc-api
  &__subtabs .q-tabs__content
    padding: 8px 0

  &__subtabs-item
    justify-content: left
    min-height: 36px !important
    .q-tab__content
      width: 100%

  &__subtabs,
  &__subtabs-item
    border-radius: 0 !important

  &__container
    max-height: 600px

  &__nothing-to-show
    padding: 16px

  &__search-field
    cursor: text
    min-width: 10em !important

  &__search
    border: 0
    outline: 0
    background: none
    color: inherit
    width: 1px !important // required when on narrow width window to not overflow the page
    height: 37px

.doc-api-entry
  padding: 16px
  color: #757575

  .doc-api-entry
    padding: 8px

  & + &
    border-top: 1px solid #ddd

  &__expand-btn
    margin-left: 4px

  &__item
    min-height: 25px
    & + &
      margin-top: 4px

  &__subitem
    padding: 4px 0 0 8px
    border-radius: 4px
    > div
      border: 1px solid rgba(0,0,0,.12) !important
      border-radius: inherit
    > div + div
      margin-top: 8px

  &__type
    line-height: 24px

  &__value
    color: #474747

  &--indent
    padding-left: 8px

  .doc-token
    margin: 4px
    display: inline-block

  &__added-in,
  &__pill
    font-size: 15px
    letter-spacing: .7px
    line-height: 1.4em

  &__added-in
    font-size: 12px

body.body--light
  .doc-api .doc-token
    background-color: #eee
    border: 1px solid rgba(0, 0, 0, .12)
    color: #474747
  .doc-api-entry__pill
    color: #fff
  .doc-api-entry__added-in
    color: $red-7
    border-color: $red
    background-color: $red-1

body.body--dark
  .doc-api .doc-token
    background-color: #050A14
    border: 1px solid rgba(255, 255, 255, .28)
    color: #cbcbcb
  .doc-api__search
    color: #cbcbcb
  .doc-api-entry
    color: #8FA8B2
    & + .doc-api-entry,
    &__subitem > div
      border-color: rgba(255, 255, 255, .28) !important
    &__value
      color: #cbcbcb
    &__example
      color: #00B4FF
      border-color: #00B4FF
    &__pill
      color: $dark
    &__added-in
      color: $red
      border-color: $red
      background-color: #050A14
</style>
