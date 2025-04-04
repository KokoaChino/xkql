<template>
    <Title/>
    <div class="container">
        <el-input
            v-loading="isRunning"
            v-model="url"
            placeholder="请输入目标网址"
            class="input-field"
            clearable
        />
        <div class="selector-group">
            <el-input-number
                v-model="maxDeep"
                :min="1"
                :max="100"
                placeholder="递归深度"
                class="selector"
            />
            <el-select
                v-model="parseType"
                placeholder="选择解析类型"
                class="selector"
            >
                <el-option label="解析全部链接" value="all" />
                <el-option label="解析下载链接" value="download" />
            </el-select>
        </div>
        <div class="button-container">
            <div class="button-group">
                <el-button
                    @click="startParse"
                    :disabled="isRunning"
                    type="primary"
                    class="action-btn"
                >
                    开始解析
                </el-button>
                <el-button
                    @click="stopParse"
                    :disabled="!isRunning"
                    type="danger"
                    class="action-btn"
                >
                    停止解析
                </el-button>
            </div>
            <div class="status-message">
                已解析：{{ results.length }} 个链接
            </div>
        </div>
        <div class="result-container">
            <ul class="result-list">
                <li v-for="link in results" :key="link" class="result-item">
                    <a :href="link" target="_blank" class="result-link">{{ link }}</a>
                </li>
            </ul>
        </div>
    </div>
</template>



<script setup>
import { ref, provide } from 'vue';
import { GET, post, POST } from '@/net/index.js';
import { useStore } from '@/stores/index.js';
import { onBeforeRouteLeave } from "vue-router";
import Title from "@/components/layout/Title.vue";

provide("title", "简单爬虫脚本");

const store = useStore();
const url = ref('');
const maxDeep = ref(1);
const parseType = ref('all');
const isRunning = ref(false);
const results = ref([]);
const timerId = ref();

const startParse = async () => {
    isRunning.value = true;
    const params = {
        username: store.auth.user.username,
        target: url.value,
        mode: parseType.value,
        maxDeep: maxDeep.value,
    };
    await post('/api/crawler/get-all-links', params);
    results.value = []
    timerId.value = setInterval(async () => {
        try {
            results.value = await GET('/api/crawler/get-all-links', {
                username: store.auth.user.username,
            });
            if (results.value.length > 0 && results.value[results.value.length - 1] === null) {
                results.value.pop();
                clearInterval(timerId.value);
                isRunning.value = false;
            }
        } catch (e) {
            isRunning.value = false;
            clearInterval(timerId.value);
        }
    }, 2000);
};

const stopParse = async () => {
    clearInterval(timerId.value);
    isRunning.value = false;
    await POST('/api/crawler/stop-task', {
        username: store.auth.user.username,
    });
};

onBeforeRouteLeave((to, from, next) => {
    stopParse()
    next()
})
</script>



<style scoped>
.container {
    max-width: 1000px;
    margin: 2rem auto;
    padding: 1rem;
    background: #f5f5f5;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.input-field {
    width: 100%;
    margin-bottom: 1.5rem;
}
.selector-group {
    display: flex;
    justify-content: space-between;
    margin-bottom: 1.5rem;
}
.selector {
    width: 48%;
}

.action-btn {
    width: 120px;
}

.result-container {
    margin-top: 2rem;
}
.result-list {
    list-style: none;
    padding: 0;
    margin: 0;
}
.result-item {
    display: flex;
    align-items: center;
    padding: 12px;
    margin: 8px 0;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}
.result-link {
    flex: 1;
    margin-right: 10px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: #409eff;
    text-decoration: underline;
    cursor: pointer;
}

.button-container {
    display: flex;
    align-items: center;
    margin-bottom: 2rem;
    gap: 2rem;
}
.button-group {
    display: flex;
    justify-content: center;
    flex: 1;
    gap: 1rem;
}
.status-message {
    flex: 1;
    text-align: left;
    padding-left: 10px;
}

@media (max-width: 768px) {
    .button-container {
        flex-direction: column;
        align-items: stretch;
    }
    .button-group {
        margin-bottom: 1rem;
    }
    .status-message {
        text-align: left;
        padding: 0;
    }
}
</style>
