# DRL Leaderboard App
Webapp for displaying DRL Simulator leaderboards.

**Modules**
* backend (Data processing and storage)
* frontend (Webapp)
* server (Infrastructure configuration)

# TODOs
* [ ] Server
  * [x] ~~Update server~~
  * [x] ~~Modify SSH config~~
  * [x] ~~Check ports~~
  * [x] ~~Install Docker, Docker Compose~~
  * [x] Nginx configuration
    * [x] ~~Fail to ban~~
    * [x] ~~Logrotate~~
    * [x] ~~certbot~~
    * [x] ~~ratelimiting~~
    * [ ] caching (frontend, maybe backend?)
  * [x] ~~Certbot config~~
    * [x] ~~AWS DNS~~
    * [x] ~~SSL certificate (letsencrypt)~~
  * [x] Backend
    * [x] ~~Docker JRE Container~~ 
    * [x] ~~Nginx configuration~~
    * [x] ~~Postgres Volume~~
* [ ] Frontend
  * [ ] Ansible build deployment 
  * [ ] Nginx configuration
  * [ ] Design
    * [ ] Something better than the current one
    * [ ] Light/Dark mode
  * [ ] Features
    * [ ] Overall rankings
    * [ ] Per map rankings
    * [ ] Per Player rankings for all maps
    * [ ]...
