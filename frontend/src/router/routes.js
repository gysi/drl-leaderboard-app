
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { name: 'home', path: '', component: () => import('pages/IndexPage.vue') },
      { name: 'overallrankings', path: 'overall-rankings', component: () => import('pages/OverallPage.vue') },
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
