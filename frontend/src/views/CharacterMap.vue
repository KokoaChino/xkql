<template>
    <h1 style="text-align: center">角色图</h1>
    <hr>
    <div class="container">
        <div class="photo-card" v-for="(url, index) in urls" :key="index">
            <img :src="url" :alt="url" @click="move(list[index][0])">
            <div class="overlay">{{ list[index][0] }}</div>
        </div>
    </div>
</template>

<script setup>
import { get, post } from "@/net";
import router from "@/router/index.js";
import { ref, onMounted } from 'vue';

const list = ref([])
const urls = ref([])

const move = async (name) => {
    await post("/character-map/setName", name)
    await router.push('/character-map/sub')
}

onMounted( async () => {
    list.value = await get("/character-map/getCharacterMapCatalogue");
    for (let i = 0; i < list.value.length; i++) {
        urls.value.push('/角色图/' + list.value[i][0] + '/' + list.value[i][1]);
    }
})
</script>

<style scoped>
.container {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    padding: 16px;
}

.photo-card {
    position: relative;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    text-align: center;
}

.photo-card img {
    width: 100%;
    height: auto;
    display: block;
}

.overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    text-align: center;
    padding: 10px;
    box-sizing: border-box;
    transition: opacity 0.3s ease;
    opacity: 0;
}

.photo-card:hover .overlay {
    opacity: 1;
}

body {
    margin: 0;
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
}
</style>
