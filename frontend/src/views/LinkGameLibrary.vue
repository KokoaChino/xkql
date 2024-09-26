<template>
    <div @mouseup="is_down=false" @dragstart.prevent>
        <Title/>
        <div class="radio">
            <span style="margin-right: 30px">方阵大小</span>
            <el-radio-group v-model="n" size="large" v-for="(x, index) in [4, 5, 6, 7, 8, 9, 10, 11, 12, 13]" :key="index">
                <el-radio-button :label="x" :value="x"/>
            </el-radio-group>
        </div>
        <div class="select_color">
            <span style="margin-right: 30px">颜色样式</span>
            <button class="item" v-for="(x, index) in colors" :key="index"
                    :style="select_color(index)"
                    @mousedown="color = index">
            </button>
        </div>
        <h1 style="width: 100%; text-align: center; color: #606266">
            {{ p }} / {{ G.length - 1 }}
        </h1>
        <div class="grid">
            <div v-for="(row, rowIndex) in g" :key="rowIndex" class="row">
                <div v-for="(cell, cellIndex) in row" :key="cellIndex" class="cell"
                     :style="set_color(cell)"
                     @mousedown="set_path(rowIndex, cellIndex)"
                     @mouseup="switch_color(rowIndex, cellIndex)"
                     @mouseenter="!is_down ? '' : add_path(rowIndex, cellIndex)"
                     @mouseleave="is_leave = true"
                     @dblclick="remove_path(rowIndex, cellIndex)">
                    <div class="level" :style="set_style(0, cell)"></div>
                    <div class="vertical" :style="set_style(1, cell)"></div>
                </div>
            </div>
        </div>
        <div class="buttons">
            <button @click="p = (p - 1 + G.length) % G.length">&lt;</button>
            <button @click="p = 0">初始</button>
            <button @click="p = (p + 1) % G.length">&gt;</button>
        </div>
        <div class="buttons">
            <button @click="remove">删除</button>
            <button @click="init(n)">清空</button>
            <button @click="submit">提交</button>
        </div>
    </div>
</template>



<script setup>
import Title from '@/components/module/Title.vue';
import cloneDeep from 'lodash/cloneDeep';
import { provide, ref, watch, onMounted } from "vue";
import { POST, post } from "@/net";
import { ElMessage } from "element-plus";

provide("title", "连连看游戏库");

const n = ref(5); // 方阵大小
const g = ref([]); // 节点属性方阵
const G = ref([]); // 方阵数组
const p = ref(0); // 方阵数组的下标
const is_down = ref(false); // 是否为按下状态
const is_leave = ref(false); // 是否离开过监听的节点
const is_gain = ref(true); // 是否在初始化时重新获取方阵数组
const path = ref([]) // 节点路径栈追踪
const vis = ref([]) // 节点访问状态
const cache = ref(new Map()), ckey = ref(1); // 路径缓存
const color = ref(0); // 全局颜色指针
const colors = ref(["white", "black", "brown", "red", "orange", "yellow", "green", "cyan", "blue", "purple", "magenta", "pink"]) // 颜色表
const line = ref([ // 样式表
    ["", ""],
    ["width: 100%; height: 6%; top: 47%;", ""],
    ["", "width: 6%; height: 100%; left: 47%;"],
    ["width: 53%; height: 6%; top: 47%; left: 47%;", "width: 6%; height: 53%; left: 47%;"],
    ["width: 53%; height: 6%; top: 47%; left: 47%;", "width: 6%; height: 53%; left: 47%; top: 47%;"],
    ["width: 53%; height: 6%; top: 47%;", "width: 6%; height: 53%; left: 47%; top: 47%;"],
    ["width: 53%; height: 6%; top: 47%;", "width: 6%; height: 53%; left: 47%;"],
    ["", "width: 6%; height: 53%; left: 47%;"],
    ["width: 53%; height: 6%; top: 47%; left: 47%;", ""],
    ["", "width: 6%; height: 53%; left: 47%; top: 47%;"],
    ["width: 53%; height: 6%; top: 47%;", ""]
])

watch(n, async (newVal) => {
    await init(newVal);
});

watch(p, async (newVal) => {
    g.value = cloneDeep(G.value[newVal]);
});

const init = async (size) => {
    if (is_gain.value)
        G.value = await post("/link-game/get-library", size);
    g.value = cloneDeep(G.value[0]);
    p.value = 0;
    vis.value = await post("/link-game/create-visited", size);
    path.value = [];
    cache.value = new Map();
    ckey.value = 1;
    color.value = 1;
}

const submit = async () => {
    let cnt = 0;
    for (let i = 0; i < g.value.length; i++) {
        for (let j = 0; j < g.value.length; j++) {
            if (g.value[i][j][0] !== 0 && g.value[i][j][1] === 0) {
                cnt++;
                if (vis.value[i][j] === 0) {
                    ElMessage.warning("方阵内元素未完全连通，请重新设置！")
                    return
                }
            }
        }
    }
    if (cnt === 0) {
        ElMessage.warning("方阵内没有任何元素，请重新设置！")
        return
    }
    if (p.value === 0) {
        G.value.push(g.value);
    } else {
        G.value[p.value] = g.value;
    }
    is_gain.value = false;
    await init(n.value);
    is_gain.value = true;
    POST("/link-game/set-library",
        {json: JSON.stringify(G.value), n: n.value},
        (message) => ElMessage.success(message));
}

const remove = async () => {
    if (p.value !== 0) {
        G.value.splice(p.value, 1);
    }
    is_gain.value = false;
    await init(n.value);
    is_gain.value = true;
    POST("/link-game/set-library",
        {json: JSON.stringify(G.value), n: n.value},
        (message) => ElMessage.success(message));
}

const select_color = (index) => {
    if (color.value === index)
        return `background-color: ${ colors.value[index] }; box-shadow: 0 0 20px ${ colors.value[index] };`
    return `background-color: ${ colors.value[index] };`
}

const set_color = (cell) => {
    if (cell[1] > 0) return '';
    return `background-color: ${ colors.value[cell[0]] };`
}

const switch_color = async (i, j) => {
    if (vis.value[i][j] > 0 || path.value.length > 1) return;
    if (g.value[i][j][1] === 0 && !is_leave.value) {
        g.value[i][j][0] = color.value;
    }
}

const set_style = (index, cell) => {
    let res = `background-color: ${ colors.value[cell[0]] };`;
    res += "position: absolute;";
    res += line.value[cell[1]][index];
    return res;
}

const set_path = (i, j) => {
    is_leave.value = false;
    if (vis.value[i][j] > 0) return;
    if (g.value[i][j][0] > 0 && g.value[i][j][1] === 0 && path.value.length <= 1) {
        path.value = [[i, j, 0]];
        is_down.value = true;
    }
    if (path.value.length > 0) {
        is_down.value = true;
    }
}

const add_path = (xi, yi) => {
    let cur = [...path.value[path.value.length - 1]], root = [...path.value[0]];
    let x = cur[0], y = cur[1], z = cur[2];
    let col = g.value[x][y][0];
    const directions = [
        [x - 1, y],
        [x, y + 1],
        [x + 1, y],
        [x, y - 1]
    ];
    if (vis.value[xi][yi] > 0) return; // 标记过的节点
    if (g.value[xi][yi][0] !== 0 || g.value[xi][yi][1] !== 0) { // 非空地
        if (path.value.length >= 2) {
            let p = path.value[path.value.length - 2];
            if (xi === p[0] && yi === p[1]) { // 回退路径
                g.value[x][y] = [0, 0];
                path.value.pop();
                switch (p[2]) {
                    case 1:
                        g.value[xi][yi][1] = 9;
                        break;
                    case 2:
                        g.value[xi][yi][1] = 10;
                        break;
                    case 3:
                        g.value[xi][yi][1] = 7;
                        break;
                    case 4:
                        g.value[xi][yi][1] = 8;
                        break;
                    default:
                        g.value[xi][yi][1] = 0;
                        break;
                }
            }
        }
        if (xi === root[0] && yi === root[1]) return; // 路径起点
        if (g.value[xi][yi][0] === col && g.value[xi][yi][1] === 0) { // 路径终点
            if (directions.some(([dx, dy]) => xi === dx && yi === dy)) { // 相邻
                if (xi === x - 1 && yi === y) {
                    switch (z) {
                        case 1:
                            g.value[x][y][1] = 2;
                            break;
                        case 2:
                            g.value[x][y][1] = 6;
                            break;
                        case 4:
                            g.value[x][y][1] = 3;
                            break;
                        default:
                    }
                    path.value.push([xi, yi, 1])
                } else if (xi === x && yi === y + 1) {
                    switch (z) {
                        case 1:
                            g.value[x][y][1] = 4;
                            break;
                        case 2:
                            g.value[x][y][1] = 1;
                            break;
                        case 3:
                            g.value[x][y][1] = 3;
                            break;
                        default:
                    }
                    path.value.push([xi, yi, 2])
                } else if (xi === x + 1 && yi === y) {
                    switch (z) {
                        case 2:
                            g.value[x][y][1] = 5;
                            break;
                        case 3:
                            g.value[x][y][1] = 2;
                            break;
                        case 4:
                            g.value[x][y][1] = 4;
                            break;
                        default:
                    }
                    path.value.push([xi, yi, 3])
                } else if (xi === x && yi === y - 1) {
                    switch (z) {
                        case 1:
                            g.value[x][y][1] = 5;
                            break;
                        case 3:
                            g.value[x][y][1] = 6;
                            break;
                        case 4:
                            g.value[x][y][1] = 1;
                            break;
                        default:
                    }
                    path.value.push([xi, yi, 4])
                }
                path.value.forEach(([x, y, z]) => {
                    vis.value[x][y] = ckey.value; // 设置访问状态
                })
                cache.value.set(ckey.value++, [...path.value]); // 记录路径至缓存
                is_down.value = false;
                path.value = [];
            }
        }
        return;
    }
    if (directions.some(([dx, dy]) => xi === dx && yi === dy)) { // 相邻
        if (xi === x - 1 && yi === y) {
            g.value[xi][yi] = [col, 9];
            switch (z) {
                case 1:
                    g.value[x][y][1] = 2;
                    break;
                case 2:
                    g.value[x][y][1] = 6;
                    break;
                case 4:
                    g.value[x][y][1] = 3;
                    break;
                default:
            }
            path.value.push([xi, yi, 1]);
        } else if (xi === x && yi === y + 1) {
            g.value[xi][yi] = [col, 10];
            switch (z) {
                case 1:
                    g.value[x][y][1] = 4;
                    break;
                case 2:
                    g.value[x][y][1] = 1;
                    break;
                case 3:
                    g.value[x][y][1] = 3;
                    break;
                default:
            }
            path.value.push([xi, yi, 2]);
        } else if (xi === x + 1 && yi === y) {
            g.value[xi][yi] = [col, 7];
            switch (z) {
                case 2:
                    g.value[x][y][1] = 5;
                    break;
                case 3:
                    g.value[x][y][1] = 2;
                    break;
                case 4:
                    g.value[x][y][1] = 4;
                    break;
                default:
            }
            path.value.push([xi, yi, 3]);
        } else if (xi === x && yi === y - 1) {
            g.value[xi][yi] = [col, 8];
            switch (z) {
                case 1:
                    g.value[x][y][1] = 5;
                    break;
                case 3:
                    g.value[x][y][1] = 6;
                    break;
                case 4:
                    g.value[x][y][1] = 1;
                    break;
                default:
            }
            path.value.push([xi, yi, 4]);
        }
    }
}

const remove_path = (i, j) => {
    let ckey = vis.value[i][j];
    if (path.value.length === 1) {
        let p = path.value.pop();
        let x = p[0], y = p[1];
        g.value[x][y] = [0, 0];
    }
    if (ckey === 0) {
        for (let i = 1; i < path.value.length; i++) {
            let x = path.value[i][0], y = path.value[i][1];
            g.value[x][y] = [0, 0];
        }
        path.value = [];
        return;
    }
    let p = cache.value.get(ckey);
    for (let i = 1; i < p.length - 1; i++) {
        let x = p[i][0], y = p[i][1];
        g.value[x][y] = [0, 0];
        vis.value[x][y] = 0;
    }
    vis.value[p[0][0]][p[0][1]] = vis.value[p[p.length - 1][0]][p[p.length - 1][1]] = 0;
    cache.value.delete(ckey);
}

onMounted(async () => {
    await init(5);
})
</script>



<style scoped>
.radio {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
}

.select_color {
    width: 75%;
    height: 50px;
    margin: 20px auto;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
}
.select_color .item {
    width: 40px;
    height: 40px;
    border: 1px solid black;
    transition: all 0.1s;
}
.select_color .item:active {
    transform: scale(0.5)
}

.grid {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin: 100px auto;
}
.grid .row {
    display: flex;
}
.grid .cell {
    width: 50px;
    height: 50px;
    display: flex;
    border: 1px solid #000;
    position: relative;
}

.buttons {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 100px;
    margin: 20px;
}
.buttons button {
    border: none;
    outline: none;
    background-color: #6c5ce7;
    padding: 10px 20px;
    font-size: 14px;
    font-weight: 700;
    color: #fff;
    border-radius: 5px;
    transition: all ease 0.1s;
    box-shadow: 0 5px 0 0 #a29bfe;
    font-family: '楷体', serif;
}
.buttons button:active {
    transform: translateY(5px);
    box-shadow: 0 0 0 0 #a29bfe;
}
</style>
