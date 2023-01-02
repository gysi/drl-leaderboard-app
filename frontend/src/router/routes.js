
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/IndexPage.vue') },
      { path: 'overallrankings', component: () => import('pages/OverallPage.vue') },
      { path: 'tracks', component: () => import('pages/TracksPage.vue') },
      { path: 'tracklb', component: () => import('pages/TrackLbPage.vue') },
      { path: 'playerlb', component: () => import('pages/PlayerLbPage.vue') }
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
