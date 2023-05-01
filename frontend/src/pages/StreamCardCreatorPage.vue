<template>
  <q-page padding style="height: 100%" class="q-pa-md items-start">
    <q-card class="doc-api q-mb-md" flat bordered style="max-width: 1000px">
<!--      Header-->
      <div class="header-toolbar row items-center q-pr-sm">
        <div class="doc-card-title q-my-xs q-mr-sm ">Stream Cards Settings</div>
        <div id="streamcard-info">
          <p>Create and Customize stream cards, that can be used as an overlay within your stream.</p>
          <p>The data within the stream cards are automatically updated every 10 seconds, so you don't need to refresh the page.</p>
        </div>
      </div>
<!--      Tabs-->
      <q-tabs class="header-tabs" v-model="currentTab" active-color="brand-primary" indicator-color="brand-primary" align="left" :breakpoint="0">
        <q-tab v-for="tab in tabsList" :key="`api-tab-${tab}`" :name="tab" class="header-btn">
          <div class="row no-wrap items-center">
            <span class="q-mr-xs text-capitalize">{{ settings[tab].name }}</span>
            <q-badge v-if="Object.keys(settings[tab])" :label="Object.keys(settings[tab].content).length" />
          </div>
        </q-tab>
      </q-tabs>

      <q-separator />

      <q-tab-panels v-model="currentTab" animated>
        <q-tab-panel class="q-pa-none" v-for="tab in tabsList" :name="tab" :key="tab">
          <div class="doc-api__container row no-wrap" v-if="Object.keys(settings[currentTab].content).length !== 1">
            <div class="col-auto">
              <q-tabs class="header-tabs doc-api__subtabs" v-model="currentInnerTab" active-color="brand-primary" indicator-color="brand-primary" :breakpoint="0" vertical dense shrink>
                <q-tab class="doc-api__subtabs-item header-btn" v-for="(innerTab, key) in settings[currentTab].content" :key="`api-inner-tab-${key}`" :name="key">
                  <div class="row no-wrap items-center self-stretch q-pl-sm">
                    <span class="q-mr-xs text-capitalize">{{ innerTab.name }}</span>
                    <div class="col" />
                    <q-badge v-if="innerTab.content != null" :label="Object.keys(innerTab.content).length" />
                  </div>
                </q-tab>
              </q-tabs>
            </div>

            <q-separator vertical />
            <q-tab-panels class="col" v-model="currentInnerTab" animated transition-prev="slide-down" transition-next="slide-up">
              <q-tab-panel class="q-pa-none" v-for="(innerTab, key) in settings[currentTab].content" :name="key" :key="key">
                <div class="doc-api-entrys">
                  <div class="doc-api-entry row" v-for="(setting, key) in settings[currentTab].content[currentInnerTab].content" :key="setting.name">
                    <StreamCardSetting v-model="settings[currentTab].content[currentInnerTab].content[key]" />
                  </div>
                </div>
              </q-tab-panel>
            </q-tab-panels>
          </div>
          <div class="doc-api__container" v-else>
            <div class="doc-api-entry row" v-for="(setting, key) in settings[currentTab].content[currentInnerTab].content" :key="setting.name">
              <StreamCardSetting v-model="settings[currentTab].content[currentInnerTab].content[key]" />
            </div>
          </div>
        </q-tab-panel>
      </q-tab-panels>
    </q-card>
    <div id="streamcard-preview" class="row justify-start q-gutter-x-md">
      <div id="streamcard-page" class="col-auto" :style='{
        width: settings.general.content.previewBox.content.width.settings.value+settings.general.content.previewBox.content.width.settings.unit,
         height: settings.general.content.previewBox.content.height.settings.value+settings.general.content.previewBox.content.height.settings.unit
      }'>
        <StreamCardPage :compressed-json="compressedData" style="width: 100%; height: 100%"/>
      </div>
      <div id="embed" class="col" style="max-width: 500px">
        <q-input filled v-model="iFrameUrlFull" label="Url" class="iframe-preview-input">
          <template v-if="iFrameUrlFull" v-slot:after>
            <q-btn label="Copy" @click="copyUrlToClipboard"/>
          </template>
        </q-input>
      </div>
    </div>
  </q-page>

</template>

<script setup>
import { ref, watch, onMounted, reactive, computed } from 'vue';
import { compressJSON, decompressJSON } from "src/modules/JsonCompression";
import ColorPickerButton from "components/ColorPickerButton.vue";
import StreamCardPage from "pages/StreamCardPage.vue";
import StreamCardSetting from "components/StreamCardSetting.vue";
import { copyToClipboard } from 'quasar'

const settings = reactive({
  general: {
    name: "General",
    content: {
      general: {
        name: "General",
        content: {
          player: { name: "Focused Playername", type: "input", settings: { value: "gysi",}},
          fontColor: { name: "Font Color", type: "color", settings: { value: "rgba(255,255,255,1)",}},
          backgroundColor: { name: "Background Color", type: "color", settings: { value: 'rgba(0,0,0,0.56)',}},
        }
      },
      previewBox: {
        name: "Preview Box",
        content: {
          width: { name: "Width", type: "slider", settings: { value: 250, min: 100, max: 800, step: 5, unit: 'px'}},
          height: { name: "Height", type: "slider", settings: { value: 300, min: 100, max: 800, step: 5, unit: 'px'}},
        }
      },
    }
  },
  rankingsCard: {
    name: "Rankings Card",
    type: "card",
    content: {
      general: {
        name: "General",
        content: {
          padding: { name: "Padding", type: "slider", settings: { value: 5, min: 0, max: 16, step: 0.2, unit: 'px'}},
        }
      },
      header: {
        name: "Header",
        content: {
          text: { name: "Text", type: "input", settings: { value: "Rankings",}},
          size: { name: "Size", type: "slider", settings: { value: 30, min: 16, max: 70, step: 1, unit: 'px'} },
          lineHeight: { name: "Line Height", type: "slider", settings: { value: 1.2, min: 1, max: 3, step: 0.1, unit: 'em'} },
          paddingLeft: { name: "Padding Left", type: "slider", settings: { value: 0, min: 0, max: 16, step: 0.2, unit: 'px'} },
          paddingRight: { name: "Padding Right", type: "slider", settings: { value: 0, min: 0, max: 16, step: 0.2, unit: 'px'} },
          paddingTop: { name: "Padding Top", type: "slider", settings: { value: 0, min: 0, max: 16, step: 0.2, unit: 'px'} },
          paddingBottom: { name: "Padding Bottom", type: "slider", settings: { value: 0, min: 0, max: 16, step: 0.2, unit: 'px'} },

        }
      },
      rankings: {
        name: 'Rankings',
        content: {
          topPositionCount: { name: "Top positions that should be displayed", type: "slider", settings: { value: 3, min: 0, max: 10, step: 1, unit: ''}},
          fontSize: { name: "Font Size", type: "slider", settings: { value: 18, min: 8, max: 50, step: 1, unit: 'px'}},
          width: { name: "Width", type: "slider", settings: { value: 260, min: 100, max: 800, step: 5, unit: 'px'}},
        },
      },
    },
  },
  trackCard: {
    name: "Track Card",
    type: "card",
    content: {
      general: {
        name: "General",
        content: {
          topPositionCount: { name: "Top positions that should be displayed", type: "slider", settings: { value: 3, min: 0, max: 10, step: 1, unit: ''}},
          padding: { name: "Padding", type: "slider", settings: { value: 5, min: 0, max: 16, step: 0.2, unit: 'px'}},
        }
      },
      header: {
        name: "Header",
        content: {
          text: { name: "Text", type: "input", settings: { value: "Track",}},
          size: { name: "Size", type: "slider", settings: { value: 40, min: 16, max: 70, step: 1, unit: 'px'} },
          lineHeight: { name: "Line Height", type: "slider", settings: { value: 1.5, min: 1, max: 3, step: 0.1, unit: 'em'} },
          paddingLeft: { name: "Padding Left", type: "slider", settings: { value: 5, min: 0, max: 16, step: 0.2, unit: 'px'} },
          paddingRight: { name: "Padding Right", type: "slider", settings: { value: 5, min: 0, max: 16, step: 0.2, unit: 'px'} },
          paddingTop: { name: "Padding Top", type: "slider", settings: { value: 5, min: 0, max: 16, step: 0.2, unit: 'px'} },
          paddingBottom: { name: "Padding Bottom", type: "slider", settings: { value: 5, min: 0, max: 16, step: 0.2, unit: 'px'} },

        }
      }
    },
  },
});

const tabsList = Object.keys(settings);
const currentTab = ref("general")
const currentInnerTab = ref(null)
let activeCard = ref({activeCard: "rankingsCard"});
watch(currentTab, val => {
  currentInnerTab.value = Object.keys(settings[val].content)[0]
  if (settings[val].type === "card") {
    activeCard.value.activeCard = val
  }
}, {immediate: true})
let compressedData = ref("");
const iFrameUrlFull = computed(() => {
  return `${process.env.DLAPP_URL}/stream-card?settings=${encodeURIComponent(compressedData.value)}`;
});

const copyUrlToClipboard = () => {
  copyToClipboard(iFrameUrlFull.value)
}

const onChange = function () {
  compressedData.value = compressJSON({...settings, ...activeCard.value});
};
watch([settings, activeCard], onChange, {deep: true});
onChange();
</script>

<style lang="sass">
#streamcard-info
  font-size: 18px
  margin: 8px 0px 0px 8px
  p
    margin: 0 0 2px

.streamcard-settings-box
  width: 400px
  p
    margin: 8px 0 0 0

#streamcard-page
  background: white


.iframe-preview-input .q-field__after
  align-items: stretch
  padding: 0


///// DOC CARD /////
.doc-card-title
  margin-left: -12px
  padding: 4px 8px 4px 28px
  position: relative
  border-radius: 3px 5px 5px 0
  background: #D8E1E5
  color: #757575
  font-size: 18px
  letter-spacing: .7px

  &:after
    content: ''
    position: absolute
    top: 100%
    left: 0
    width: 0
    height: 0
    border: 0 solid transparent
    border-width: 9px 0 0 11px
    border-top-color: scale-color(#D8E1E5, $lightness: -15%)

body.body--dark .doc-card-title
  background: #475D66
  color: #cbcbcb
  &:after
    border-top-color: scale-color(#475D66, $lightness: -30%)

.doc-api
  &__subtabs .q-tabs__content
    padding: 8px 0

  &__subtabs-item
    justify-content: left
    min-height: 36px !important
    .q-tab__content
      width: 100%

  &__subtabs,
  &__subtabs-item
    border-radius: 0 !important

  &__container
    max-height: 600px

  &__nothing-to-show
    padding: 16px

  &__search-field
    cursor: text
    min-width: 10em !important

  &__search
    border: 0
    outline: 0
    background: none
    color: inherit
    width: 1px !important // required when on narrow width window to not overflow the page
    height: 37px

.doc-api-entry
  padding: 16px
  color: #757575

  .doc-api-entry
    padding: 8px

  & + &
    border-top: 1px solid #ddd

  &__expand-btn
    margin-left: 4px

  &__item
    min-height: 25px
    & + &
      margin-top: 4px

  &__subitem
    padding: 4px 0 0 8px
    border-radius: 4px
    > div
      border: 1px solid rgba(0,0,0,.12) !important
      border-radius: inherit
    > div + div
      margin-top: 8px

  &__type
    line-height: 24px

  &__value
    color: #474747

  &--indent
    padding-left: 8px

  .doc-token
    margin: 4px
    display: inline-block

  &__added-in,
  &__pill
    font-size: 15px
    letter-spacing: .7px
    line-height: 1.4em

  &__added-in
    font-size: 12px

body.body--light
  .doc-api .doc-token
    background-color: #eee
    border: 1px solid rgba(0, 0, 0, .12)
    color: #474747
  .doc-api-entry__pill
    color: #fff
  .doc-api-entry__added-in
    color: $red-7
    border-color: $red
    background-color: $red-1

body.body--dark
  .doc-api .doc-token
    background-color: #050A14
    border: 1px solid rgba(255, 255, 255, .28)
    color: #cbcbcb
  .doc-api__search
    color: #cbcbcb
  .doc-api-entry
    color: #8FA8B2
    & + .doc-api-entry,
    &__subitem > div
      border-color: rgba(255, 255, 255, .28) !important
    &__value
      color: #cbcbcb
    &__example
      color: #00B4FF
      border-color: #00B4FF
    &__pill
      color: $dark
    &__added-in
      color: $red
      border-color: $red
      background-color: #050A14
</style>
