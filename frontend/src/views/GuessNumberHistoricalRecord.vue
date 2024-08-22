<template>
    <h1 style="text-align: center">历史记录</h1>
    <hr>
    <table>
        <thead>
        <tr>
            <th>最大值</th>
            <th>最小次数</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>10</td>
            <td v-if="a[0]">{{ a[0] }}</td>
            <td v-if="!a[0]" style="color: red">INF</td>
        </tr>
        <tr>
            <td>100</td>
            <td v-if="a[1]">{{ a[1] }}</td>
            <td v-if="!a[1]" style="color: red">INF</td>
        </tr>
        <tr>
            <td>1000</td>
            <td v-if="a[2]">{{ a[2] }}</td>
            <td v-if="!a[2]" style="color: red">INF</td>
        </tr>
        <tr>
            <td>10000</td>
            <td v-if="a[3]">{{ a[3] }}</td>
            <td v-if="!a[3]" style="color: red">INF</td>
        </tr>
        <tr>
            <td>100000</td>
            <td v-if="a[4]">{{ a[4] }}</td>
            <td v-if="!a[4]" style="color: red">INF</td>
        </tr>
        <tr>
            <td>1000000</td>
            <td v-if="a[5]">{{ a[5] }}</td>
            <td v-if="!a[5]" style="color: red">INF</td>
        </tr>
        <tr>
            <td>10000000</td>
            <td v-if="a[6]">{{ a[6] }}</td>
            <td v-if="!a[6]" style="color: red">INF</td>
        </tr>
        <tr>
            <td>100000000</td>
            <td v-if="a[7]">{{ a[7] }}</td>
            <td v-if="!a[7]" style="color: red">INF</td>
        </tr>
        </tbody>
    </table>
    <div class="button-container">
        <button class="my-button" @click="reset">清空历史记录</button>
    </div>
</template>



<script setup>
import { ref, onMounted } from "vue";
import { get, post } from "@/net";
import {useStore} from "@/stores";

const a = ref([0, 0, 0, 0, 0, 0, 0, 0])
const store = useStore()

const reset = async () => {
    a.value = [0, 0, 0, 0, 0, 0, 0, 0]
    await post("/guess-number/historical-record/reset", store.auth.user.username)
}

onMounted(async () => {
    a.value = await post("/guess-number/historical-record", store.auth.user.username)
})

</script>



<style scoped>
table {
    border-collapse: collapse;
    margin: 0 auto; /* Center the table horizontally */
    width: 50%; /* Adjust width as needed */
}
th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: center; /* Center the text inside table cells */
}
th {
    background-color: #f4f4f4;
}
.button-container {
    display: flex;
    justify-content: center; /* 水平居中 */
    margin-top: 50px; /* 添加顶部边距 */
}
.my-button {
    padding: 10px 20px; /* 按钮内边距 */
    background-color: #007BFF; /* 按钮背景色 */
    color: white; /* 文字颜色 */
    border: none; /* 无边框 */
    border-radius: 5px; /* 圆角 */
    cursor: pointer; /* 鼠标悬停时变成手形 */
    font-size: 16px; /* 字体大小 */
}
.my-button:hover {
    background-color: #0056b3; /* 鼠标悬停时背景色 */
}
</style>
