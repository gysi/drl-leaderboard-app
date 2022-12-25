# DRL Leaderboard Server
This module contains the server that runs the DRL Leaderboard backend and frontend. It is a docker container that can be run with different commands.
* It uses terraform to create (and delete) a server on hetzner.cloud
* It uses Ansible to provision the server
  * Keeping it up to date
  * Configures SSH, SSL (certbot, letsencrypt), Nginx
  * Deploys the frontend & backend.
  
## Setup

### Config
- Copy .dockerenv-example to .dockerenv and fill in the ssh password
- Copy terraform/config.tfvars.example and fill in the hetznercloudtoken

### Build Dockerimage
```bash
docker build \
  --tag drl-leaderboard-app-pipeline:v1 $(./build_docker_args.sh) \
  -f Dockerfile ..
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

Updates distro on the test server:
```bash
./docker_run.sh update-testserver
```
This should be done after creating a new server. Make a snapshot of the test server before doing this after you deployed the app so that when this fails you can revert to the snapshot and retest.

Deploys the leaderboard app to test server:
```bash
./docker_run.sh deploy-app-testserver
```

### Production server
Updates distro on the test server:
```bash
./docker_run.sh update-prodserver
```
Deploys the leaderboard app to main server:
```bash
./docker_run.sh deploy-app-prodserver
```
