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
                       width="120" height="68"
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
</style>
