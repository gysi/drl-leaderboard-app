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
  * [ ] Nginx configuration
    * [ ] Fail to ban
    * [ ] Logrotate
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
* [ ] Frontend (Maybe Quasar framework?)
  * [ ] Nginx configuration
