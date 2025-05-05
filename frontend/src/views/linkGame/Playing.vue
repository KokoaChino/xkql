<template>
    <div class="main-container">
        <Title/>
        <div @mouseup="is_down=false" @dragstart.prevent>
            <div class="radio">
                <span style="margin-right: 30px">方阵大小</span>
                <el-radio-group v-model="n" size="large" v-for="(x, index) in [4, 5, 6, 7, 8, 9, 10, 11, 12, 13]" :key="index">
                    <el-radio-button :label="x" :value="x"/>
                </el-radio-group>
            </div>
            <div v-show="G.length > 1" style="width: 100%; text-align: center;">
                <h1 style="color: #606266">
                    {{ p }} / {{ G.length - 1 }}
                </h1>
                <h2 style="color: #529b2e">
                    {{ formatSeconds(seconds) }}
                </h2>
            </div>
            <div class="grid">
                <div v-for="(row, rowIndex) in g" :key="rowIndex" class="row">
                    <div v-for="(cell, cellIndex) in row" :key="cellIndex" class="cell"
                         :style="set_color(cell)"
                         :data-row="rowIndex" :data-col="cellIndex"
                         @mousedown="handleStart($event, rowIndex, cellIndex)"
                         @touchstart.passive="handleStart($event, rowIndex, cellIndex)"
                         @mouseenter="!is_down ? '' : add_path(rowIndex, cellIndex)"
                         @touchmove.prevent="handleMove($event)"
                         @touchend="handleEnd"
                         @dblclick="remove_path(rowIndex, cellIndex)"
                         @touchstart="handleTouchStart($event, rowIndex, cellIndex)">
                        <div class="level" :style="set_style(0, cell)"></div>
                        <div class="vertical" :style="set_style(1, cell)"></div>
                    </div>
                </div>
            </div>
            <div v-if="G.length <= 1" class="buttons">
                <button @click="init">开始游戏</button>
            </div>
            <div v-else class="buttons">
                <button @click="reset">重置</button>
                <button @click="init">新的游戏</button>
            </div>
        </div>
    </div>
</template>



<script setup>
import Title from '@/components/layout/Title.vue';
import cloneDeep from 'lodash/cloneDeep';
import { provide, ref, watch } from "vue";
import { post } from "@/net/index.js";
import { useStore } from "@/stores/index.js";
import { ElMessage } from "element-plus";

provide("title", "连连看游戏");
const store = useStore()

const n = ref(4); // 方阵大小
const g = ref([]); // 节点属性方阵
const G = ref([]); // 方阵数组
const p = ref(0); // 方阵数组的下标
const prev_p = ref(0); // 上一次方阵数组的下标
const seconds = ref(0); // 记录用时
const timerId = ref(-1); // 计时器 ID
const is_down = ref(false); // 是否为按下状态
const path = ref([]) // 节点路径栈追踪
const vis = ref([]) // 节点访问状态
const cnt = ref(0) // 节点对数
const cache = ref(new Map()), ckey = ref(1); // 路径缓存
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

watch(n, () => {
    G.value = []
    g.value = []
    cnt.value = p.value = prev_p.value = 0
});

const init = async () => {
    G.value = await post("/api/link-game/get-library", n.value);
    if (G.value.length <= 1) {
        ElMessage.warning("游戏库为空！")
        return;
    }
    if (p.value !== 0) prev_p.value = p.value;
    p.value = await post("/api/link-game/get-random", [G.value.length, prev_p.value])
    await reset();
    clearTimeout(timerId.value); // 取消定时器
    seconds.value = 0;
    timerId.value = setInterval(() => {seconds.value++}, 1000);
}

const reset = async () => {
    g.value = re_mat(cloneDeep(G.value[p.value]));
    vis.value = await post("/api/link-game/create-visited", n.value);
    path.value = [];
    cache.value = new Map();
    ckey.value = 1;
}

const re_mat = (mat) => {
    cnt.value = 0;
    for (let i = 0; i < mat.length; i++) {
        for (let j = 0; j < mat[i].length; j++) {
            if (mat[i][j][0] !== 0 && mat[i][j][1] === 0) cnt.value++;
            if (mat[i][j][1] !== 0) mat[i][j] = [0, 0];
        }
    }
    cnt.value /= 2;
    return mat;
}

const set_color = (cell) => {
    if (cell[1] > 0) return '';
    return `background-color: ${ colors.value[cell[0]] };`
}

const set_style = (index, cell) => {
    let res = `background-color: ${ colors.value[cell[0]] };`;
    res += "position: absolute;";
    res += line.value[cell[1]][index];
    return res;
}

function formatSeconds(seconds) {
    return `${String(Math.floor(seconds / 60)).padStart(2, '0')} : ${String(seconds % 60).padStart(2, '0')}`;
}

const set_path = (i, j) => {
    if (vis.value[i][j] > 0) return;
    if (g.value[i][j][0] > 0 && g.value[i][j][1] === 0 && path.value.length <= 1) {
        path.value = [[i, j, 0]];
        is_down.value = true;
    }
    if (path.value.length > 0) {
        is_down.value = true;
    }
}

const handleStart = (event, rowIndex, cellIndex) => {
    event.preventDefault();
    set_path(rowIndex, cellIndex);
    is_down.value = true;
}

const handleMove = (event) => {
    if (!is_down.value) return;
    event.preventDefault();
    const touch = event.touches[0];
    const element = document.elementFromPoint(touch.clientX, touch.clientY);
    if (element?.closest('.cell')) {
        const dataset = element.dataset;
        add_path(parseInt(dataset.row), parseInt(dataset.col));
    }
}

const handleEnd = () => {
    is_down.value = false;
}

const lastTap = ref(0), tapDelay = ref(300);

const handleTouchStart = (event, row, col) => {
    const now = Date.now();
    if (now - lastTap.value < tapDelay.value) {
        event.preventDefault();
        remove_path(row, col);
        lastTap.value = 0;
    } else {
        lastTap.value = now;
    }
}

const add_path = async (xi, yi) => {
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
                if (--cnt.value === 0) { // 游戏结束
                    ElMessage.success("游戏结束！");
                    clearTimeout(timerId.value); // 取消定时器
                    await post("/api/link-game/historical-record/update", [store.auth.user.username, n.value, formatSeconds(seconds.value)])
                }
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
    cnt.value++;
    cache.value.delete(ckey);
}
</script>



<style scoped>
* {
    touch-action: manipulation;
}
.main-container {
    width: 100%;
    max-width: 1680px;
    margin: 0 auto;
    box-sizing: border-box;
}

.radio {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
}

.grid {
    overflow: hidden;
    touch-action: none;
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
    user-select: none;
    -webkit-user-drag: none;
    -webkit-tap-highlight-color: transparent;
    touch-action: manipulation;
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
    margin: 20px;
    gap: 30px;
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
