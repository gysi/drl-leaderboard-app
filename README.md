# DRL Leaderboard App
Webapp for displaying DRL Simulator leaderboards.

**Modules**
* backend (Data processing and storage)
* frontend (Webapp)
* server (Infrastructure configuration)

# TODOs
* [ ] Backend
  * [ ] Features
    * [x] ~~Map listing~~
    * [x] ~~Overall rankings~~
    * [x] ~~Per map rankings~~
    * [x] ~~Per player rankings~~
      * [x] ~~Beaten by rankings for each pbs~~
    * [x] ~~Removal/exclusion of invalid runs~~
    * [x] ~~Score calculation~~
    * [x] ~~Latest Player activity~~
    * [x] ~~Caching~~
* [ ] Server
  * [x] ~~Update server~~
  * [x] ~~Modify SSH config~~
  * [x] ~~Firewall configuration~~
  * [x] ~~Install Docker, Docker Compose~~
  * [x] ~~Nginx configuration~~
    * [x] ~~Fail to ban~~
    * [x] ~~Logrotate~~
    * [x] ~~Certbot~~
    * [x] ~~Ratelimiting~~
  * [x] ~~Certbot config~~
    * [x] ~~AWS DNS configuration~~
    * [x] ~~SSL certificates (letsencrypt)~~
  * [x] ~~Backend~~
    * [x] ~~Docker JRE Container~~
    * [x] ~~Graal VM Native Image + Container (Optional)~~
    * [x] ~~Nginx configuration~~
    * [x] ~~Postgres Volume~~
  * [x] ~~Frontend~~
    * [x] ~~Ansible build + deployment~~
    * [x] ~~Nginx configuration~~
      * [x] ~~Basic configuration~~
      * [x] ~~Caching~~
* [ ] Frontend
  * [x] ~~Initial setup~~
  * [ ] Design
    * [ ] Something better than the current one
    * [x] ~~Dark mode~~
    * [ ] Mobile friendly (Optional)
      * Zoom is disabled on mobile without using desktop mode on android
      * And desktop mode does not enable zoom on ios for safari and chrome
      * Using rem units for font sizes
    * [ ] Multi-language support (Optional)
    * [ ] Custom scrollbars
      * https://binaryify.github.io/vue-custom-scrollbar/en/#why-use-vue-custom-scrollbar (all browsers)
      * https://codepen.io/Hawkeye64/pen/bGRXBOY?editors=1100 (only webkit)
    * [ ] Customize Styling / Theme 
  * [ ] Features
    * Home
      * [x] ~~Latest Player activity~~
      * [x] ~~Most active players (last 7/30 days)~~
      * [x] ~~Link from track / map / parent category "chips" to track rankings / tracks section~~
      * [ ] ??? More stuff 
      * [x] ~~Most played tracks (7 days / month)~~
      * [x] Overlay which shows releases, upcoming features and a red info box explaining why positions don't match the ingame leaderboard because people won't read the FAQ
    * [ ] Track section
      * [x] ~~Basic track/map listing~~
      * [x] ~~Track/Map images (Optional)~~
      * [x] ~~Link to track leaderboard~~
      * [ ] Top 3 players?
      * [ ] Latest activity
      * [ ] Search/Filter
    * [ ] Overall rankings
      * [x] ~~Basic overall rankings~~
      * [x] ~~Adds a "?" icon to the invalid runs column header which links to the FAQ Section~~
      * [x] ~~Adds a "?" icon to the points column header which links to the FAQ Section~~
      * [x] ~~Avg Position~~
      * [ ] Latest Activty (track)
      * [x] ~~Ability to scroll to a player (perhaps also from an external link)~~
      * [x] ~~Additional leaderboards for parent categories~~
    * [ ] Track rankings
      * [x] ~~Basic map rankings~~
      * [x] ~~Search should also search in parent categories~~
      * [ ] Display map image to the left of the input when something is selected (+ text overlay name, map, category)
      * ??? More stuff
      * [ ] Ability to scroll to a player (perhaps also from an external link)
      * [ ] Remember last state (filter)
    * [ ] Player rankings
      * [x] ~~Basic player rankings~~
      * [x] ~~List not finished tracks~~
      * [x] ~~Link to track leaderboard~~
      * [x] ~~Sortable columns~~
      * [x] ~~More readable invalid runs onhover overlay~~
      * [x] ~~"Beaten By" integration for each PB that got beaten since the submission~~
      * [x] ~~Adds a "?" icon to the points column header which links to the FAQ Section~~
      * [ ] General statistics on the top of the page/table-header
      * [x] ~~Make the "beaten by" column also sortable.~~
      * [ ] Remember last state (filter, compare filter)
      * [ ] Encode special characters in the url (player names, e.g.: fv|impulse)
    * [x] ~~SLOTMACHINE!!!~~
      * ~~Maybe take 10 lastest improved tracks, 10 highest position, 10 of most by beaten entries etc. and then take a random one. Display it like a slotmachine.~~
      * ~~Could be a new entry within the navbar which then renders an overlay with a playersearch bar and a slotmachine~~
    * [ ] FAQ 
      * [x] ~~Point system explanation~~
      * [x] ~~Invalid runs explanation~~
      * [x] ~~"Beaten by" column explanation~~
      * [ ] My time is not on the leaderboard but I just put up a time
      * [ ] My placement doesn't match with the leadeboard
      * ??? More stuff
    * [ ] Tournaments
      * [x] First POC
      * ...
    * [ ] Replay Viewer
      * [x] First POC
      * ...
    * [ ] Stream Cards Creator
      * [x] First POC
      * [ ] Better design (CSS)
      * [ ] More options 
        * ...
      * [ ] "Track Card"

## Misc
Count lines of code:
cloc --fullpath --not-match-d="(node_modules|dist|.gradle|.idea|.yarn|.vscode|.quasar|native-image|.mvn|target)" --not-match-f="(yarn\.lock|package\-lock\.json|\.svg|\.pnp\.cjs|\.pnp\.loader\.mjs|mvnw|mvnw.cmd|\.iml)" .
