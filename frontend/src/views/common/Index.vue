<template>
    <div class="container">
        <Title/>
        <div style="display: flex">
            <div style="margin-left: 10px;">
                <button class="Btn" style="background-color: red" @click="open">
                    <div class="sign">
                        <svg viewBox="0 0 512 512">
                            <path style="fill: firebrick"
                                d="M377.9 105.9L500.7 228.7c7.2 7.2 11.3 17.1 11.3 27.3s-4.1 20.1-11.3 27.3L377.9 406.1c-6.4 6.4-15 9.9-24 9.9c-18.7 0-33.9-15.2-33.9-33.9l0-62.1-128 0c-17.7 0-32-14.3-32-32l0-64c0-17.7 14.3-32 32-32l128 0 0-62.1c0-18.7 15.2-33.9 33.9-33.9c9 0 17.6 3.6 24 9.9zM160 96L96 96c-17.7 0-32 14.3-32 32l0 256c0 17.7 14.3 32 32 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32l-64 0c-53 0-96-43-96-96L0 128C0 75 43 32 96 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32z"
                            ></path>
                        </svg>
                    </div>
                    <div class="text" style="color: darkred">注销账号</div>
                </button>
            </div>
            <div style="margin-left: auto; margin-right: 10px;">
                <button class="Btn" @click="logout">
                    <div class="sign">
                        <svg viewBox="0 0 512 512">
                            <path
                                d="M377.9 105.9L500.7 228.7c7.2 7.2 11.3 17.1 11.3 27.3s-4.1 20.1-11.3 27.3L377.9 406.1c-6.4 6.4-15 9.9-24 9.9c-18.7 0-33.9-15.2-33.9-33.9l0-62.1-128 0c-17.7 0-32-14.3-32-32l0-64c0-17.7 14.3-32 32-32l128 0 0-62.1c0-18.7 15.2-33.9 33.9-33.9c9 0 17.6 3.6 24 9.9zM160 96L96 96c-17.7 0-32 14.3-32 32l0 256c0 17.7 14.3 32 32 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32l-64 0c-53 0-96-43-96-96L0 128C0 75 43 32 96 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32z"
                            ></path>
                        </svg>
                    </div>
                    <div class="text">退出登录</div>
                </button>
            </div>
        </div>
        <br>
        <div class="outermost">
            <div class="item" @click="watermark">
                <img src="/icon/BatchImageWatermarker.png" alt="批量图片水印处理">
                <button role="button" class="button-name">批量图片水印处理</button>
            </div>
            <div class="item" @click="crawler">
                <img src="/icon/Crawler.png" alt="简单爬虫脚本">
                <button role="button" class="button-name">简单爬虫脚本</button>
            </div>
            <div class="item" @click="guess_number">
                <img src="/icon/GuessNumber.png" alt="猜数字游戏">
                <button role="button" class="button-name">猜数字游戏</button>
            </div>
            <div class="item" @click="link_game">
                <img src="/icon/LinkGame.png" alt="连连看游戏"
                     :style="{ transform: `scale(${ scales[0] })` }"
                     @mouseenter="scales[0] *= 1.05"
                     @mouseleave="scales[0] /= 1.05">
                <button role="button" class="button-name">连连看游戏</button>
            </div>
            <div class="item" @click="character_map">
                <img src="/icon/CharacterMap.png" alt="角色自截图">
                <button role="button" class="button-name">角色自截图</button>
            </div>
            <div class="item" @click="document">
                <img src="/icon/Document.png" alt="Document"
                     :style="{ transform: `scale(${ scales[1] })` }"
                     @mouseenter="scales[1] *= 1.05"
                     @mouseleave="scales[1] /= 1.05">
                <button role="button" class="button-name">文档中心</button>
            </div>
            <div class="item" @click="test">
                <img src="/icon/Test.png" alt="Test">
                <button role="button" class="button-name">Test</button>
            </div>
        </div>
    </div>
</template>



<script setup>
import { _GET, post } from "@/net/index.js";
import router from "@/router/index.js";
import { useStore } from "@/stores/index.js";
import Title from '@/components/layout/Title.vue';
import { provide, ref } from "vue";
import { ElMessage, ElMessageBox } from 'element-plus';

provide("title", "星开祈灵百宝箱");

const store = useStore()
const scales = ref([0.7, 0.8])

const logout = () => {
    _GET('/api/auth/logout', (message) => {
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

const link_game = async () => {
    await router.push('/link-game')
}

const crawler = async () => {
    await router.push('/crawler')
}

const watermark = async () => {
    await router.push('/batch-image-watermarker')
}

const document = async () => {
    await router.push('/document')
}

const test = async () => {
    await router.push('/test')
}

const open = () => {
    ElMessageBox.confirm(
        '注销账号将永久删除所有信息，操作不可恢复！<br>您确定要继续吗？',
        '警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
            dangerouslyUseHTMLString: true
        }
    )
        .then(() => {
            ElMessage({
                type: 'success',
                message: '注销账号成功',
            })
            unsubscribe()
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '注销账号取消',
            })
        })
}

const unsubscribe = async () => {
    logout()
    await post("api/auth/unsubscribe", store.auth.user.username)
}
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}

.container {
    width: 1680px;
    height: 2200px;
    background-color: #ffffff;
    background-image: radial-gradient(rgba(12, 12, 12, 0.171) 2px, transparent 0);
    background-size: 30px 30px;
    background-position: -5px -5px;
    margin: auto;
}

.outermost {
    display: grid;
    grid-template-columns: 1fr 1fr;
    width: 50%;
    margin: auto;
    gap: 50px;
}

.item {
    background-color: white;
    border: 1px solid #151111;
    border-radius: 5px;
}
.item img {
    width: 100%;
    height: 78.5%;
    transition: transform 0.3s ease;
}
.item img:hover {
    transform: scale(1.05);
}

.button-name {
    align-items: center;
    appearance: none;
    background-color: #FCFCFD;
    border-radius: 4px;
    border-width: 0;
    box-shadow: rgba(45, 35, 66, 0.2) 0 2px 4px,rgba(45, 35, 66, 0.15) 0 7px 13px -3px,#D6D6E7 0 -3px 0 inset;
    box-sizing: border-box;
    color: #36395A;
    cursor: pointer;
    display: inline-flex;
    font-family: "楷体", monospace;
    width: 100%;
    height: 20%;
    justify-content: center;
    line-height: 1;
    list-style: none;
    overflow: hidden;
    padding-left: 16px;
    padding-right: 16px;
    position: relative;
    text-align: left;
    text-decoration: none;
    transition: box-shadow .15s,transform .15s;
    user-select: none;
    -webkit-user-select: none;
    touch-action: manipulation;
    white-space: nowrap;
    will-change: box-shadow,transform;
    font-size: 25px;
}
.button-name:focus {
    box-shadow: #D6D6E7 0 0 0 1.5px inset, rgba(45, 35, 66, 0.4) 0 2px 4px, rgba(45, 35, 66, 0.3) 0 7px 13px -3px, #D6D6E7 0 -3px 0 inset;
}
.button-name:hover {
    box-shadow: rgba(45, 35, 66, 0.3) 0 4px 8px, rgba(45, 35, 66, 0.2) 0 7px 13px -3px, #D6D6E7 0 -3px 0 inset;
    transform: translateY(-2px);
}
.button-name:active {
    box-shadow: #D6D6E7 0 3px 7px inset;
    transform: translateY(2px);
}

.Btn {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    width: 45px;
    height: 45px;
    border: none;
    border-radius: 50%;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition-duration: 0.3s;
    box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.199);
    background-color: RGB(29, 185, 84);
}
.sign {
    width: 100%;
    transition-duration: 0.3s;
    display: flex;
    align-items: center;
    justify-content: center;
}
.sign svg {
    width: 18px;
}
.sign svg path {
    fill: forestgreen;
}
.Btn .text {
    position: absolute;
    right: 0;
    width: 0;
    opacity: 0;
    color: darkgreen;
    font-size: 1.2em;
    font-weight: 600;
    transition-duration: 0.3s;
}
.Btn:hover {
    width: 125px;
    border-radius: 40px;
    transition-duration: 0.3s;
}
.Btn:hover .sign {
    width: 30%;
    transition-duration: 0.3s;
    padding-left: 10px;
}
.Btn:hover .text {
    opacity: 1;
    width: 70%;
    transition-duration: 0.3s;
}
.Btn:active {
    transform: translate(2px, 2px);
}
</style>
