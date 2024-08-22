<template>
    <h1 style="text-align: center">星开祈灵的工具箱</h1>
    <hr>
    <div style="display: flex">
        <div>
            <el-button @click="unsubscribe()" type="danger" plain>注销账户</el-button>
        </div>
        <div style="margin-left: auto">
            <el-button @click="logout()" type="danger" plain>退出登录</el-button>
        </div>
    </div>
    <br>
    <div style="display: grid; grid-template-columns: 1fr 1fr; grid-template-rows: 50px 50px; gap: 10px; height: auto; width: 50%; margin: 0 auto;">
        <button @click="guess_number" class="my-button">猜数字游戏</button>
        <button class="my-button"></button>
        <button class="my-button"></button>
        <button class="my-button"></button>
    </div>

</template>



<script setup>
import {GET, post, get} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";
import {ref} from "vue";

const store = useStore()

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


const unsubscribe = async () => {
    logout()
    await post("api/auth/unsubscribe", store.auth.user.username)
}



</script>



<style scoped>
.my-button {
    background-color: #3498db; /* 按钮背景色为蓝色 */
    color: white; /* 字体颜色为白色 */
    font-size: 16px; /* 字体大小 */
    padding: 10px 20px; /* 内边距 */
    border: 2px solid #2980b9; /* 添加蓝色边框 */
    border-radius: 12px; /* 圆角 */
    cursor: pointer; /* 鼠标悬停时显示为手形 */
    transition: background-color 0.3s, border-color 0.3s; /* 背景颜色和边框颜色过渡效果 */
    font-family: 'Arial', sans-serif; /* 使用更圆润的字体 */
}

.my-button:hover {
    background-color: #2980b9; /* 悬停时的背景色为深蓝色 */
    border-color: #1f5b9a; /* 悬停时边框颜色稍微变暗 */
}
</style>
