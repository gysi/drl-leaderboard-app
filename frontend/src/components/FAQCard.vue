<template>
  <q-card bordered class="faq-card bg-grey-9 text-white grid-item" style="max-width: 40rem">
    <q-card-section class="faq-card-header">
      <slot name="title">
        <div class="text-h6">{{ title }}</div>
      </slot>
      <q-btn
        v-if="isExpanded"
        color="grey"
        flat
        icon="keyboard_arrow_up"
        class="expanded-button"
        @click="isExpanded = !isExpanded"
      />
    </q-card-section>

    <q-separator inset />
    <q-card-section
      class="faq-card-content text-black"
      :class="!isExpanded ? 'truncated' : ''"
      style="font-size: 1.1rem;"
    >
      <slot></slot>
      <q-btn
        v-if="!isExpanded"
        color="grey"
        flat
        icon="keyboard_arrow_down"
        class="truncated-button"
        @click="isExpanded = !isExpanded"
      />
    </q-card-section>
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
.faq-card-header
  display: flex
  justify-content: space-between
  align-items: center
  >div
    margin-right: 22px

.faq-card-content
  overflow-x: auto

.truncated
  max-height: 10rem
  overflow: hidden
  position: relative

.truncated::after
  content: ""
  position: absolute
  top: 0
  left: 0
  right: 0
  bottom: 0

.truncated-button
  position: absolute
  top: 0
  left: 0
  right: 0
  bottom: 0
  z-index: 1

.expanded-button
  position: absolute
  top: 0
  left: 0
  width: 100%
  height: 100%

:deep(.expanded-button span)
  //align-content: end
  //align-items: end
  //color: rgba(0,0,0,0)
  align-items: center
  flex-flow: row
  justify-content: end

:deep(.truncated-button span)
  align-items: end
  align-content: end
  flex-flow: column
  justify-content: end

:deep(.q-separator--horizontal-inset)
  margin: 0

:deep(a:link)
  color: var(--app-faq-card-link-color)

:deep(a:visited)
  color: var(--app-faq-card-link-color-visited)

:deep(a:hover)
  color: var(--app-faq-card-link-color-hover)

:deep(a:active)
  color: var(--app-faq-card-link-color-active)
</style>

