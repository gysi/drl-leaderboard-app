# Set the variable value in *.tfvars file
# or using -var="hcloud_token=..." CLI option

variable "hcloud_token" {}
variable "hcloud_name" {}

resource "hcloud_ssh_key" "ssh_key_drl_leaderboard_app" {
  name = "ssh_key_drl_leaderboard_app"
  public_key = file("../.ssh/id_ed25519.pub")
}

# Configure the Hetzner Cloud Provider
provider "hcloud" {
  token = var.hcloud_token
}

resource "hcloud_server" "prod4" {
  name = "${var.hcloud_name}4"
  server_type = "cax11"
  image = "debian-12"
  location = "nbg1"
  ssh_keys = ["ssh_key_drl_leaderboard_app"]

  depends_on = [hcloud_ssh_key.ssh_key_drl_leaderboard_app]
}

resource "hcloud_server" "test3" {
  name = "${var.hcloud_name}-test3"
  server_type = "cax11"
  image = "debian-12"
  location = "nbg1"
  ssh_keys = ["ssh_key_drl_leaderboard_app"]

  depends_on = [hcloud_ssh_key.ssh_key_drl_leaderboard_app]
}
