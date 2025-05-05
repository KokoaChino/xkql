<template>
    <div class="main-container">
        <Title/>
        <div class="container">
            <span style="color: darkslategrey">请输入 [0, {{ max }}] 之间的数字</span>
        </div>
        <div class="container" style="height: 30px; margin-bottom: 50px;gap: 10px">
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
    </div>
</template>



<script setup>
import { ElMessage } from "element-plus";
import { ref, onMounted } from "vue";
import { get, post } from "@/net/index.js";
import { useStore } from "@/stores/index.js";
import Title from '@/components/layout/Title.vue';
import { provide } from "vue";

provide("title", "猜数字游戏");

const store = useStore(), flag = ref(true)
const g = ref({
    list: []
}), cur = ref(), max = ref()

const play = async () => {
    g.value = await post("/api/guess-number/play", cur.value)
    if (g.value.list[0].includes('对')) {
        await game_over()
    }
}

const game_over = async () => {
    await post("/api/guess-number/historical-record/update", store.auth.user.username)
    flag.value = false
    ElMessage.success("游戏结束")
}

onMounted(async () => {
    max.value = await get("/api/guess-number/init")
});
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}

.main-container {
    width: 100%;
    max-width: 1680px;
    margin: 0 auto;
    box-sizing: border-box;
}
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 10px;
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
    width: 25%;
    margin: 0 auto;
    padding: 20px;
    box-sizing: border-box;
    line-height: 30px;
}
</style>
