<template>
  <q-card bordered class="faq-card bg-grey-9 text-white grid-item" style="max-width: 40rem">
    <q-card-section class="faq-card-header">
      <div class="text-h6">{{ title }}</div>
    </q-card-section>

    <q-separator inset />
    <q-card-section
      class="faq-card-content text-black"
      :class="!isExpanded ? 'truncated' : ''"
      style="font-size: 1.1rem;"
    >
      <slot></slot>
    </q-card-section>
    <q-btn
      color="grey"
      flat
      :icon="isExpanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
      v-bind:class="{
        'expanded-button': isExpanded,
        'truncated-button': !isExpanded
      }"
      @click="isExpanded = !isExpanded; console.log('clicked')"
      :style="{
        'color': isExpanded ? 'rgba(0,0,0,0)' : 'black',
      }"
    />
  </q-card>
</template>

<script>
export default {
  name: 'FAQCard',
  props: {
    title: {
      type: String,
      required: true
    },
    msnry: {
      type: Object
    },
    initiallyExpanded: {
      type: Boolean,
      default: false
    }
  },
  setup () {
  },
  data () {
    return {
      console: console,
      isExpanded: this.initiallyExpanded
    }
  },
  watch: {
    isExpanded: {
      handler() {
        if(!this.msnry) return;
        this.msnry.layout();
        console.log('updated');
      },
      flush: 'post'
    }
  }
}
</script>

<style lang="sass" scoped>
.truncated
  max-height: 10rem
  overflow: hidden
  position: relative

.truncated::after
  content: ""
  //background: linear-gradient(to bottom, rgba(255,255,255,0) 0%, white 90%)
  position: absolute
  bottom: 0
  left: 0
  right: 0
  height: 10rem

.truncated-button
  position: absolute
  bottom: 0
  width: 100%
  height: 10rem

.expanded-button
  position: absolute
  bottom: 0
  width: 100%
  height: calc(100% - 64px)
  //pointer-events: none

:deep(.expanded-button span)
  align-content: end
  align-items: end
  color: rgba(0,0,0,0)

:deep(.truncated-button span)
  align-items: end
  align-content: end
  flex-flow: column
  justify-content: end
</style>
