<template>
    <Title/>
    <div class="content">
        <div class="item1">
            <table>
                <thead>
                    <tr>
                        <th style="border-left: none;">副词条名称</th>
                        <th>权重</th>
                        <th style="border-right: none;">数值列表</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(key, i) in keys" :key="i">
                        <td :style="set_style3(key)">{{ key }}</td>
                        <td :style="set_style3(key)">{{ Math.round(weigths[key] * 100) / 100 }}</td>
                        <td>
                            <el-radio-group style="gap: 10px" v-model="radios[i]" size="large">
                                <div v-for="x in map[key]" :key="x">
                                    <el-radio-button :label="x" :value="x" @mousedown="add_list(key, x)"/>
                                </div>
                            </el-radio-group>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="item2">
            <div>
                <table style="width: 100%;">
                    <thead>
                        <tr>
                            <th style="border-left: none;width: 30%;">角色名称</th>
                            <th style="border-right: none;width: 70%">
                                <el-select v-model="name" placeholder="Select" size="large" style="width: 100%;">
                                    <el-option v-for="item in names" :key="item.value"
                                               :label="item.label" :value="item.value"/>
                                </el-select>
                            </th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div style="width: 100%; display: flex;flex-direction: column; height: 100%;position: relative;">
                <table style="border-bottom: none;">
                    <thead style="border: 2px solid black;">
                        <tr>
                            <th style="border-left: none;">
                                <el-select v-model="main" placeholder="Select" size="large" style="width: 100%;">
                                    <el-option v-for="item in mains[cost]" :key="item.value"
                                               :label="item.label" :value="item.value"/>
                                </el-select>
                            </th>
                            <th style="border-right: none;">
                                <el-select v-model="cost" placeholder="Select" size="large" style="width: 100%;">
                                    <el-option v-for="item in costs" :key="item.value"
                                               :label="item.label" :value="item.value"/>
                                </el-select>
                            </th>
                        </tr>
                    </thead>
                    <tbody style="border: 2px solid black;">
                        <tr v-for="(key, i) in list" :key="i">
                            <td style="width: 70%">
                                <div class="container" :style="set_style1(key[0])">
                                    <div>{{ key[0] }}</div>
                                    <div>{{ key[0].includes('固定') || key[0] === '' ? key[1] : key[1] + '%' }}</div>
                                </div>
                            </td>
                            <td :style="set_style2(key[0])">
                                {{ key[0] !== '' ? percent[key[0]] + '%' : '' }}
                            </td>
                        </tr>
                        <tr style="border-top: 2px solid black;">
                            <td v-if="list[0][0]" colspan="2" :style="set_style4(percent['总得分'])">
                                {{ percent["总得分"] }}
                            </td>
                            <td v-else colspan="2"></td>
                        </tr>
                    </tbody>
                </table>
                <div class="buttons">
                    <div style="flex: 1;display: flex;justify-content: center;align-items: center;">
                        <el-button class="bt" type="danger" plain @click="refill">重置</el-button>
                    </div>
                    <div style="flex: 1;display: flex;justify-content: center;align-items: center;">
                        <el-button class="bt" type="success" plain @click="add_echo">添加</el-button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>



<script setup>
import Title from '@/components/module/Title.vue';
import { ref, provide, onMounted, watch } from "vue";
import { get, post, POST } from "@/net";
import { ElMessage } from "element-plus";
import { useStore } from "@/stores/index.js";

provide("title", "声骸评分系统");
const store = useStore()

const keys = ref([
    "固定攻击", "固定生命", "固定防御", "百分比攻击", "百分比生命", "百分比防御",
    "暴击率", "暴击伤害", "共鸣效率", "普攻伤害加成", "重击伤害加成", "共鸣技能伤害加成", "共鸣解放伤害加成"
])
const map = ref({
    "固定攻击": [],
    "固定生命": [],
    "固定防御": [],
    "百分比攻击": [],
    "百分比生命": [],
    "百分比防御": [],
    "暴击率": [],
    "暴击伤害": [],
    "共鸣效率": [],
    "普攻伤害加成": [],
    "重击伤害加成": [],
    "共鸣技能伤害加成": [],
    "共鸣解放伤害加成": [],
})
const weigths = ref({
    "固定攻击": 0,
    "固定生命": 0,
    "固定防御": 0,
    "百分比攻击": 0,
    "百分比生命": 0,
    "百分比防御": 0,
    "暴击率": 0,
    "暴击伤害": 0,
    "共鸣效率": 0,
    "普攻伤害加成": 0,
    "重击伤害加成": 0,
    "共鸣技能伤害加成": 0,
    "共鸣解放伤害加成": 0,
})
const percent = ref({
    "总得分": 0
})
const names = [
    { value: '长离', label: '长离' },
], name = ref("长离")
const costs = [
    { value: 1, label: 1 },
    { value: 3, label: 3 },
    { value: 4, label: 4 },
], cost = ref(4)
const mains = {
    1: [
        { value: '百分比攻击 18%', label: '百分比攻击 18%' },
        { value: '百分比生命 22.8%', label: '百分比生命 22.8%' },
        { value: '百分比防御 18%', label: '百分比防御 18%' },
    ],
    3: [
        { value: '属性伤害加成 30%', label: '属性伤害加成 30%' },
        { value: '共鸣效率 32%', label: '共鸣效率 32%' },
        { value: '百分比攻击 30%', label: '百分比攻击 30%' },
        { value: '百分比生命 30%', label: '百分比生命 30%' },
        { value: '百分比防御 38%', label: '百分比防御 38%' },
    ],
    4: [
        { value: '暴击率 22%', label: '暴击率 22%' },
        { value: '暴击伤害 44%', label: '暴击伤害 44%' },
        { value: '治疗效果加成 26.4%', label: '治疗效果加成 26.4%' },
        { value: '百分比攻击 33%', label: '百分比攻击 33%' },
        { value: '百分比生命 33%', label: '百分比生命 33%' },
        { value: '百分比防御 41.8%', label: '百分比防御 41.8%' },
    ]
}, main = ref('暴击率 22%')
const radios = ref(new Array(keys.value.length).fill(0)), data = ref()
const list = ref([['', ''], ['', ''], ['', ''], ['', ''], ['', '']]), len = ref(0)

watch(cost, (newVal) => {
    main.value = mains[newVal][0].value
});

const add_list = async (key, x) => {
    for (let i = 0; i < 5; i++) {
        if (list.value[i][0] === key) {
            if (x === 0) {
                list.value[i] = ['', '']
                len.value--
            } else list.value[i][1] = x
            percent.value = await POST("/echo-scoring-system/get-percent", {name: name.value, values: JSON.stringify(to_map())})
            list.value.sort((a, b) => {
                if (a[0] === '') return 1;
                if (b[0] === '') return -1;
                return percent.value[b[0]] - percent.value[a[0]]
            })
            return
        }
    }
    if (x === 0) return
    if (len.value === 5) {
        ElMessage.warning("声骸副词条个数不合法！");
        let index = keys.value.indexOf(key)
        setTimeout(() => radios.value[index] = 0, 250);
    } else {
        list.value[len.value++] = [key, x]
        percent.value = await POST("/echo-scoring-system/get-percent", {name: name.value, values: JSON.stringify(to_map())})
        list.value.sort((a, b) => {
            if (a[0] === '') return 1;
            if (b[0] === '') return -1;
            return percent.value[b[0]] - percent.value[a[0]]
        })
    }
}

function set_color(key) {
    let color, w = weigths.value[key]
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

function refill() {
    radios.value.fill(0)
    list.value = [['', ''], ['', ''], ['', ''], ['', ''], ['', '']]
    len.value = 0
    percent.value = {
        "总得分": 0
    }
}

async function add_echo() {
    if (len.value < 5) {
        ElMessage.warning("声骸副词条个数不合法！");
        return
    }
    console.log(name.value in Object.keys(data.value))
    if (name.value in Object.keys(data.value)) {
        let len = 0, sum = 0
        for (let item of data.value[name.value]) {
            if (item.main !== '') {
                len++
                sum += Number(item.cost)
            }
        }
        if (len === 5) {
            ElMessage.warning("该角色的声骸列表已满！");
            return
        }
        console.log(sum, cost.value)
        if (sum + cost.value > 12) {
            ElMessage.warning("该角色声骸的 Cost 不合法！");
            return
        }
    }
    let echo = []
    for (let item of list.value) {
        echo.push([item[0], item[1], percent.value[item[0]]])
    }
    let echoImpl = {
        main: main.value,
        cost: cost.value,
        echo: echo,
        score: percent.value['总得分']
    }
    data.value = await POST("/echo-scoring-system/add-data", {
        name: name.value,
        username: store.auth.user.username,
        json: JSON.stringify(echoImpl)
    })
    ElMessage.success("添加成功！");
}

const set_style1 = (key) => {
    let res = "display: flex; justify-content: space-between;"
    res += set_color(key)
    return res
}

const set_style2 = (key) => {
    let res = "width: 30%; text-align: center;"
    res += set_color(key)
    return res
}

const set_style3 = (key) => {
    let res = "text-align: center;"
    res += set_color(key)
    return res
}

const set_style4 = (w) => {
    let res = "text-align: center;", color
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
    return res + `color: ${ color }`
}

const to_map = () => {
    let map = {}
    for (let i = 0; i < 5; i++) {
        if (list.value[i][0] !== '')
            map[list.value[i][0]] = list.value[i][1]
    }
    return map
}

onMounted(async () => {
    map.value = await get("/echo-scoring-system/get-values")
    keys.value.forEach(key => {
        map.value[key].unshift(0);
        map.value[key].sort((a, b) => a - b);
    })
    weigths.value = await post("/echo-scoring-system/get-weigths", name.value)
    data.value = await post("/echo-scoring-system/get-data", store.auth.user.username)
})
</script>



<style scoped>
.content {
    width: 95%;
    margin: auto;
    display: flex;
}
.content .item1 {
    flex: 2;
    border: 2px solid black;
    border-right: none;
    box-sizing: border-box;
}
.content .item2 {
    flex: 1;
    border: 2px solid black;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
}

.content .item1 table {
    width: 100%;
    border-collapse: collapse;
}
.content .item1 td {
    padding: 10px;
    border: 1px solid #ccc;
}
.content th {
    padding: 10px;
    border: 2px solid black;
    border-top: none;
    height: 50px;
}
.content .item2 table {
    width: 70%;
    margin: auto;
    align-items: center;
    border-collapse: collapse;
}
.content .item2 td {
    padding: 10px;
    height: 30px;
    border: 1px solid #493939;
}

.buttons {
    height: 100px;
    width: 70%;
    margin: auto;
    position: absolute;
    left: 15%;
    top: 74.5%;
    display: flex;
}
.bt {
    width: 50%;
    height: 50%;
}
</style>
