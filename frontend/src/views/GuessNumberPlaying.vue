<template>
    <h1 style="text-align: center">猜数字游戏</h1>
    <hr>
    <div class="container">
        <span style="color: darkslategrey">请输入 [0, {{ max }}] 之间的数字</span>
    </div>
    <div class="container" style="height: 3vh; margin-bottom: 5vh;gap: 10px">
        <input v-model="cur" type="number" style="height: 35px" placeholder="请输入你要猜的数字">
        <el-button style="height: 40px; " type="success" @click="play" :disabled="!flag">
            提交
        </el-button>
    </div>
    <el-divider>
        <span style="color: grey;font-size: 15px">历史记录</span>
    </el-divider>
    <div style="width: 100%">
        <div class="box">
            <ul style="list-style: none; color: darkblue" v-for="s in g.list">
                <li>
                    {{ s }}
                </li>
            </ul>
        </div>
    </div>
</template>



<script setup>
import {ElMessage} from "element-plus";
import { ref, onMounted } from "vue";
import { get, post } from "@/net";
import {useStore} from "@/stores";

const store = useStore(), flag = ref(true)
const g = ref({
    list: []
}), cur = ref(), max = ref()

const play = async () => {
    g.value = await post("/guess-number/play", cur.value)
    if (g.value.list[0].includes('对')) {
        await game_over()
    }
}

const game_over = async () => {
    await post("/guess-number/historical-record/update", store.auth.user.username)
    flag.value = false
    ElMessage.success("游戏结束")
}

onMounted(async () => {
    max.value = await get("/guess-number/init")
});



</script>



<style scoped>
.container {
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中（可选） */
    height: 5vh;
}
input[type="text"] {
    padding: 10px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
}
button {
    padding: 10px 20px;
    font-size: 16px;
    border: none;
    border-radius: 4px;
    background-color: #007bff;
    color: #fff;
    cursor: pointer;
}
button:hover {
    background-color: #0056b3;
}
.box {
    width: 50%; /* 设置宽度为屏幕的 50% */
    margin: 0 auto; /* 左右外边距自动，以居中对齐 */
    padding: 20px;
    box-sizing: border-box; /* 包括内边距和边框在内的宽度 */
}
.no {
    cursor: not-allowed;
}
</style>
