# DRL Leaderboard Server
This module contains the server that runs the DRL Leaderboard backend and frontend. It is a docker container that can be run with different commands.
* It uses terraform to create (and delete) a server on hetzner.cloud
* It uses Ansible to provision the server
  * Keeping it up to date
  * Configures SSH, SSL (certbot, letsencrypt), Nginx
  * Deploys the frontend & backend.

**Tech/Framework/Libs:**
* Docker
* Docker Compose
* Terraform
* Ansible
* Bash

## Setup

### Config
- Create a SSH key (read the Reamde in .ssh).
- Copy .dockerenv-example to .dockerenv and fill in the ssh password (for the key you just created).
- Copy terraform/config.tfvars.example and fill in the hetznercloudtoken

### Build Dockerimage
```bash
./docker_run.sh build
```

## Docker Commands
Here you can manually run stuff:
```bash
./docker_run.sh bash
```
### Infrastructure
Applies terraform state:
```bash
./docker_run.sh create-server
```
Destroys terraform state:
```bash
./docker_run.sh destroy-server
```

### Test server
Updates distro:
```bash
./docker_run.sh update-testserver
```
This should be done after creating a new server. Make a snapshot of the server before doing this after you deployed the app so that when this fails you can revert to the snapshot and retest.

Deploys the leaderboard app:
```bash
./docker_run.sh deploy-app-testserver
```
Or use the quick variant to just deploy the app and nothing else
```bash
./docker_run.sh deploy-app-testserver-quick
```
### Production server
Updates distro:
```bash
./docker_run.sh update-prodserver
```
This should be done after creating a new server. Make a snapshot of the server before doing this after you deployed the app so that when this fails you can revert to the snapshot and retest.  

Deploys the leaderboard app:
```bash
./docker_run.sh deploy-app-prodserver
```
Or use the quick variant to just deploy the app and nothing else
```bash
./docker_run.sh deploy-app-prodserver-quick
```

## Misc

### Connect to Postgres on the server
Just port forward a local port to the postgres port on the server:
```bash
ssh -N -L 5433:localhost:5432 root@test.drl-leaderboards.com
ssh -N -L 5434:localhost:5432 root@drl-leaderboards.com
```
Then just connect to localhost:5433/localhost:5434 with your favorite postgres client.
### Logs
Nginx: /var/log/nginx/*  
Backend: /var/log/backend/*

### JAIL2BAN
List Jails:
```bash
fail2ban-client status
```

List banned IPs:
```bash
fail2ban-client status [jailname]
```

### GoAccess
Analyze logs:
```bash
goaccess https-drl-leaderboards.com.access.log* --log-format='%h - %^ [%d:%t %^] "%r" %s %b "%R" "%u" "%^"' --date-format='%d/%b/%Y' --time-format='%H:%M:%S'
```
