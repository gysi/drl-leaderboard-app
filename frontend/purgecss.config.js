module.exports = {
  content: ["./dist/spa/assets/*.js"],
  css: ["./dist/spa/assets/*.css"],
  output: ["./dist/spa/assets/"],
  fontFace: true,
  keyframes: true,
  variables: false,
  rejected: true,
  safelist: {
    standard: [
      /fixed-.*/,
      /absolute-.*/,
      /bg-.*/,
      /q-btn.*/,
      /q-toggle.*/,
      /q-icon.*/,
      /q-item__section.*/,
      /q-field__control.*/,
      /q-field--filled.*/,
      /q-table--horizontal-separator.*/,
      /q-card__section--vert.*/,
      /q-separator--.*/,
      /q-tabs--.*/,
      /q-slider--.*/,
      /q-drawer.*/,
      /q-batch.*/,
      /q-table__sort.*/,
      /q-dialog__inner*/,
      /map-.*/,
      /.*slotmachine.*/,
      /text-.*/,
      /justify-.*/,
      /fi-.*/],
    greedy: [
      /slotmachine$/
    ],
  }
};
