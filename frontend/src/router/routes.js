import MainLayout from "layouts/MainLayout.vue";
import IndexPage from "pages/IndexPage.vue";

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { name: 'home', path: '', component: IndexPage },
      { name: 'overallrankings', path: 'overall-rankings', component: () => import('pages/OverallPage.vue') },
      { name: 'tournaments', path: 'tournaments', component: () => import('pages/TournamentsPage.vue') },
      { name: 'tracks', path: 'tracks', component: () => import('pages/TracksPage.vue') },
      { name: 'tracklb', path: 'track-lb', component: () => import('pages/TrackLbPage.vue'), props: route => ({ q: route.query.q }) },
      { name: 'playerlb', path: 'player-lb', component: () => import('pages/PlayerLbPage.vue'), props: route => ({ q: route.query.q }) },
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
