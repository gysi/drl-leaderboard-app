import { ref, onMounted, onUnmounted } from 'vue'

export function useSleep() {
  const sleep = async (milliseconds) => {
    return new Promise(resolve => setTimeout(resolve, milliseconds))
  }

  return { sleep }
}
