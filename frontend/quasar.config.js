/* eslint-env node */

/*
 * This file runs in a Node context (it's NOT transpiled by Babel), so use only
 * the ES6 features that are supported by your Node version. https://node.green/
 */

// Configuration for your app
// https://v2.quasar.dev/quasar-cli-vite/quasar-config-js

const vue = require('@vitejs/plugin-vue');

const { configure } = require('quasar/wrappers');
const path = require('path');
const { simpleSitemapAndIndex } = require('sitemap');
const { sitemapRoutes } = require('./src/router/sitemapRoutes.js');
const { Readable } = require('stream');
const ejs = require('ejs');
const fs = require('fs');

const srcDir = path.join(__dirname, 'src');
const pubDir = path.join(__dirname, 'public');

function processEJSFiles(dir) {
  fs.readdirSync(dir).forEach((file) => {
    const filePath = path.join(dir, file);
    const fileStat = fs.statSync(filePath);

    if (fileStat.isDirectory()) {
      // If the current file is a directory, recursively process its contents
      processEJSFiles(filePath);
    } else if (path.extname(file) === '.ejs') {
      // If the current file is an .ejs file, process it using EJS
      const ejsTemplate = fs.readFileSync(filePath, 'utf-8');
      const htmlContent = ejs.render(ejsTemplate);

      const outputFilename = path.basename(file, '.ejs');
      const outputFilePath = path.join(dir, outputFilename);

      fs.writeFileSync(outputFilePath, htmlContent);
      console.log(`Processed: ${filePath}`);
    }
  });
}
processEJSFiles(pubDir);
processEJSFiles(srcDir);

async function generateSitemap() {
  simpleSitemapAndIndex({
    hostname: process.env.DLAPP_ENV === 'PROD' ? 'https://drl-leaderboards.miau.io' : 'https://drl-leaderboards-test.miau.io',
    destinationDir: './public',
    sourceData: sitemapRoutes,
    gzip: false,
    limit: 45000
  }).then(() => {
    console.log('Sitemap generated');
  })
}

module.exports = configure(function (/* ctx */) {
  return {
    eslint: {
      // fix: true,
      // include = [],
      // exclude = [],
      // rawOptions = {},
      warnings: true,
      errors: true
    },

    // https://v2.quasar.dev/quasar-cli/prefetch-feature
    // preFetch: true,

    // app boot file (/src/boot)
    // --> boot files are part of "main.js"
    // https://v2.quasar.dev/quasar-cli/boot-files
    boot: [
      'i18n',
      'axios'
    ],

    // https://v2.quasar.dev/quasar-cli-vite/quasar-config-js#css
    css: [
      'app.scss',
      '~flag-icons/css/flag-icons.min.css'
    ],

    // https://github.com/quasarframework/quasar/tree/dev/extras
    extras: [
      // 'ionicons-v4',
      // 'mdi-v5',
      // 'fontawesome-v6',
      // 'eva-icons',
      // 'themify',
      // 'line-awesome',
      // 'roboto-font-latin-ext', // this or either 'roboto-font', NEVER both!

      'roboto-font', // optional, you are not bound to it
      'material-icons', // optional, you are not bound to it
    ],

    // Full list of options: https://v2.quasar.dev/quasar-cli-vite/quasar-config-js#build
    build: {
      target: {
        browser: [ 'es2019', 'edge88', 'firefox78', 'chrome87', 'safari13.1' ],
        node: 'node18'
      },

      minify: 'terser',

      vueRouterMode: 'history', // available values: 'hash', 'history'
      // vueRouterBase,
      // vueDevtools,
      // vueOptionsAPI: false,

      // rebuildCache: true, // rebuilds Vite/linter/etc cache on startup

      // publicPath: '/',
      // analyze: true,
      env: (function () {
        global_vars = {
          DLAPP_ENV: process.env.DLAPP_ENV ? process.env.DLAPP_ENV : 'LOCAL',
          DLAPP_SWAGGER_URL_PART: "/swagger-ui.html"
        }
        env_vars = {
          LOCAL: {
            // 192.168.178.31 for testing vm from host/phone
            DLAPP_URL: 'http://localhost:9000',
            DLAPP_API_URL: 'http://localhost:8080/api',
            // DLAPP_THUMBOR_URL: 'http://localhost/thumbor' // if you run the docker-compose-full.yaml
            DLAPP_THUMBOR_URL: 'http://localhost:8888/thumbor'
          },
          STAGING: {
            DLAPP_URL: 'https://drl-leaderboards-test.miau.io',
            DLAPP_API_URL: 'https://drl-leaderboards-test.miau.io/api',
            DLAPP_THUMBOR_URL: 'https://drl-leaderboards-test.miau.io/thumbor'
          },
          PROD: {
            DLAPP_URL: 'https://drl-leaderboards.miau.io',
            DLAPP_API_URL: 'https://drl-leaderboards.miau.io/api',
            DLAPP_THUMBOR_URL: 'https://drl-leaderboards.miau.io/thumbor'
          }
        };
        console.log({ ...global_vars, ...env_vars[process.env.DLAPP_ENV ? process.env.DLAPP_ENV : 'LOCAL'] });
        return { ...global_vars, ...env_vars[process.env.DLAPP_ENV ? process.env.DLAPP_ENV : 'LOCAL'] };
      })(),
      // rawDefine: {}
      // ignorePublicFolder: true,
      // minify: false,
      // polyfillModulePreload: true,
      // distDir

      extendViteConf (viteConf) {
        generateSitemap().catch(err => {
          console.error('Error generating sitemap:', err);
        });
      },
      viteVuePluginOptions: {
        template: {
          compilerOptions: {
            // treat all tags with a dash as custom elements
            isCustomElement: (tag) => tag.includes('shadow-')
          }
        }
      },

      vitePlugins: [
        ['@intlify/vite-plugin-vue-i18n', {
          // if you want to use Vue I18n Legacy API, you need to set `compositionOnly: false`
          // compositionOnly: false,

          // you need to set i18n resource including paths !
          include: path.resolve(__dirname, './src/i18n/**')
        },
        vue({
          template: {
            compilerOptions: {
              // treat all tags with a dash as custom elements
              isCustomElement: (tag) => tag.includes('-')
            }
          }}),
      ]]
    },

    // Full list of options: https://v2.quasar.dev/quasar-cli-vite/quasar-config-js#devServer
    devServer: {
      // https: true
      open: true // opens browser window automatically
    },

    // https://v2.quasar.dev/quasar-cli-vite/quasar-config-js#framework
    framework: {
      config: {
        dark: 'light' // auto
      },

      // iconSet: 'material-icons', // Quasar icon set
      // lang: 'en-US', // Quasar language pack

      // For special cases outside of where the auto-import strategy can have an impact
      // (like functional components as one of the examples),
      // you can manually specify Quasar components/directives to be available everywhere:
      //
      // components: [],
      // directives: [],

      // Quasar plugins
      plugins: [
        'LocalStorage',
        'SessionStorage'
      ]
    },

    // animations: 'all', // --- includes all animations
    // https://v2.quasar.dev/options/animations
    animations: [],

    // https://v2.quasar.dev/quasar-cli-vite/quasar-config-js#property-sourcefiles
    // sourceFiles: {
    //   rootComponent: 'src/App.vue',
    //   router: 'src/router/index',
    //   store: 'src/store/index',
    //   registerServiceWorker: 'src-pwa/register-service-worker',
    //   serviceWorker: 'src-pwa/custom-service-worker',
    //   pwaManifestFile: 'src-pwa/manifest.json',
    //   electronMain: 'src-electron/electron-main',
    //   electronPreload: 'src-electron/electron-preload'
    // },

    // https://v2.quasar.dev/quasar-cli/developing-ssr/configuring-ssr
    ssr: {
      // ssrPwaHtmlFilename: 'offline.html', // do NOT use index.html as name!
                                          // will mess up SSR

      // extendSSRWebserverConf (esbuildConf) {},
      // extendPackageJson (json) {},

      pwa: false,

      // manualStoreHydration: true,
      // manualPostHydrationTrigger: true,

      prodPort: 3000, // The default port that the production server should use
                      // (gets superseded if process.env.PORT is specified at runtime)

      middlewares: [
        'render' // keep this as last one
      ]
    },

    // https://v2.quasar.dev/quasar-cli/developing-pwa/configuring-pwa
    pwa: {
      workboxMode: 'generateSW', // or 'injectManifest'
      injectPwaMetaTags: true,
      swFilename: 'sw.js',
      manifestFilename: 'manifest.json',
      useCredentialsForManifestTag: false,
      // useFilenameHashes: true,
      // extendGenerateSWOptions (cfg) {}
      // extendInjectManifestOptions (cfg) {},
      // extendManifestJson (json) {}
      // extendPWACustomSWConf (esbuildConf) {}
    },

    // Full list of options: https://v2.quasar.dev/quasar-cli/developing-cordova-apps/configuring-cordova
    cordova: {
      // noIosLegacyBuildFlag: true, // uncomment only if you know what you are doing
    },

    // Full list of options: https://v2.quasar.dev/quasar-cli/developing-capacitor-apps/configuring-capacitor
    capacitor: {
      hideSplashscreen: true
    },

    // Full list of options: https://v2.quasar.dev/quasar-cli/developing-electron-apps/configuring-electron
    electron: {
      // extendElectronMainConf (esbuildConf)
      // extendElectronPreloadConf (esbuildConf)

      inspectPort: 5858,

      bundler: 'packager', // 'packager' or 'builder'

      packager: {
        // https://github.com/electron-userland/electron-packager/blob/master/docs/api.md#options

        // OS X / Mac App Store
        // appBundleId: '',
        // appCategoryType: '',
        // osxSign: '',
        // protocol: 'myapp://path',

        // Windows only
        // win32metadata: { ... }
      },

      builder: {
        // https://www.electron.build/configuration/configuration

        appId: 'drl-leaderboard-frontend'
      }
    },

    // Full list of options: https://v2.quasar.dev/quasar-cli-vite/developing-browser-extensions/configuring-bex
    bex: {
      contentScripts: [
        'my-content-script'
      ],

      // extendBexScriptsConf (esbuildConf) {}
      // extendBexManifestJson (json) {}
    }
  }
});
