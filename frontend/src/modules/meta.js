import {useMeta as useQuasarMeta, createMetaMixin as createQuasarMetaMixin } from "quasar";

function removeStaticMetaDescription() {
  const meta = document.querySelector('meta[name="description"][data-static="true"]')
  if (meta) {
    meta.parentNode.removeChild(meta)
  }
}

function useMeta(metaDefinition){
  removeStaticMetaDescription()
  useQuasarMeta(metaDefinition)
}

function createMetaMixin(metaDefinition) {
  removeStaticMetaDescription()
  return createQuasarMetaMixin(metaDefinition)
}

export { useMeta, createMetaMixin }
