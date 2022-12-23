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
docker build --tag drl-leaderboard-app-pipeline:v1 $(./build_docker_args.sh) .
```

## Docker Commands
Here you can manually run stuff:
```bash
docker run --rm -it -v $(pwd)/docker_state:/docker_state drl-leaderboard-app-pipeline:v1 bash
```
Applies terraform state:
```bash
docker run --rm -v $(pwd)/docker_state:/docker_state drl-leaderboard-app-pipeline:v1 create-server
```
Destroys terraform state:
```bash
docker run --rm -v $(pwd)/docker_state:/docker_state drl-leaderboard-app-pipeline:v1 destroy-server
```
Deploys the leaderboard app:
```bash
docker run --rm -v $(pwd)/docker_state:/docker_state drl-leaderboard-app-pipeline:v1 deploy-app
```
<br/>

Example: Apply Terraformstate and deploy app (Command chaining)
```bash
docker run --rm drl-leaderboard-app-pipeline:v1 create-server deploy-app
```
