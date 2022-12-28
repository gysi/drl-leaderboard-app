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
    * [ ] Latest Player activity
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
    * [ ] Caching (Optional)
  * [x] ~~Frontend~~
    * [x] ~~Ansible build + deployment~~
    * [x] ~~Nginx configuration~~
      * [x] ~~Basic configuration~~
      * [x] ~~Caching~~
* [ ] Frontend
  * [x] ~~Initial setup~~
  * [ ] Design
    * [ ] Something better than the current one
    * [ ] Light/Dark mode
    * [ ] Mobile friendly (Optional)
    * [ ] Multi-language support (Optional)
  * [ ] Features
    * [ ] Map listing
      * [ ] Map images (Optional) 
    * [ ] Overall rankings
    * [ ] Per map rankings
    * [ ] Per Player rankings for all maps
    * [ ] Latest Player activity
