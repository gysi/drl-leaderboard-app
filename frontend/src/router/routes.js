import MainLayout from "layouts/MainLayout.vue";
import IndexPage from "pages/IndexPage.vue";

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { name: 'home', path: '', component: IndexPage },
      { name: 'news', path: 'news', component: () => import('pages/NewsPage.vue')},
      { name: 'overallrankings', path: 'overall-rankings', component: () => import('pages/OverallPage.vue') },
      { name: 'community-rankings', path: 'community-rankings', component: () => import('pages/CommunityRankingPage.vue') },
      { name: 'community-previous-season-rankings', path: 'community-previous-season-rankings', component: () => import('pages/CommunityPreviousSeasonRankingPage.vue') },
      { name: 'grand-finals', path: 'grand-finals', component: () => import('pages/GrandFinalsPage.vue') },
      { name: 'grand-finals-guide-rules', path: 'grand-finals-guide-rules', component: () => import('pages/GrandFinalsGuidePage.vue') },
      { name: 'tournaments', path: 'tournaments', component: () => import('pages/TournamentsPage.vue') },
      { name: 'tracks', path: 'tracks', component: () => import('pages/TracksPage.vue') },
      { name: 'tracks-community', path: 'tracks-community', component: () => import('pages/TracksCommunityPage.vue') },
      { name: 'tracklb', path: 'track-leaderboard', component: () => import('pages/TrackLbPage.vue'), props: route => ({ q: route.query.q }) },
      { name: 'playerlb', path: 'player-leaderboard', component: () => import('pages/PlayerLbPage.vue'), props: route => ({ q: route.query.q }) },
      { name: 'playerlb-community', path: 'player-leaderboard-community', component: () => import('pages/PlayerLbCommunityPage.vue'), props: route => ({ q: route.query.q }) },
      { name: 'communitySeasonCompetitionFaq', path: 'community-season-competition-faq', component: () => import('pages/FAQCommunitySeasonCompetitionPage_2024_Fall.vue') },
      { name: 'faq', path: 'faq', component: () => import('pages/FAQPage.vue') },
      { name: 'worstTracksPicker', path: 'worst-tracks-picker', component: () => import('pages/WorstTracksPickerPage.vue') },
      { name: 'replayviewer', path: 'replay-viewer', component: () => import('pages/ReplayPage.vue') },
      { name: 'streamcardcreator', path: 'stream-card-creator', component: () => import('pages/StreamCardCreatorPage.vue') },
    ]
  },
  {
    path: '/stream-card',
    component: () => import('pages/StreamCardPage.vue'),
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes;
