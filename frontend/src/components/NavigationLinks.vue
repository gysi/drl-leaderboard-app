<template>
  <q-expansion-item
    v-if="type === 'expansion-item'"
    :label="title"
    :default-opened="defaultOpened"
    :content-inset-level="contentInsetLevel"
    :header-inset-level="headerInsetLevel"
    :dense="dense || isExpanded"
    :dense-toggle="denseToggle"
    :duration="150"
    v-model="isExpanded"
  >
    <template v-for="(child, index) in children" :key="index">
      <NavigationLinks v-bind="child" />
    </template>
  </q-expansion-item>
  <q-item
    v-if="type === 'link'"
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
      <q-item-label caption v-if="caption">{{ caption }}</q-item-label>
    </q-item-section>
    <q-item-section side top v-if="openInNew" style="padding-right: 0">
      <q-icon name="open_in_new" />
    </q-item-section>
  </q-item>
</template>

<script setup>

import {ref} from "vue";

const props = defineProps({
  type: {
    type: String,
    required: true
  },
  // ITEM TYPE
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
  },
  // EXPANSION TYPE
  defaultOpened: {
    type: Boolean,
    default: false
  },
  contentInsetLevel: {
    type: Number,
    default: 0,
  },
  headerInsetLevel: {
    type: Number,
    default: 0
  },
  dense: {
    type: Boolean,
    default: false,
  },
  denseToggle: {
    type: Boolean,
    default: true
  },
  // COMPLEX TYPE WITH CHILDS
  children: {
    type: Array,
    default(rawProps) {
      return []
    }
  }
});

const isExpanded = ref(props.defaultOpened);
</script>
