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
        * Most played tracks (7 days / month)
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
      * [ ] Avg Position
    * [ ] Track rankings
      * [x] ~~Basic map rankings~~
      * [x] ~~Search should also search in parent categories~~
      * ??? More stuff
    * [ ] Player rankings
      * [x] ~~Basic player rankings~~
      * [x] ~~List not finished tracks~~
      * [x] ~~Link to track leaderboard~~
      * [x] ~~Sortable columns~~
      * [ ] More readable invalid runs onhover overlay #**HIGH PRIO**
      * [x] ~~"Beaten By" integration for each PB that got beaten since the submission~~
      * [ ] Adds a "?" icon to the points column header which links to the FAQ Section
      * [ ] General statistics on the top of the page/table-header
      * [ ] Make the "beaten by" column also sortable. ascend = newest beaten by entry, descend = oldest beaten by entry or none
    * [ ] SLOTMACHINE!!! #**HIGH PRIO**
      * Maybe take 10 lastest improved tracks, 10 highest position, 10 of most by beaten entries etc. and then take a random one. Display it like a slotmachine.
      * Could be a new entry within the navbar which then renders an overlay with a playersearch bar and a slotmachine
    * [ ] FAQ 
      * [x] ~~Point system explanation~~
      * [x] ~~Invalid runs explanation~~
      * ??? More stuff
    * [ ] A good way to remove invalid times (wrong drone bug) which aren't exceeding the topspeed. Maybe a kinda voting system? Or something like a player analyis (median / comparision to other track runs / count of total tracks / comparision to overall rankin player position, etc..)

