<template>
  <q-page padding style="height: 100%" class="row items-start">
    <q-table
      title="Tracks"
      :columns="columns"
      :rows="rows"
      :loading="loading"
      :pagination="pagination"
      row-key="id"
      class="my-sticky-header-table col-auto"
      style="max-height: 80%;"
      flat
      bordered
    />
  </q-page>
</template>

<script>
import axios from 'axios';

export default {
  name: 'TracksPage',
  data() {
    return {
      pagination: {
        rowsPerPage: 50,
      },
      columns: [
        { name: 'name', label: 'Track', field: 'name' },
        { name: 'mapName', label: 'Map', field: 'mapName' },
        { name: 'parentCategory', label: 'Parent Category', field: 'parentCategory' },
      ],
      rows: [],
      loading: false
    }
  },
  methods: {
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
}
</script>

<style lang="sass">
.my-sticky-header-table
  /* height or max-height is important */
  //min-height: inherit

  .q-table__top,
  .q-table__bottom,
  thead tr:first-child th
    /* bg color is important for th; just specify one */
    background-color: $primary
    color: white

  thead tr th
    position: sticky
    z-index: 1
  thead tr:first-child th
    top: 0

  /* this is when the loading indicator appears */
  &.q-table--loading thead tr:last-child th
    /* height of all previous header rows */
    top: 48px
</style>
