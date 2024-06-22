# DRL Leaderboard Frontend (drl-leaderboard-frontend)
**Tech/Framework/Libs:**
* Node (18)
* Quasar (Vue, Vite) -> using the Quasar CLI (with Vite) flavour
* Yarn

### Install yarn
Yarn is included within the repository, just tell node to find it automatically using corepack
```bash
corepack enable
```

### Install dependencies
```bash
yarn install
```

### Start the app in development mode (hot-code reloading, error reporting, etc.)
```bash
yarn quasar dev
```

### Lint the files
```bash
yarn lint
```

### Format the files
```bash
yarn format
```

### Build the app for production
```bash
DLAPP_ENV=PROD yarn quasar build
```

### Customize the configuration
See [Configuring quasar.config.js](https://v2.quasar.dev/quasar-cli-vite/quasar-config-js).
