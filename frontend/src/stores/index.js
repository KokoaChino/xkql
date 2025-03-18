import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import piniaPersistedState from 'pinia-plugin-persistedstate';

export const useStore = defineStore('store', () => {
    const auth = reactive({
        user: null,
    })
    return { auth }
}, {
    persist: true // 持久化
})

export function setupPersistedStore(pinia) {
    pinia.use(piniaPersistedState);
}
