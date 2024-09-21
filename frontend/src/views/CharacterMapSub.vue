<template>
    <div class="card-sub">
        <div class="text">{{ characterName }}</div>
    </div>
    <div class="container">
        <div class="photo-card" v-for="(url, index) in urls" :key="index">
            <img :src="url" :alt="url" @click="move(url)">
            <div class="photo-info">
                <div class="photo-title">{{ list[index][0] }}</div>
                <div class="photo-time">{{ list[index][1] }}</div>
                <div style="height: 10px"></div>
                <a class="button" :href="url" :download="list[index][2]">
                    <svg
                        stroke-linejoin="round"
                        stroke-linecap="round"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.5"
                        viewBox="0 0 24 24"
                        height="40"
                        width="40"
                        class="button__icon"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path fill="none" d="M0 0h24v24H0z" stroke="none"></path>
                        <path d="M4 17v2a2 2 0 0 0 2 2h12a2 2 0 0 0 2 -2v-2"></path>
                        <path d="M7 11l5 5l5 -5"></path>
                        <path d="M12 4l0 12"></path>
                    </svg>
                    <span class="button__text">下载图片</span>
                </a>
                <div style="height: 10px"></div>
            </div>
        </div>
    </div>
</template>



<script setup>
import { post } from "@/net";
import { ref, onMounted } from 'vue';

const characterName = ref()
const list = ref([])
const urls = ref([])

const move = (url) => {
    const newUrl = 'http://localhost:5173' + url
    window.open(newUrl, '');
}

onMounted(async () => {
    characterName.value = await post("character-map/getName")
    list.value = await post("character-map/getCharacterMapFiles", characterName.value)
    for (let i = 0; i < list.value.length; i++) {
        urls.value.push('/角色图/' + characterName.value + '/' + list.value[i][0] + '.jpg');
    }
})
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}
body {
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
}

.card-sub {
    width: 100%;
    height: 100px;
    border: solid 1px #202222;
    background-size: 20px 20px;
    background: white;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    flex-direction: column;
    color: #fff;
    margin-bottom: 20px;
}
.card-sub .text {
    font-weight: bolder;
    font-size: 3rem;
    background: black;
    background-clip: text;
    color: transparent;
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

.button {
    width: 95px;
    height: 25px;
    text-decoration: none;
    margin: auto;
    line-height: 1;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 0.35em;
    padding: 0.75em 1.25em 0.75em 1em;
    color: #fff;
    border: 1px solid transparent;
    font-weight: 700;
    border-radius: 2em;
    font-size: 1rem;
    box-shadow: 0 0.7em 1.5em -0.5em hsla(249, 62%, 51%, 0.745);
    transition: transform 0.3s;
    background: transparent linear-gradient(
        90deg,
        rgba(77, 54, 208, 1) 0%,
        rgba(132, 116, 254, 1) 100%
    );
}
.button__icon {
    width: 1.5em;
    height: 1.5em;
}
.button:hover {
    border-color: #f4f5f2;
}
.button:active {
    transform: scale(0.98);
    box-shadow: 0 0.5em 1.5em -0.5em hsla(249, 62%, 51%, 0.745);
}
</style>
