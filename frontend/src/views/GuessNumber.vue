<template>
<h1 style="text-align: center">猜数字游戏</h1>
<hr>
    <div class="button-container">
        <button class="button button-1" @click="game_start">开始游戏</button>
        <label>
            设置游戏最大值：
            <select v-model="max" style="width: 90px">
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
import { post } from "@/net";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const max = ref()

const game_start = async () => {
    if (max.value) {
        await post("/guess-number/game/start", max.value)
        await router.push('/guess-number/playing')
    }
    else ElMessage.warning("请先选择游戏最大值")
}

const historical_record = async () => {
    await router.push('/guess-number/historical-record')
}




</script>



<style scoped>
.button-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px; /* 按钮之间的间隔 */
    margin-top: 50px; /* 顶部间隔 */
}

/* 按钮样式 */
.button {
    width: 200px; /* 按钮宽度 */
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    transition: background-color 0.3s ease;
}

/* 不同高度的按钮 */
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

/* 按钮悬停效果 */
.button:hover {
    opacity: 0.9;
}
</style>
