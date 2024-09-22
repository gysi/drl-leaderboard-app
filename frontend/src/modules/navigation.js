const navigation = {
  'QUAL_TIME_TRIAL_FINISH_TIME_TRIAL': [
    {
      type: 'link',
      title: 'Tracks',
      // caption: 'Community season tracks',
      icon: 'route',
      link: '/tracks-community'
    },
    {
      type: 'link',
      title: 'Rankings',
      // caption: 'Player rankings for season tracks',
      icon: 'leaderboard', // sports_score as alternative
      link: '/community-rankings'
    },
    {
      type: 'link',
      title: 'Player Leaderboard',
      // caption: 'Leaderboard for a specific player',
      icon: 'sports_martial_arts',
      link: '/player-leaderboard-community'
    },
    {
      type: 'link',
      title: 'FAQ',
      // caption: 'Summer Season Competition FAQ',
      icon: 'help',
      link: '/community-season-competition-faq'
    },
    {
      type: 'link',
      title: 'Previous Seasons',
      // caption: 'Summer Season Competition FAQ',
      icon: 'inventory_2',
      link: '/community-previous-season-rankings'
    }
  ],
  /////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////
  'QUAL_TIME_TRAIL_FINISH_TOURNAMENT': [
    {
      type: 'link',
      title: 'Tracks',
      // caption: 'Community season tracks',
      icon: 'route',
      link: '/tracks-community'
    },
    {
      type: 'expansion-item',
      title: 'Qualification',
      defaultOpened: true,
      contentInsetLevel: 0.25,
      children: [
        {
          type: 'link',
          title: 'Time Trial',
          // caption: 'Player rankings for season tracks',
          icon: 'leaderboard', // sports_score as alternative
          link: '/community-rankings'
        }
      ]
    },
    {
      type: 'link',
      title: 'Final Tournament',
      // caption: 'Summer Season Competition FAQ',
      icon: 'sports_score',
      link: '/community-season-competition-faq'
    },
    {
      type: 'link',
      title: 'Register',
      icon: 'how_to_reg',
      link: '',
      external: true,
      openInNew: true
    },
    {
      type: 'link',
      title: 'FAQ',
      // caption: 'Summer Season Competition FAQ',
      icon: 'help',
      link: '/community-season-competition-faq'
    },
    {
      type: 'link',
      title: 'Previous Seasons',
      icon: 'inventory_2',
      link: '/community-previous-season-rankings'
    }
  ],
  /////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////
  'QUAL_TIME_TRIAL_AND_TOURNAMENTS_FINISH_TOURNAMENT': [
    {
      type: 'link',
      title: 'Tracks',
      // caption: 'Community season tracks',
      icon: 'route',
      link: '/tracks-community'
    },
    {
      type: 'expansion-item',
      title: 'Qualification options',
      defaultOpened: true,
      contentInsetLevel: 0.25,
      children: [
        {
          type: 'link',
          title: '1. Time Trial',
          // caption: 'Player rankings for season tracks',
          icon: 'leaderboard', // sports_score as alternative
          link: '/community-rankings'
        },
        {
          type: 'link',
          title: '2. Tournaments',
          // caption: 'Tourney Rankings and upcoming dates',
          icon: 'leaderboard', // sports_score as alternative
          link: '/community-rankings'
        },
      ]
    },
    {
      type: 'link',
      title: 'Final Tournament',
      // caption: 'Summer Season Competition FAQ',
      icon: 'sports_score',
      link: '/community-season-competition-faq'
    },
    {
      type: 'link',
      title: 'FAQ',
      // caption: 'Summer Season Competition FAQ',
      icon: 'help',
      link: '/community-season-competition-faq'
    },
    {
      type: 'link',
      title: 'Previous Seasons',
      // caption: 'Summer Season Competition FAQ',
      icon: 'inventory_2',
      link: '/community-previous-season-rankings'
    }
  ],
}


export const NAVIGATION_QUAL_TIME_TRIAL_FINISH_TIME_TRIAL = navigation['QUAL_TIME_TRIAL_FINISH_TIME_TRIAL'];
export const NAVIGATION_QUAL_TIME_TRAIL_FINISH_TOURNAMENT = navigation['QUAL_TIME_TRAIL_FINISH_TOURNAMENT'];
export const NAVIGATION_QUAL_TIME_TRIAL_AND_TOURNAMENTS_FINISH_TOURNAMENT = navigation['QUAL_TIME_TRIAL_AND_TOURNAMENTS_FINISH_TOURNAMENT'];
