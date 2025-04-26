<template>
    <Title/>
    <div class="button-container">
        <button class="button button-1" @click="game_start">开始游戏</button>
        <label>
            设置游戏最大值：
            <select v-model="max" style="width: 90px;height: 30px;">
                <option selected>10</option>
                <option>100</option>
                <option>1000</option>
                <option>10000</option>
                <option>100000</option>
                <option>1000000</option>
                <option>10000000</option>
                <option>100000000</option>
            </select>
        </label>
        <button class="button button-2" @click="historical_record">历史记录</button>
    </div>
</template>



<script setup>
import { ref } from "vue";
import { post } from "@/net/index.js";
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import Title from '@/components/layout/Title.vue';
import { provide } from "vue";

provide("title", "猜数字游戏");

const max = ref()

const game_start = async () => {
    if (max.value) {
        await post("/api/guess-number/game/start", max.value)
        await router.push('/guess-number/playing')
    }
    else ElMessage.warning("请先选择游戏最大值")
}

const historical_record = async () => {
    await router.push('/guess-number/historical-record')
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
    background-color: #4CAF50;
}
.button-2 {
    height: 50px;
    width: 220px;
    background-color: #2196F3;
}
.button:hover {
    opacity: 0.9;
    transform: scale(1.05);
}
</style>
