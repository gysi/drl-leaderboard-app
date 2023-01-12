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
    * [ ] Dark mode
    * [ ] Mobile friendly (Optional)
      * Zoom is disabled on mobile without using desktop mode on android
      * And desktop mode does not enable zoom on ios for safari and chrome
      * Using rem units for font sizes
    * [ ] Multi-language support (Optional)
    * [ ] Custom scrollbars
      * https://binaryify.github.io/vue-custom-scrollbar/en/#why-use-vue-custom-scrollbar (all browsers)
      * https://codepen.io/Hawkeye64/pen/bGRXBOY?editors=1100 (only webkit)
  * [ ] Features
    * Home
      * [x] ~~Latest Player activity~~
      * [x] ~~Most active players (last 7/30 days)~~
      * [ ] Link from track / map / parent category "chips" to track rankings / tracks section
      * [ ] ??? More stuff 
    * [ ] Track section
      * [x] ~~Basic track/map listing~~
      * [ ] Track/Map images (Optional) 
      * [ ] Link to track leaderboard
      * [ ] Top 3 players?
      * [ ] Search/Filter
    * [ ] Overall rankings
      * [x] ~~Basic overall rankings~~
      * [ ] Adds a "?" icon to the invalid runs column header which links to the FAQ Section
      * [ ] Adds a "?" icon to the points column header which links to the FAQ Section
    * [ ] Track rankings
      * [x] ~~Basic map rankings~~
      * [ ] Search should also search in parent categories
    * [ ] Player rankings
      * [x] ~~Basic player rankings~~
      * [ ] List not finished tracks #**HIGH PRIO**
      * [ ] Link to track leaderboard #**HIGH PRIO**
      * [ ] Sortable columns
      * [ ] More readable invalid runs onhover overlay #**HIGH PRIO**
      * [ ] "Beaten By" integration for each PB that got beaten since the submission #**HIGH PRIO**
      * [ ] Adds a "?" icon to the points column header which links to the FAQ Section
      * [ ] General statistics on the top of the page/table-header
      * [ ] Sortable columns
    * [ ] FAQ 
      * [x] ~~Point system explanation~~
      * [ ] Invalid runs explanation #**HIGH PRIO**

