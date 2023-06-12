<template>
  <q-page padding :style-fn="qpageStyleFn" class="q-pa-md">
    <q-table
      title="Tracks"
      :columns="columns"
      :rows="rows"
      row-key="id"
      class="my-sticky-header-table"
      style="max-height: 100%;"
      :loading="loading"
      loading-label="Loading tracks..."
      no-data-label="Tracks couldn't be loaded, please try again"
      flat
      bordered
      hide-pagination
      :pagination="{rowsPerPage: 0}"
    >
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td v-for="col in props.cols" :key="col.name" :props="props"
            class="td-borders-font-size16 trackspage-td-padding"
          >
            <q-item style="padding:0" v-if="col.name === 'name'"
                    clickable
                    :to="`/track-lb?trackId=${props.row.id}`"
                    class="q-item-player-region"
            >
              <q-item-section thumbnail style="padding-right: 10px; margin: 0;">
                  <img src="~assets/maps/maps-120x68.png"
                       :class="'map-'+props.row.mapName.replaceAll(/[. ]/g, '-').toLocaleLowerCase()"
                       width="120" height="68"
                  />
              </q-item-section>
              <q-item-section >
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

<script setup>
  import axios from 'axios';
  import { ref, shallowRef } from 'vue';

  const columns = [
    { name: 'name', label: 'Name', field: 'name', align: 'left' }
  ]
  const rows = shallowRef([]);
  const loading = ref(true);

  function qpageStyleFn(offset, height) {
    return {
      minHeight: offset ? `calc(100vh - ${offset}px)` : '100vh',
      maxHeight: offset ? `calc(100vh - ${offset}px)` : '100vh',
      height: offset ? `calc(100vh - ${offset}px)` : '100vh',
    }
  }
  const fetchData = async () => {
    try {
      const response = await axios.get(process.env.DLAPP_API_URL+'/tracks');
      rows.value = response.data;
    } catch (error) {
      console.error(error);
    } finally {
      loading.value = false;
    }
  };
  fetchData();
</script>

<style lang="scss">
  .trackspage-td-padding {
    padding: 5px 5px 5px 5px;
  }
</style>

<style lang="scss" scoped>
:deep(.track-item-label) {
  font-size: 19px;
}
</style>
