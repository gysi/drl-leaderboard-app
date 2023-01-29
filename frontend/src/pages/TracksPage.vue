<template>
  <q-page padding :style-fn="qpageStyleFn" class="q-pa-md">
    <q-table
      title="Tracks"
      :columns="columns"
      :rows="rows"
      :loading="loading"
      :pagination="pagination"
      row-key="id"
      class="my-sticky-header-table"
      style="max-height: 100%; overflow: auto;"
      flat
      bordered
    >
      <template v-slot:body="props">
        <q-tr>
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
            style="padding: 5px 5px 5px 5px;"
          >
            <q-item style="padding:0" v-if="col.name === 'name'"
                    clickable
                    :to="`/track-lb/?trackId=${props.row.id}`"
                    class="q-item-player-region"
            >
              <q-item-section thumbnail style="padding-right: 5px; margin: 0;">
                  <img src="~assets/maps/maps-120x68.png"
                       :class="'map-'+props.row.mapName.replaceAll(/\.| /g, '-').toLocaleLowerCase()"
                  />
              </q-item-section>
              <q-item-section>
                <q-item-label class="track-item-label">{{ props.row.name }}</q-item-label>
                <q-item-label caption>
                  <q-badge class="track-chip-map">{{ props.row.mapName }}</q-badge>
                </q-item-label>
                <q-item-label caption>
                  <q-badge class="track-chip-parentcategory">{{ props.row.parentCategory }}</q-badge>
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'
import axios from 'axios';

export default defineComponent({
  name: 'TracksPage',
  data() {
    return {
      pagination: {
        rowsPerPage: 50,
      },
      columns: [
        { name: 'name', label: 'Name', field: 'name', align: 'left' }
      ],
      rows: [],
      loading: false
    }
  },
  methods: {
    qpageStyleFn(offset, height) {
      return {
        minHeight: offset ? `calc(100vh - ${offset}px)` : '100vh',
        maxHeight: offset ? `calc(100vh - ${offset}px)` : '100vh',
        height: offset ? `calc(100vh - ${offset}px)` : '100vh',
      }
    },
    async fetchData() {
      this.loading = true;
      try {
        const response = await axios.get(process.env.DLAPP_API_URL+'/tracks');
        this.rows = response.data;
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
    }
  },
  created() {
    this.fetchData();
  },
  mounted() {

  },
  beforeUnmount() {
    clearInterval(this.interval);
  }
})
</script>

<style lang="scss" scoped>
:deep(.track-item-label) {
  font-size: 19px;
}

// For GRID view
//.my-sticky-grid-table {
//  :deep(.q-table__top) {
//    position: sticky;
//    top: 0;
//    z-index: 1;
//  }
//  :deep(.q-table__bottom) {
//    position: sticky;
//    bottom: 0;
//    z-index: 1;
//  }
//}

// montage -resize 120 map-* -auto-orient -geometry +1+1 -background none -format png maps.png
// http://www.spritecow.com/
:deep(.map-2017-world-championship) {
  object-fit: none;
  object-position: -1px -1px;
  width: 120px;
  height: 68px;
}

:deep(.map-adventuredome) {
  object-fit: none;
  object-position: -123px -1px;
  width: 120px;
  height: 68px;
}

:deep(.map-allianz-riviera) {
  object-fit: none;
  object-position: -245px -1px;
  width: 120px;
  height: 68px;
}

:deep(.map-atlanta-aftermath) {
  object-fit: none;
  object-position: -367px -1px;
  width: 120px;
  height: 68px;
}

:deep(.map-biosphere-2) {
  object-fit: none;
  object-position: -489px -1px;
  width: 120px;
  height: 68px;
}

:deep(.map-bmw-welt) {
  object-fit: none;
  object-position: -611px -1px;
  width: 120px;
  height: 68px;
}

:deep(.map-boston-foundry) {
  object-fit: none;
  object-position: -1px -71px;
  width: 120px;
  height: 68px;
}

:deep(.map-bridge) {
  object-fit: none;
  object-position: -123px -71px;
  width: 120px;
  height: 68px;
}

:deep(.map-california-nights) {
  object-fit: none;
  object-position: -245px -71px;
  width: 120px;
  height: 68px;
}

:deep(.map-campground) {
  object-fit: none;
  object-position: -367px -71px;
  width: 120px;
  height: 68px;
}

:deep(.map-championship-kingdom) {
  object-fit: none;
  object-position: -367px -71px;
  width: 120px;
  height: 68px;
}

:deep(.map-detroit) {
  object-fit: none;
  object-position: -611px -71px;
  width: 120px;
  height: 68px;
}

:deep(.map-drl-sandbox) {
  object-fit: none;
  object-position: -1px -141px;
  width: 120px;
  height: 68px;
}

:deep(.map-drone-park) {
  object-fit: none;
  object-position: -123px -141px;
  width: 120px;
  height: 68px;
}

:deep(.map-gates-of-new-york) {
  object-fit: none;
  object-position: -245px -141px;
  width: 120px;
  height: 68px;
}

:deep(.map-hard-rock-stadium) {
  object-fit: none;
  object-position: -367px -141px;
  width: 120px;
  height: 68px;
}

:deep(.map-l-a-pocalypse) {
  object-fit: none;
  object-position: -367px -141px;
  width: 120px;
  height: 68px;
}

:deep(.map-mardi-gras-world) {
  object-fit: none;
  object-position: -611px -141px;
  width: 120px;
  height: 68px;
}

:deep(.map-mega-city) {
  object-fit: none;
  object-position: -1px -211px;
  width: 120px;
  height: 68px;
}

:deep(.map-miami-lights) {
  object-fit: none;
  object-position: -1px -211px;
  width: 120px;
  height: 68px;
}

:deep(.map-multigp) {
  object-fit: none;
  object-position: -245px -211px;
  width: 120px;
  height: 68px;
}

:deep(.map-munich-playoffs) {
  object-fit: none;
  object-position: -367px -211px;
  width: 120px;
  height: 68px;
}

:deep(.map-ohio-crashsite) {
  object-fit: none;
  object-position: -489px -211px;
  width: 120px;
  height: 68px;
}

:deep(.map-out-of-service) {
  object-fit: none;
  object-position: -611px -211px;
  width: 120px;
  height: 68px;
}

:deep(.map-project-manhattan) {
  object-fit: none;
  object-position: -1px -281px;
  width: 120px;
  height: 68px;
}

:deep(.map-skatepark-la) {
  object-fit: none;
  object-position: -123px -281px;
  width: 120px;
  height: 68px;
}

:deep(.map-the-house) {
  object-fit: none;
  object-position: -245px -281px;
  width: 120px;
  height: 68px;
}

:deep(.map-u-s--air-force-boneyard) {
  object-fit: none;
  object-position: -367px -281px;
  width: 120px;
  height: 68px;
}

:deep(.map-u-s--air-force-night-mode) {
  object-fit: none;
  object-position: -489px -281px;
  width: 120px;
  height: 68px;
}
</style>
