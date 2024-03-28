<template>
  <q-item
    clickable
    tag="a"
    :to="external ? undefined : link"
    :href="external ? link : undefined"
    :target="external ? '_blank' : undefined"
    exact
  >
    <q-item-section v-if="icon" avatar>
      <q-icon :name="icon" />
    </q-item-section>

    <q-item-section>
      <q-item-label>{{ title }}<q-badge color="accent" style="margin-left: 5px" v-if="badge" :label="badge" align="top"/></q-item-label>
      <q-item-label caption>{{ caption }}</q-item-label>
    </q-item-section>
    <q-item-section side top v-if="openInNew" style="padding-right: 0">
      <q-icon name="open_in_new" />
    </q-item-section>
  </q-item>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'NavigationLinks',
  props: {
    title: {
      type: String,
      required: true
    },
    caption: {
      type: String,
      default: ''
    },
    link: {
      type: String,
      default: '#'
    },
    external: {
      type: Boolean,
      default: false
    },
    icon: {
      type: String,
      default: ''
    },
    badge: {
      type: String,
      default: ''
    },
    openInNew: {
      type: Boolean,
      default: false
    }
  }
})
</script>
