// vite.config.js
import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "file:///C:/Users/Kokoa_Chino/Desktop/Learning%20materials/Java/Java_Code/Items/xkql/frontend/node_modules/vite/dist/node/index.js";
import vue from "file:///C:/Users/Kokoa_Chino/Desktop/Learning%20materials/Java/Java_Code/Items/xkql/frontend/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import AutoImport from "file:///C:/Users/Kokoa_Chino/Desktop/Learning%20materials/Java/Java_Code/Items/xkql/frontend/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///C:/Users/Kokoa_Chino/Desktop/Learning%20materials/Java/Java_Code/Items/xkql/frontend/node_modules/unplugin-vue-components/dist/vite.js";
import { ElementPlusResolver } from "file:///C:/Users/Kokoa_Chino/Desktop/Learning%20materials/Java/Java_Code/Items/xkql/frontend/node_modules/unplugin-vue-components/dist/resolvers.js";

var __vite_injected_original_import_meta_url = "file:///C:/Users/Kokoa_Chino/Desktop/Learning%20materials/Java/Java_Code/Items/xkql/frontend/vite.config.js";
var vite_config_default = defineConfig({
    plugins: [
        vue(),
        AutoImport({
            resolvers: [ElementPlusResolver()]
        }),
        Components({
            resolvers: [ElementPlusResolver()]
        })
    ],
    resolve: {
        alias: {
            "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
        }
    }
});
export {
    vite_config_default as default
};
