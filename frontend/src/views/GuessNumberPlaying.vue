<template>
    <div class="card-sub">
        <div class="text">猜数字游戏</div>
    </div>
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
import { ElMessage } from "element-plus";
import { ref, onMounted } from "vue";
import { get, post } from "@/net";
import { useStore } from "@/stores";

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
* {
    margin: 0;
    padding: 0;
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
    display: flex;
    justify-content: center;
    align-items: center;
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
    width: 25%;
    margin: 0 auto;
    padding: 20px;
    box-sizing: border-box;
    line-height: 30px;
}
</style>
