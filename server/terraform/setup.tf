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

resource "hcloud_server" "prod2" {
  name = "${var.hcloud_name}2"
  server_type = "cx11"
  image = "debian-11"
  location = "nbg1"
  ssh_keys = ["ssh_key_drl_leaderboard_app"]

  depends_on = [hcloud_ssh_key.ssh_key_drl_leaderboard_app]
}

resource "hcloud_server" "test" {
  name = "${var.hcloud_name}-test"
  server_type = "cx11"
  image = "debian-11"
  location = "nbg1"
  ssh_keys = ["ssh_key_drl_leaderboard_app"]

  depends_on = [hcloud_ssh_key.ssh_key_drl_leaderboard_app]
}
