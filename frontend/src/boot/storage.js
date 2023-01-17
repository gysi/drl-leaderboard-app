import { boot } from 'quasar/wrappers'
import {
  Quasar,
  LocalStorage,
  SessionStorage
} from 'quasar'

export default boot(({ app }) => {
  app.use(Quasar, {
    plugins: {
      LocalStorage,
      SessionStorage
    }
  })
})

