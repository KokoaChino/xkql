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
import { setupPersistedStore, useStore } from './stores/index.js';
import LazyLoad from './directives/lazyLoad'
import { _GET } from "@/net/index.js";

const pinia = createPinia();
setupPersistedStore(pinia);


const app = createApp(App)

axios.defaults.baseURL = 'http://8.138.214.176:8081'

app.use(createPinia())
app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.directive('lazy', LazyLoad)
app.mount('#app')

const store = useStore();
if (store.auth.user) {
    _GET('/api/user/me',
        (message) => store.auth.user = message,
        (errorMsg) => store.auth.user = null
    );
}

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
