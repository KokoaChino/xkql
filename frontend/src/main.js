/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from "axios";
import { setupPersistedStore } from './stores/index.js';
import LazyLoad from './directives/lazyLoad'

const pinia = createPinia();
setupPersistedStore(pinia);


const app = createApp(App)

axios.defaults.baseURL = 'http://localhost:8081'

app.use(createPinia())
app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.directive('lazy', LazyLoad)
app.mount('#app')

router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    if (to.meta.icon) {
        let link = document.querySelector("link[rel='icon']");
        if (!link) {
            link = document.createElement('link');
            link.rel = 'icon';
            document.head.appendChild(link);
        }
        link.href = to.meta.icon;
    }
    next();
});
