# ⚠️ Project Archived: DRL Simulator End of Life

**Notice:** As of February 2026, this repository has been officially archived and is no longer maintained.

---

## 🛑 Why is this project archived?
The **Drone Racing League (DRL) Simulator** official online infrastructure has been decommissioned. Since approximately **December 2025**, the game's servers have been offline, preventing the simulator from syncing user data, progress, and official leaderboards. 

Because this application relies on the DRL internal APIs to fetch and display rankings, the core functionality of this web app is no longer operational. 

### Current Status
* **Domain Status:** The domain `drl-leaderboards.com` is being decommissioned and will soon be inaccessible.
* **Server Shutdown:** Community reports indicate that DRL (now owned by Infinite Reality) has deprecated the simulator's online services. Players can currently only access the simulator via "Offline Mode" workarounds.
* **Functionality:** Without the official DRL backend, this app can no longer verify laps, update rankings, or track "Beaten By" status.

## 🛠 For Developers & Forks
If you are interested in adapting this code for a different simulator or a custom community-run database, the source code remains available under the **AGPL-3.0 license**.

* **`/backend`**: Data processing and storage logic (Java).
* **`/frontend`**: The Vue-based web interface.
* **`/server`**: Ansible and Docker configurations for hosting.

If a community-driven "Local Data Mod" for the DRL Sim is eventually released, this codebase could serve as a foundation for a new community-hosted leaderboard.

## 🔗 Resources & Context
For more information on the current state of the DRL Simulator and how to access offline mode, please refer to these community resources:
* **Steam Community:** [DRL Connection Failed Discussion](https://steamcommunity.com/app/641780/discussions/0/693121929170693051)
* **Video Guide:** [How to fix the DRL connection failed error (Offline Workaround)](https://www.youtube.com/watch?v=9RP974aaOlI)

---
> **A Note from the Maintainer:**
> Thank you to the DRL community for the years of competition, the feedback, and the support. It was a great run.
> 
> — **gysi**

# DRL Leaderboard App
Webapp for displaying DRL Simulator leaderboards.
https://drl-leaderboards.com/

**Modules**
* backend (Data processing and storage)
* frontend (Webapp)
* server (Infrastructure configuration)
