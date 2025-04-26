<template>
    <Title/>
    <div class="button-container">
        <button class="button button-1" @click="game_start">开始游戏</button>
        <button class="button button-2" @click="set_game">游戏库</button>
        <button class="button button-3" @click="historical_record">历史记录</button>
    </div>
</template>



<script setup>
import Title from '@/components/layout/Title.vue';
import { provide } from "vue";
import router from "@/router/index.js";
import { useStore } from "@/stores/index.js";
import {ElMessage} from "element-plus";

provide("title", "连连看游戏");
const store = useStore(), administrators = new Set(["星开祈灵"])

const game_start = async () => {
    await router.push('/link-game/game-start')
}

const set_game = async () => {
    if (!administrators.has(store.auth.user.username)) {
        ElMessage.error("该功能仅对管理员开放！")
        return;
    }
    await router.push('/link-game/set-game')
}

const historical_record = async () => {
    await router.push('/link-game/historical-record')
}
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}

.button-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    margin-top: 50px;
}
.button {
    width: 200px;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    transition: background-color 0.3s ease;
}
.button-1 {
    height: 50px;
    width: 220px;
    background-color: #67C23A;
}
.button-2 {
    height: 50px;
    width: 220px;
    background-color: #E6A23C;
}
.button-3 {
    height: 50px;
    width: 220px;
    background-color: #409EFF;
}
.button:hover {
    opacity: 0.9;
    transform: scale(1.05);
}
</style>
