<template>
    <h1 style="text-align: center">{{ characterName }}</h1>
    <div style="text-align: center">
        <a :href="zipurl" :download="zipname">下载全部图片</a>
    </div>
    <hr>
    <div class="container">
        <div class="photo-card" v-for="(url, index) in urls" :key="index">
            <img :src="url" :alt="url">
            <div class="photo-info">
                <div class="photo-title">{{ list[index][0] }}</div>
                <div class="photo-time">{{ list[index][1] }}</div>
                <div style="height: 10px"></div>
                <a class="download-btn" :href="url" :download="list[index][2]">
                    下载图片
                </a>
                <div style="height: 10px"></div>
            </div>
        </div>
    </div>
</template>

<script setup>
import {get, post} from "@/net";
import {ref, onMounted} from 'vue';

const characterName = ref()
const zipurl = ref(), zipname = ref()
const list = ref([])
const urls = ref([])

onMounted(async () => {
    characterName.value = await post("character-map/getName")
    list.value = await post("character-map/getCharacterMapFiles", characterName.value)
    for (let i = 0; i < list.value.length; i++) {
        urls.value.push('/角色图/' + characterName.value + '/' + list.value[i][0] + '.jpg');
    }
    zipurl.value = '/角色图压缩/' + characterName.value + '.zip'
    zipname.value = characterName.value + '.zip'
})
</script>

<style scoped>
body {
    margin: 0;
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
}
.container {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    padding: 16px;
}
.photo-card {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    overflow: hidden;
    text-align: center;
}
.photo-card img {
    width: 100%;
    height: auto;
    display: block;
}
.photo-info {
    padding: 8px;
}
.photo-title {
    font-size: 16px;
    margin: 8px 0 4px;
}
.photo-time {
    font-size: 14px;
    color: #666;
}
.download-btn {
    margin-top: 8px;
    padding: 8px 16px;
    font-size: 14px;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    text-decoration: none;
}
.download-btn:hover {
    background-color: #0056b3;
}
</style>
