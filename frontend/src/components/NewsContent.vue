<template>
  <q-card bordered style="max-height: 100%; display: flex; flex-direction: column">
    <q-banner inline-actions class="text-white bg-red-6" style="min-height: fit-content">
      <div class="text-h6">News</div>
      <q-btn dense flat icon="close" v-close-popup style="position: absolute; top: 0; right: 0">
        <q-tooltip class="bg-white text-primary">Close</q-tooltip>
      </q-btn>
    </q-banner>
    <q-separator />
    <q-list ref="scrollTargetRef" class="scroll" style="overflow-y: auto">
      <q-infinite-scroll
        ref="infiniteScrollRef"
        @load="loadNextBatch"
        :offset="500"
        :scroll-target="scrollTargetRef ? scrollTargetRef.value : null">
        <template v-for="news in visibleNewsComponents" :key="news.name">
          <component :is="news.component" v-if="news.component" />
          <div v-else>Loading {{ news.name }}...</div>
          <q-separator />
        </template>
        <template v-slot:loading>
          <div class="text-center news-content">
            <q-spinner-dots size="40px" />
          </div>
        </template>
      </q-infinite-scroll>
    </q-list>
  </q-card>
</template>

<script setup>
import { ref, markRaw, onMounted, onUpdated, nextTick } from 'vue';

console.log("NewsContent2.vue loaded");

const infiniteScrollRef = ref(null);
const scrollTargetRef = ref(null);

const modules = import.meta.glob('./news/*.vue', { eager: false });

let allNewsComponents = Object.entries(modules).map(([path, loader]) => ({
  name: path.split('/').pop().replace('.vue', ''),
  loader,
  component: null,
}));

allNewsComponents = allNewsComponents.sort((a, b) => b.name.localeCompare(a.name));

const visibleNewsComponents = ref([]);
const batchSize = 5;

const loadNextBatch = async (index, done) => {
  console.log(`loadNextBatch triggered for index: ${index}`);

  const startIndex = (index - 1) * batchSize;
  const endIndex = Math.min(startIndex + batchSize, allNewsComponents.length);

  if (startIndex >= allNewsComponents.length) {
    console.log('All news components loaded');
    done(true); // Stop further loading
    return;
  }

  const batch = allNewsComponents.slice(startIndex, endIndex);
  for (const news of batch) {
    const resolved = await news.loader();
    news.component = markRaw(resolved.default || resolved);
  }

  visibleNewsComponents.value.push(...batch);

  done(false);
};

const initializeScroll = async () => {
  console.log("Initializing scroll target...");
  await nextTick(); // Ensure DOM is updated

  if (scrollTargetRef.value && infiniteScrollRef.value) {
    infiniteScrollRef.value.updateScrollTarget(scrollTargetRef.value); // Update target for scrolling
    infiniteScrollRef.value.resume(); // Resume listening for scroll events
  } else {
    console.error("Scroll target or infinite scroll reference is not defined.");
  }
};

onMounted(async () => {
  await initializeScroll();
});

onUpdated(async () => {
  // Re-initialize scroll target if the dialog re-renders
  await initializeScroll();
});
</script>
