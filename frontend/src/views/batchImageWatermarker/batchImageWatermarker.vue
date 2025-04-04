<template>
    <Title/>
    <div class="container">
        <div class="upload-section" v-loading="isRunning">
            <el-upload
                :http-request="customUpload"
                :before-upload="beforeUpload"
                :show-file-list="false"
                class="custom-upload"
            >
                <div class="upload-content">
                    <el-icon class="upload-icon">
                        <Plus />
                    </el-icon>
                    <div class="upload-text">请上传 .zip 文件</div>
                </div>
            </el-upload>
        </div>
        <el-progress
            :percentage="progress"
            :stroke-width="24"
            color="#409EFF"
            :text-inside="true"
            class="custom-progress"
        >
            <span class="progress-text">{{ progress }}%</span>
        </el-progress>
        <div class="selection-container">
            <div
                v-for="i in modes"
                :key="i"
                class="select-item"
                :class="{ active: i === mode }"
                @click="mode=i"
            >
                <img
                    :src="`/批量图片水印处理/模式 ${i}.jpg`"
                    class="select-image"
                />
                <div class="select-number">模式 {{ i }}</div>
            </div>
        </div>
        <div class="result-container">
            <div class="result-block">
                <div class="block-header">匹配成功：{{ successMatch.length }} 个</div>
                <el-table
                    :data="Array.from(successMatch)"
                    height="200"
                    class="match-table"
                    :show-header="false"
                    :empty-text="' '"
                >
                    <el-table-column width="300">
                        <template #default="{ row }">
                            <div class="match-item">{{ row }}</div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="result-block">
                <div class="block-header">表匹配失败：{{ tableNoMatch.length }} 个</div>
                <el-table
                    :data="Array.from(tableNoMatch)"
                    height="200"
                    class="match-table"
                    :show-header="false"
                    :empty-text="' '"
                >
                    <el-table-column width="300">
                        <template #default="{ row }">
                            <div class="match-item">{{ row }}</div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="result-block">
                <div class="block-header">图匹配失败：{{ imageNoMatch.length }} 个</div>
                <el-table
                    :data="Array.from(imageNoMatch)"
                    height="200"
                    class="match-table"
                    :show-header="false"
                    :empty-text="' '"
                >
                    <el-table-column width="300">
                        <template #default="{ row }">
                            <div class="match-item">{{ row }}</div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>



<script setup>
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue'
import { ref, provide } from "vue";
import { GET, POST } from "@/net/index.js";
import { useStore } from "@/stores/index.js";
import axios from 'axios';
import Title from "@/components/layout/Title.vue";
import { onBeforeRouteLeave } from "vue-router";

provide("title", "批量图片水印处理");
const store = useStore()

const successMatch = ref([])
const tableNoMatch = ref([])
const imageNoMatch = ref([])

const modes = Array.from({length: 5}, (_, i) => i + 1);
const mode = ref(1)
const progress = ref(0)
const isRunning = ref(false);

const customUpload = async (options) => {
    isRunning.value = true
    try {
        successMatch.value = tableNoMatch.value = imageNoMatch.value = []
        progress.value = 0
        const {file} = options;
        const formData = new FormData();
        formData.append('username', store.auth.user.username);
        formData.append('file', file);
        formData.append('mode', mode.value);
        await axios.post('/api/batch-image-watermarker/start-task', formData, {
            withCredentials: true,
            responseType: 'blob'
        });
        let timerId = setInterval(async () => {
            progress.value = await GET("/api/batch-image-watermarker/get-progress", {
                username: store.auth.user.username
            })
            if (progress.value === 100) {
                clearInterval(timerId);
                try {
                    const response = await axios.get('/api/batch-image-watermarker/get-zip-file', {
                        params: {
                            username: store.auth.user.username
                        },
                        withCredentials: true,
                        responseType: 'blob'
                    });
                    let map = await GET("/api/batch-image-watermarker/get-additional-data", {
                        username: store.auth.user.username
                    })
                    successMatch.value = map["successMatch"]
                    tableNoMatch.value = map["tableNoMatch"]
                    imageNoMatch.value = map["imageNoMatch"]
                    const url = window.URL.createObjectURL(response.data);
                    const link = document.createElement('a');
                    link.href = url;
                    link.download = 'modified.zip';
                    document.body.appendChild(link);
                    link.click();
                    link.remove();
                    window.URL.revokeObjectURL(url);
                    options.onSuccess('文件上传成功');
                } catch (error) {
                    options.onError(error);
                    if (error.response) {
                        const errorText = await error.response.data.text();
                        const errorJson = JSON.parse(errorText);
                        ElMessage.error(errorJson.message);
                    }
                } finally {
                    isRunning.value = false
                }
            }
        }, 1000);
    } catch (error) {
        ElMessage.error('处理过程中发生错误: ' + error.message);
        options.onError(error);
    } finally {
        isRunning.value = false
    }
}

const beforeUpload = (file) => {
    const allowedTypes = ['application/zip', 'application/x-zip-compressed'];
    if (allowedTypes.includes(file.type) && file.name.endsWith('.zip')) return true;
    ElMessage.error('请上传 .zip 文件');
    return false;
}

onBeforeRouteLeave(async (to, from, next) => {
    await POST("/api/batch-image-watermarker/stop-task", {
        username: store.auth.user.username
    })
    next()
})
</script>



<style scoped>
.container {
    width: 1000px;
    margin: 20px auto;
    padding: 30px;
    background: #f5faff;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(64,158,255,0.08);
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.upload-section {
    width: 100%;
    height: 200px;
    margin-bottom: 20px;
}
.custom-upload {
    width: 100%;
    height: 100%;
    border: 2px dashed #409EFF;
    border-radius: 8px;
    background-color: rgba(64,158,255,0.05);
    transition: border-color 0.3s;
    &:hover {
        border-color: #79bbff;
        background-color: rgba(64,158,255,0.1);
    }
}
.upload-content {
    text-align: center;
    color: #409EFF;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    .upload-icon {
        font-size: 48px;
        margin-bottom: 12px;
    }
    .upload-text {
        font-size: 16px;
        font-weight: 500;
    }
}
.custom-upload :deep(.el-upload) {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.custom-progress {
    margin: 10px 0;
    border-radius: 12px;
    :deep(.el-progress-bar) {
        padding-right: 60px;
        margin-right: -50px;
    }
    :deep(.el-progress-bar__inner) {
        background-image: linear-gradient(
            45deg,
            rgba(255,255,255,0.15) 25%,
            transparent 25%,
            transparent 50%,
            rgba(255,255,255,0.15) 50%,
            rgba(255,255,255,0.15) 75%,
            transparent 75%,
            transparent
        );
        background-size: 40px 40px;
        animation: progress-stripes 2s linear infinite;
    }
}
.progress-text {
    position: absolute;
    right: 15px;
    top: 50%;
    transform: translateY(-50%);
    color: #fff;
    font-weight: bold;
    z-index: 1;
}
@keyframes progress-stripes {
    from { background-position: 0 0; }
    to { background-position: 40px 0; }
}

.result-container {
    display: flex;
}
.result-block {
    flex: 1;
    background: white;
    padding: 15px;
    box-sizing: border-box;
    box-shadow: 0 2px 8px rgba(64,158,255,0.08);
    border: 1px solid #e4e7ed;
}
.match-table {
    :deep(.el-table__body) {
        tr:hover > td {
            background-color: transparent !important;
        }
        td {
            border-bottom: none;
            padding: 8px 0;
        }
    }
}

.selection-container {
    display: flex;
    gap: 10px;
    overflow-x: auto;
    padding: 15px 0;
}
.select-item {
    flex-shrink: 0;
    width: 200px;
    cursor: pointer;
    transition: all 0.3s;
    border: 2px solid #ebeef5;
    border-radius: 8px;
    padding: 8px;
    &.active {
        border-color: #409EFF;
        background-color: #f0f7ff;
    }
}
.select-image {
    width: 100%;
    aspect-ratio: 1;
    border-radius: 4px;
    object-fit: cover;
}
.select-number {
    text-align: center;
    font-size: 18px;
    color: #409EFF;
    padding: 8px;
    font-weight: bold;
}
.block-header {
    color: #409EFF;
    font-size: 16px;
    margin-bottom: 12px;
    font-weight: 500;
}
.selection-container::-webkit-scrollbar {
    height: 8px;
    background: #f5f7fa;
}
.selection-container::-webkit-scrollbar-thumb {
    background: #c1d9ff;
    border-radius: 4px;
}
</style>
