<template>
    <Title/>
    <table>
        <thead>
        <tr style="color: indigo">
            <th>方阵大小</th>
            <th>最短用时</th>
        </tr>
        </thead>
        <tbody v-for="(x, index) in [4, 5, 6, 7, 8, 9, 10, 11, 12, 13]" :key="index">
        <tr>
            <td style="color: #333333">{{ x }}</td>
            <td style="color: forestgreen" v-if="a[index]">{{ a[index] }}</td>
            <td v-else style="color: red">INF</td>
        </tr>
        </tbody>
    </table>
    <button class="button" type="button" @click="reset">
        <span class="button__text">清空历史记录</span>
        <span class="button__icon"><svg class="svg" height="512" viewBox="0 0 512 512" width="512" xmlns="http://www.w3.org/2000/svg"><title></title><path d="M112,112l20,320c.95,18.49,14.4,32,32,32H348c17.67,0,30.87-13.51,32-32l20-320" style="fill:none;stroke:#fff;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px"></path><line style="stroke:#fff;stroke-linecap:round;stroke-miterlimit:10;stroke-width:32px" x1="80" x2="432" y1="112" y2="112"></line><path d="M192,112V72h0a23.93,23.93,0,0,1,24-24h80a23.93,23.93,0,0,1,24,24h0v40" style="fill:none;stroke:#fff;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px"></path><line style="fill:none;stroke:#fff;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px" x1="256" x2="256" y1="176" y2="400"></line><line style="fill:none;stroke:#fff;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px" x1="184" x2="192" y1="176" y2="400"></line><line style="fill:none;stroke:#fff;stroke-linecap:round;stroke-linejoin:round;stroke-width:32px" x1="328" x2="320" y1="176" y2="400"></line></svg></span>
    </button>
</template>



<script setup>
import { ref, onMounted } from "vue";
import { post } from "@/net";
import { useStore } from "@/stores";
import Title from '@/components/module/Title.vue';
import { provide } from "vue";

provide("title", "历史记录");

const a = ref([])
const store = useStore()

const reset = async () => {
    a.value = ["", "", "", "", "", "", "", "", "", ""]
    await post("/link-game/historical-record/reset", store.auth.user.username)
}

onMounted(async () => {
    a.value = await post("/link-game/historical-record", store.auth.user.username)
})
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}

table {
    border-collapse: collapse;
    margin: 0 auto;
    width: 50%;
}
th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: center;
}
th {
    background-color: #f4f4f4;
}

.button {
    position: relative;
    width: 150px;
    height: 40px;
    cursor: pointer;
    display: flex;
    align-items: center;
    border: 1px solid #cc0000;
    background-color: #e50000;
    overflow: hidden;
    margin: 50px auto;
}
.button, .button__icon, .button__text {
    transition: all 0.3s;
}
.button .button__text {
    transform: translateX(16px);
    color: #fff;
    font-weight: 400;
}
.button .button__icon {
    position: absolute;
    transform: translateX(109px);
    height: 100%;
    width: 39px;
    background-color: #cc0000;
    display: flex;
    align-items: center;
    justify-content: center;
}
.button .svg {
    width: 20px;
}
.button:hover {
    background: #cc0000;
}
.button:hover .button__text {
    color: transparent;
}
.button:hover .button__icon {
    width: 148px;
    transform: translateX(0);
}
.button:active .button__icon {
    background-color: #b20000;
}
.button:active {
    border: 1px solid #b20000;
}
</style>
