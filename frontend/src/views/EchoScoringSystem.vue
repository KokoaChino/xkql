<template>
    <Title/>
    <div class="content">
        <div class="head">
            <div class="a">
                <span>角色</span>
            </div>
            <div class="b" @mouseenter="change_a" @mouseleave="change_b" @click="add_echo">
                <span>声骸列表</span>
            </div>
        </div>
        <div class="items">
            <div class="item" v-for="(name, index) in keys" :key="index">
                <div class="head">
                    <div class="img">
                        <div class="overlay">
                            {{ name }}
                        </div>
                    </div>
                    <div class="score">
                        <span :style="set_colorbyw(get_total(name))">
                            {{ get_total(name) }}
                        </span>
                    </div>
                </div>
                <div class="echo" v-for="(echos, index) in data[name]" :key="index">
                    <table>
                        <thead>
                        <tr>
                            <th>
                                {{ echos['main'] }}
                            </th>
                            <th style="border-left: 2px solid black">
                                {{ echos['cost'] }}
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(item, i) in echos['echo']" :key="i">
                            <td style="width: 80%">
                                <div :style="set_style1(name, item[0])">
                                    <div>{{ item[0] }}</div>
                                    <div>{{ item[0].includes('固定') || item[0] === '' ? item[1] : item[1] + '%' }}</div>
                                </div>
                            </td>
                            <td :style="set_style2(name, item[0])">
                                {{ item[2] !== '' ? item[2] + '%' : '' }}
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" :style="set_style3(echos['score'])"
                                @mouseenter="(e) => change_c(e, echos['main'])"
                                @mouseleave="(e) => change_d(e, echos['score'], echos['main'])"
                                @click="del_echo(name, index, echos['main'])">
                                {{ echos['score'] === '' ? '' : Number(echos['score']).toFixed(1) }}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>



<script setup>
import Title from '@/components/module/Title.vue';
import { ref, provide, onMounted } from "vue";
import { post, POST } from "@/net/index.js";
import router from "@/router/index.js";
import { useStore } from "@/stores/index.js";

provide("title", "声骸评分系统");
const store = useStore()

const data = ref({}), weigths = ref({}), keys = ref([])

function change_a(e) {
    e.target.innerHTML = "<button style=\"width: 100%; height: 100%;font-size: 1em;color: green\" @click='add_echo'>添加声骸</button>"
    e.target.style.fontSize = '2em';
}
function change_b(e) {
    e.target.innerText = '声骸列表'
    e.target.style.fontSize = '2em';
}
function add_echo() {
    router.push('/echo-scoring-system/main')
}

function change_c(e, k) {
    if (k === '') return
    e.target.style.padding = "0"
    e.target.innerHTML = "<button style=\"width: 100%; height: 100%;color: red\">移除声骸</button>"
}
function change_d(e, txt, k) {
    if (k === '') return
    e.target.style.padding = "10px"
    e.target.innerText = txt === '' ? '' : Number(txt).toFixed(1)
}
async function del_echo(name, index, k) {
    if (k === '') return
    data.value = await POST("/echo-scoring-system/del-echo", {
        username: store.auth.user.username,
        name: name,
        index: index
    })
    keys.value = Object.keys(data.value)
    keys.value.sort((a, b) => get_total(b) - get_total(a))
}

function set_color(name, key) {
    if (key === '') return ''
    let color = '', w = weigths.value[name][key]
    if (w >= 15) {
        color = "red"
    } else if (w >= 10) {
        color = "orange"
    } else if (w >= 5) {
        color = "blue"
    } else if (w >= 2.5) {
        color = "green"
    } else {
        color = "gray"
    }
    return `color: ${ color }`
}

function set_colorbyw(w) {
    let color = ''
    if (w >= 80) {
        color = "red"
    } else if (w >= 70) {
        color = "orange"
    } else if (w >= 60) {
        color = "blue"
    } else if (w >= 50) {
        color = "green"
    } else {
        color = "gray"
    }
    return `color: ${ color }`
}

const set_style1 = (name, key) => {
    let res = "display: flex; justify-content: space-between;"
    res += set_color(name, key)
    return res
}

const set_style2 = (name, key) => {
    let res = "width: 20%; text-align: center;"
    res += set_color(name, key)
    return res
}
const set_style3 = (w) => {
    let res = "text-align: center;border-top: 2px solid black;"
    return res + set_colorbyw(w)
}

const get_total = (name) => {
    let sum = 0
    for (let item of data.value[name]) {
        if (item.score !== '') sum += Number(item.score)
    }
    return sum / 5
}

onMounted(async () => {
    data.value = await post("/echo-scoring-system/get-data", store.auth.user.username)
    keys.value = Object.keys(data.value)
    keys.value.sort((a, b) => get_total(b) - get_total(a))
    for (let key of Object.keys(data.value)) {
        weigths.value[key] = await post("/echo-scoring-system/get-weigths", key)
    }
})
</script>



<style scoped>
.content {
    width: 95%;
    margin: auto;
    display: flex;
    flex-direction: column;
}

.head {
    display: flex;
}
.head .a {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 1;
    border: 2px solid black;
    height: 100px;
    border-right: none;
}
.head .b {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 5;
    border: 2px solid black;
    height: 100px;
}
.head span {
    font-size: 2em;
}

.items {
    display: flex;
    flex-direction: column;
}
.items .item {
    display: flex;
}
.items .item .head {
    flex: 1;
    align-items: center;
    justify-content: center;
    display: flex;
    flex-direction: column;
    border: 2px solid black;
    border-top: none;
}
.items .item .echo {
    flex: 1;
    border-top: none;
}

.head .img {
    position: relative;
    flex: 5;
    width: 100%;
    height: 300px;
    box-sizing: border-box;
    background-image: url("/角色立绘/长离.png");
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
    overflow: hidden;
}
.head .img .overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    text-align: center;
    padding: 10px;
    opacity: 0;
    transition: opacity 0.3s ease;
}
.img:hover .overlay {
    opacity: 1;
}

.head .score {
    flex: 1;
    max-height: 50px;
    width: 100%;
    box-sizing: border-box;
    border-top: 2px solid black;
    display: grid;
    place-items: center;
    font-size: 14px;
}

.echo table {
    width: 100%;
    height: 100%;
    border-collapse: collapse;
    border-right: 2px solid black;
    border-bottom: 2px solid black;
}
.echo th {
    padding: 10px;
    box-sizing: border-box;
    height: 50px;
    border-bottom: 2px solid black;
}
.echo td {
    padding: 10px;
    box-sizing: border-box;
    height: 50px;
    border: 1px solid black;
    border-left: none;
}
</style>
