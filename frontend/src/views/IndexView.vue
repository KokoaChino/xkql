<template>
    <h1 style="text-align: center">星开祈灵的工具箱</h1>
    <hr>
    <div style="display: flex">
        <div style="margin-left: 10px;">
            <el-button @click="unsubscribe()" type="danger" plain>注销账户</el-button>
        </div>
        <div style="margin-left: auto; margin-right: 10px;">
            <el-button @click="logout()" type="danger" plain>退出登录</el-button>
        </div>
    </div>
    <br>
    <div style="display: grid; grid-template-columns: 1fr 1fr; grid-template-rows: 50px 50px; gap: 25px; height: auto; width: 50%; margin: 0 auto;">
        <button @click="guess_number" class="my-button">猜数字游戏</button>
        <button @click="character_map" class="my-button">角色图</button>
        <button @click="test" class="my-button">test</button>
        <button class="my-button"></button>
    </div>
</template>



<script setup>
import {GET, post, get} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";
import {onMounted, ref} from "vue";

const store = useStore()
const list = ref([])
const urls = ref([])

const logout = () => {
    GET('/api/auth/logout', (message) => {
        ElMessage.success(message)
        store.auth.user = null
        router.push('/')
    })
}

const guess_number = () => {
    router.push('/guess-number')
}

const character_map = () => {
    router.push('/character-map')
}

const unsubscribe = async () => {
    logout()
    await post("api/auth/unsubscribe", store.auth.user.username)
}

const test = async () => {

}
</script>



<style scoped>
.my-button {
    background-color: #3498db; /* 蓝色背景 */
    color: #ffffff; /* 字体颜色为白色 */
    font-size: 16px; /* 字体大小 */
    padding: 12px 24px; /* 内边距 */
    border: 2px solid #2980b9; /* 深蓝色边框 */
    border-radius: 8px; /* 圆角 */
    cursor: pointer; /* 鼠标悬停时显示为手形 */
    transition: all 0.3s ease-in-out; /* 过渡效果包括所有属性 */
    font-family: 'Comic Sans MS', cursive, sans-serif; /* 使用更具趣味性的字体 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
}
.my-button:hover {
    transform: scale(1.05); /* 鼠标悬停时按钮稍微放大 */
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3); /* 悬停时阴影效果更显著 */
}
</style>
