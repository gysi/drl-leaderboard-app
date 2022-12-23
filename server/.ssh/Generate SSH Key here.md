# How to create an ssh key
Example:
```bash
ssh-keygen -a 256 -t ed25519 -C "drl-leaderboard-app@i337.de"
```

The ssh key should be stored in this directory and should be named `id_ed25519`.
The password you used should be stored in the `.dockerenv` file.
