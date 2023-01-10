
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { name: 'home', path: '', component: () => import('pages/IndexPage.vue') },
      { name: 'overallrankings', path: 'overallrankings', component: () => import('pages/OverallPage.vue') },
      { name: 'tracks', path: 'tracks', component: () => import('pages/TracksPage.vue') },
      { name: 'tracklb', path: 'tracklb', component: () => import('pages/TrackLbPage.vue'), props: route => ({ q: route.query.q }) },
      { name: 'playerlb', path: 'playerlb', component: () => import('pages/PlayerLbPage.vue'), props: route => ({ q: route.query.q }) },
      { name: 'faq', path: 'faq', component: () => import('pages/FAQPage.vue') }
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
