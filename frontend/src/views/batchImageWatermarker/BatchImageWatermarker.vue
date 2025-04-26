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
        <div v-if="progress > 0">
            <el-progress
                :percentage="progress"
                :stroke-width="24"
                color="#409EFF"
                :text-inside="true"
                class="custom-progress"
            >
                <span class="progress-text">{{ progress }}%</span>
            </el-progress>
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
        <div class="selection-style" v-if="!isCustomStyle">
            <div
                v-for="(data, i) in presetModes"
                :key="i"
                class="select-item"
                :class="{ active: i + 1 === mode }"
                @click="mode = i + 1"
            >
                <img
                    :src="`/批量图片水印处理/预设样式 ${ i + 1 }.jpg`"
                    class="select-image"
                    :alt="`预设样式 ${ i + 1 }`"/>
                <div class="select-number">预设样式 {{ i + 1 }}</div>
            </div>
        </div>
        <div class="selection-style" v-else v-loading="loading2">
            <div class="select-item custom-preview"
                v-for="(data, i) in customModes"
                :key="i"
                :class="{ active: i + 1 === mode }"
                @click="mode = i + 1"
                @mouseenter="showButton[i] = true"
                @mouseleave="showButton[i] = false"
            >
                <div v-if="i === customModes.length - 1">
                    <img
                        :src="customModes[i].previewImage"
                        class="select-image"
                        alt="添加自定义样式"/>
                    <div class="select-number" style="color: #27c627">自定义样式编辑区</div>
                </div>
                <div v-else>
                    <img
                        :src="customModes[i].previewImage"
                        class="select-image"
                        :alt="`自定义样式 ${ i + 1 }`"/>
                    <div class="select-number">自定义样式 {{ i + 1 }}</div>
                    <button
                        class="custom-btn"
                        :class="{ 'is-visible': showButton[i] }"
                        @click="deleteCustomStyle(i)">
                        <span class="btn-icon">×</span>
                    </button>
                </div>
            </div>
        </div>
        <div class="control-bar">
            <div class="radio-container" >
                <el-radio-group v-model="style" size="large">
                    <el-radio-button label="预设样式" value="预设样式" />
                    <el-radio-button label="自定义样式" value="自定义样式" />
                </el-radio-group>
            </div>
            <el-button
                size="large"
                type="primary"
                @click="addCustomStyle"
                :disabled="!isCustomStyle"
            >
                <span v-if="isCustomStyle && mode === customModes.length">添加样式</span>
                <span v-else>更新样式</span>
            </el-button>
        </div>
        <div class="params-container">
            <el-row :gutter="20">
                <el-col :span="12">
                    <h2 class="header-with-button">
                        样式参数
                        <el-button class="right-button"
                                   size="large"
                                   type="info"
                                   @click="refill"
                                   :disabled="!isCustomStyle">重置样式参数</el-button>
                    </h2>
                    <div class="params-panel">
                        <el-collapse v-model="activeCollapse">
                            <el-collapse-item name="位置">
                                <template #title>
                                    <span class="collapse-title">位置</span>
                                </template>
                                <div class="content-container" :class="{ 'disabled': !isCustomStyle }">
                                    <div class="mask" v-if="!isCustomStyle"></div>
                                    <div class="optional-item">
                                        <div class="optional-row">
                                            坐标
                                            <el-input-number
                                                v-model="params.x"
                                                :min="-3000" :max="3000"
                                            />
                                            <el-input-number
                                                v-model="params.y"
                                                :min="-3000" :max="3000"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </el-collapse-item>
                            <el-collapse-item name="字符偏移">
                                <template #title>
                                    <span class="collapse-title">字符偏移</span>
                                </template>
                                <div class="content-container" :class="{ 'disabled': !isCustomStyle }">
                                    <div class="mask" v-if="!isCustomStyle"></div>
                                    <div class="optional-item">
                                        <div class="optional-row">
                                            水平偏移
                                            <el-input-number v-model="params.dx"
                                                             :min="-3000" :max="3000"/>
                                            包含字符宽度
                                            <el-switch v-model="params.includeCharWidthInDx"/>
                                        </div>
                                        <div class="optional-row">
                                            垂直偏移
                                            <el-input-number v-model="params.dy"
                                                             :min="-3000" :max="3000"/>
                                            包含字符高度
                                            <el-switch v-model="params.includeCharHeightInDy"/>
                                        </div>
                                    </div>
                                </div>
                            </el-collapse-item>
                            <el-collapse-item name="字体">
                                <template #title>
                                    <span class="collapse-title">字体</span>
                                </template>
                                <div class="content-container" :class="{ 'disabled': !isCustomStyle }">
                                    <div class="mask" v-if="!isCustomStyle"></div>
                                    <div class="optional-item">
                                        <div class="optional-row">
                                            颜色
                                            <el-color-picker v-model="params.fontColor"/>
                                        </div>
                                        <div class="optional-row">
                                            名称
                                            <el-input
                                                v-model="fontNameTmp"
                                                @blur="params.fontName = fontNameTmp"
                                                style="width: 350px"
                                                placeholder="请输入字体名称"
                                                clearable
                                            />
                                        </div>
                                        <div class="optional-row">
                                            样式
                                            <el-checkbox-group v-model="params.fontStyles">
                                                <el-checkbox-button label="粗体">粗体</el-checkbox-button>
                                                <el-checkbox-button label="斜体">斜体</el-checkbox-button>
                                            </el-checkbox-group>
                                        </div>
                                        <div class="optional-row">
                                            大小
                                            <el-input-number
                                                v-model="params.fontSize"
                                                :min="0" :max="1000"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </el-collapse-item>
                            <el-collapse-item name="描边">
                                <template #title>
                                    <span class="collapse-title">描边</span>
                                </template>
                                <div class="content-container" :class="{ 'disabled': !isCustomStyle }">
                                    <div class="mask" v-if="!isCustomStyle"></div>
                                    <div class="optional-item">
                                        <div class="optional-row">
                                            颜色
                                            <el-color-picker v-model="params.strokeColor"/>
                                        </div>
                                        <div class="optional-row">
                                            大小
                                            <el-input-number
                                                v-model="params.strokeSize"
                                                :min="0" :max="100"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </el-collapse-item>
                            <el-collapse-item name="投影">
                                <template #title>
                                    <span class="collapse-title">投影</span>
                                </template>
                                <div class="content-container" :class="{ 'disabled': !isCustomStyle }">
                                    <div class="mask" v-if="!isCustomStyle"></div>
                                    <div class="optional-item">
                                        <div class="optional-row">
                                            投影颜色
                                            <el-color-picker v-model="params.shadowColor"/>
                                        </div>
                                        <div class="optional-row">
                                            水平偏移
                                            <el-input-number v-model="params.shadowDx"
                                                             :min="-3000" :max="3000"/>
                                        </div>
                                        <div class="optional-row">
                                            垂直偏移
                                            <el-input-number v-model="params.shadowDy"
                                                             :min="-3000" :max="3000"/>
                                        </div>
                                        <div class="optional-row">
                                            不透明度
                                            <el-input-number
                                                v-model="params.shadowOpacity"
                                                :min="0" :max="1"
                                                :step="0.1"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </el-collapse-item>
                            <el-collapse-item name="渐变">
                                <template #title>
                                    <span class="collapse-title">渐变</span>
                                </template>
                                <div class="content-container" :class="{ 'disabled': !isCustomStyle }">
                                    <div class="mask" v-if="!isCustomStyle"></div>
                                    <div class="optional-row" style="margin: 0 40px 15px 15px;">
                                        字体渐变
                                        <el-switch v-model="params.fontGradient.enableGradient"/>
                                        <span style="margin-left: 30px;">描边渐变</span>
                                        <el-switch v-model="params.strokeGradient.enableGradient"/>
                                    </div>
                                    <div class="sub-panel" v-if="params.fontGradient.enableGradient">
                                        <h4>字体渐变</h4>
                                        <div class="optional-item">
                                            <div class="optional-row">
                                                起始颜色
                                                <el-color-picker v-model="params.fontGradient.startColor"/>
                                                <span style="margin-left: 5px;">结束颜色</span>
                                                <el-color-picker v-model="params.fontGradient.endColor"/>
                                                循环渐变
                                                <el-switch v-model="params.fontGradient.cyclic" />
                                            </div>
                                            <div class="optional-row">
                                                起点坐标
                                                <el-input-number
                                                    v-model="params.fontGradient.start.x"
                                                    :min="-3000" :max="3000"/>
                                                <el-input-number
                                                    v-model="params.fontGradient.start.y"
                                                    :min="-3000" :max="3000"/>
                                            </div>
                                            <div class="optional-row">
                                                终点坐标
                                                <el-input-number
                                                    v-model="params.fontGradient.end.x"
                                                    :min="-3000" :max="3000"/>
                                                <el-input-number
                                                    v-model="params.fontGradient.end.y"
                                                    :min="-3000" :max="3000"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="sub-panel" v-if="params.strokeGradient.enableGradient">
                                        <h4>描边渐变</h4>
                                        <div class="optional-item">
                                            <div class="optional-row">
                                                起始颜色
                                                <el-color-picker v-model="params.strokeGradient.startColor"/>
                                                <span style="margin-left: 5px;">结束颜色</span>
                                                <el-color-picker v-model="params.strokeGradient.endColor"/>
                                                循环渐变
                                                <el-switch v-model="params.strokeGradient.cyclic" />
                                            </div>
                                            <div class="optional-row">
                                                起点坐标
                                                <el-input-number
                                                    v-model="params.strokeGradient.start.x"
                                                    :min="-3000" :max="3000"/>
                                                <el-input-number
                                                    v-model="params.strokeGradient.start.y"
                                                    :min="-3000" :max="3000"/>
                                            </div>
                                            <div class="optional-row">
                                                终点坐标
                                                <el-input-number
                                                    v-model="params.strokeGradient.end.x"
                                                    :min="-3000" :max="3000"/>
                                                <el-input-number
                                                    v-model="params.strokeGradient.end.y"
                                                    :min="-3000" :max="3000"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </el-collapse-item>
                            <el-collapse-item name="仿射变换">
                                <template #title>
                                    <span class="collapse-title">仿射变换</span>
                                </template>
                                <div class="content-container" :class="{ 'disabled': !isCustomStyle }">
                                    <div class="mask" v-if="!isCustomStyle"></div>
                                    <div class="optional-item">
                                        <div class="optional-row">
                                            优先旋转
                                            <el-switch v-model="params.priorityRotation" />
                                        </div>
                                        <div class="optional-row">
                                            旋转角度
                                            <el-input-number
                                                v-model="params.rotation"
                                                :min="-360" :max="360"
                                                :step="5"
                                            />
                                        </div>
                                        <div class="optional-row">
                                            <span style="margin-right: 2px;">X 轴剪切</span>
                                            <el-input-number
                                                v-model="params.shearX"
                                                :min="-5" :max="5"
                                                :step="0.1"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </el-collapse-item>
                        </el-collapse>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="preview-panel" v-if="!isCustomStyle">
                        <div class="preview-image">
                            <img :src="`/批量图片水印处理/预设样式 ${ mode }.jpg`" alt="预览图" class="preview-img" />
                        </div>
                    </div>
                    <div class="preview-panel" v-else>
                        <div class="preview-image" v-loading="loading1"
                             @mouseenter="showButton1 = true" @mouseleave="showButton1 = false">
                            <img
                                :src="previewImage"
                                alt="预览图"
                                class="preview-img"
                            />
                            <button
                                class="preview-btn"
                                :class="{ 'is-visible': showButton1 && backgroundImage !== defaultBackgroundImage }"
                                @click="deleteBackgroundImage"
                            >
                                <span class="btn-icon">×</span>
                            </button>
                        </div>
                        <el-upload
                            :show-file-list="false"
                            :before-upload="handleUpload"
                            accept="image/jpeg"
                        >
                            <button class="button">
                                <span class="button-content">上传预览背景图片</span>
                            </button>
                        </el-upload>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>



<script setup>
import {Plus} from '@element-plus/icons-vue';
import {ElMessage, ElMessageBox, ElNotification} from 'element-plus';
import {get, GET, POST, post, Post} from "@/net/index.js";
import {useStore} from "@/stores/index.js";
import axios from 'axios';
import Title from "@/components/layout/Title.vue";
import {onBeforeRouteLeave} from "vue-router";
import {onMounted, provide, ref, watch} from 'vue'

provide("title", "批量图片水印处理");
const store = useStore()
const skipParamsUpdate = ref(false);

const successMatch = ref([])
const tableNoMatch = ref([])
const imageNoMatch = ref([])

const progress = ref(0)
const isRunning = ref(false)

const presetModes = ref([]) // 预设样式
const customModes = ref([]) // 自定义样式
const style = ref("预设样式"), isCustomStyle = ref(false)
const mode = ref(1), limit = 100

const activeCollapse = ref(['位置', '字符偏移', '字体', '描边', '投影', '渐变', '仿射变换'])
const loading1 = ref(false), loading2 = ref(false)
const showButton1 = ref(false), showButton = ref(Array(limit).fill(false))

let defaultBackgroundImage = "";

const defaultGradient = {
    enableGradient: false,
    start: { x: 0, y: 0 },
    end: { x: 1200, y: 1200 },
    startColor: '#0000ff',
    endColor: '#ff0000',
    cyclic: true
}
const defaultParams = {
    // 位置
    x: 0, y: 1200,

    // 字符偏移
    dx: 0, dy: 0,
    includeCharWidthInDx: true,
    includeCharHeightInDy: false,

    // 字体
    fontName: '微软雅黑',
    fontStyles: [],
    fontSize: 100,
    fontColor: '#000000',

    // 描边
    strokeSize: 0,
    strokeColor: '#FFFFFF',

    // 投影
    shadowDx: 0, shadowDy: 0,
    shadowColor: '#000000',
    shadowOpacity: 0,

    // 渐变
    fontGradient: JSON.parse(JSON.stringify(defaultGradient)),
    strokeGradient: JSON.parse(JSON.stringify(defaultGradient)),

    // 仿射变换
    rotation: 0,
    shearX: 0,
    priorityRotation: true
}
const params = ref(JSON.parse(JSON.stringify(defaultParams)))
const backgroundImage = ref("")
const previewImage = ref(""), fontNameTmp = ref("")
const colorSet = new Set(["startColor", "endColor", "fontColor", "strokeColor", "shadowColor"])
const numberSet = new Set(["x", "y", "dx", "dy", "fontSize", "strokeSize", "shadowDx", "shadowDy", "shadowOpacity", "rotation", "shearX"])

watch(style, (newVal) => {
    isCustomStyle.value = newVal === "自定义样式"
    skipParamsUpdate.value = true
    mode.value = 1
    toParams()
})
watch(mode, () => {
    skipParamsUpdate.value = true
    toParams()
})
watch(params, (newVal) => {
    if (skipParamsUpdate.value) {
        skipParamsUpdate.value = false;
        return;
    }
    setNullToZero(newVal)
    toPreviewImage()
}, { deep: true });

const refill = () => {
    params.value = JSON.parse(JSON.stringify(defaultParams))
    fontNameTmp.value = params.value.fontName
}

function setNullToZero(obj) {
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            const value = obj[key];
            if (typeof value === 'object' && value !== null) {
                setNullToZero(value);
            } else if (value === null) {
                if (colorSet.has(key)) obj[key] = '#FFFFFF';
                else if (numberSet.has(key)) obj[key] = 0;
            }
        }
    }
}

const toParams = () => {
    if (!isCustomStyle.value) {
        params.value = JSON.parse(JSON.stringify(presetModes.value[mode.value - 1]))
    } else {
        if (mode.value === customModes.value.length) {
            params.value = customModes.value[mode.value - 1].params
        } else {
            params.value = JSON.parse(JSON.stringify(customModes.value[mode.value - 1].params))
        }
        backgroundImage.value = customModes.value[mode.value - 1].backgroundImage
        previewImage.value = customModes.value[mode.value - 1].previewImage
    }
    fontNameTmp.value = params.value.fontName
}

const toPreviewImage = async () => {
    if (!isCustomStyle.value) return;
    loading1.value = true
    try {
        try {
            previewImage.value = await post("/api/batch-image-watermarker/get-preview-image", {
                params: params.value,
                base64: backgroundImage.value
            });
        } catch (error) {
            if (error.response) {
                const { type, message } = error.response.data;
                if (type === "FontNotFoundException") {
                    ElMessage.error("字体未找到异常：" + message);
                    await handleTTF(message)
                }
            }
        }
        if (mode.value === customModes.value.length) {
            customModes.value[mode.value - 1].previewImage = previewImage.value
            customModes.value[mode.value - 1].backgroundImage = backgroundImage.value
        }
    } catch (e) {
        ElMessage.error("加载数据失败：" + e)
    } finally {
        loading1.value = false
    }
}

const deleteBackgroundImage = async () => {
    backgroundImage.value = defaultBackgroundImage
    await toPreviewImage()
}

const toCustomStyle = async () => {
    customModes.value = await GET("/api/batch-image-watermarker/get-custom-style-params", {
        username: store.auth.user.username
    })
    customModes.value.push({
        id: -1,
        username: store.auth.user.username,
        backgroundImage: defaultBackgroundImage,
        previewImage: await post("/api/batch-image-watermarker/get-preview-image", {
            params: defaultParams,
            base64: defaultBackgroundImage
        }),
        params: JSON.parse(JSON.stringify(defaultParams))
    })
    toParams()
}

const deleteCustomStyle = async (i) => {
    ElMessageBox.confirm(
        "是否要删除【自定义样式 " + (i + 1) + "】",
        '确认消息', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(async () => {
        loading2.value = true
        try {
            await POST("/api/batch-image-watermarker/delete-custom-style-params", {
                id: customModes.value[i].id
            })
            await toCustomStyle()
        } catch (e) {
            ElMessage.error("加载数据失败：" + e)
        } finally {
            loading2.value = false
        }
    })
}

const checkFont = async (fontName) => {
    let res = await POST("/api/batch-image-watermarker/check-font", {
        "fontName": fontName
    })
    if (!res) {
        let message = "字体 [" + fontName + "] 未安装在系统中"
        ElMessage.error("字体未找到异常：" + message)
        await handleTTF(message)
    }
    return res
}

const addCustomStyle = async () => {
    if (!await checkFont(params.value.fontName)) return
    if (customModes.value.length === limit) {
        ElNotification({
            title: '(´⊙ω⊙`)',
            message: '自定义样式个数达到上限！！！',
            type: 'warning',
            position: 'top-left'
        })
        return
    }
    loading2.value = true
    try {
        customModes.value[mode.value - 1].backgroundImage = backgroundImage.value
        customModes.value[mode.value - 1].previewImage = previewImage.value
        customModes.value[mode.value - 1].params = params.value
        if (mode.value === customModes.value.length) {
            await Post("/api/batch-image-watermarker/add-custom-style-params", customModes.value[mode.value - 1])
        } else {
            await Post("/api/batch-image-watermarker/update-custom-style-params", customModes.value[mode.value - 1])
        }
        await toCustomStyle()
    } catch (e) {
        ElMessage.error("加载数据失败：" + e)
    } finally {
        loading2.value = false
    }
}

const handleTTF = async (message) => {
    await ElMessageBox.confirm(
        '请上传 .ttf/.otf 字体文件',
        message,
        {
            confirmButtonText: '上传字体文件',
            cancelButtonText: '取消',
            type: 'error',
        }
    ).then(() => {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = '.ttf,.otf'
        input.onchange = (e) => {
            const file = e.target.files[0];
            if (!file) return;
            handleUploadTTF(file);
        };
        input.click();
    }).catch(() => {});
};

const handleUploadTTF = async (file) => {
    const isValidExtension = file.name.toLowerCase().endsWith('.ttf') || file.name.toLowerCase().endsWith('.otf')
    if (!isValidExtension) {
        ElMessage.warning('文件类型不正确，请上传有效的 .ttf/.otf 字体文件')
        return
    }
    const maxFileSize = 50 * 1024 * 1024;
    if (file.size > maxFileSize) {
        ElMessage.warning('文件大小不得超过50MB');
        return;
    }
    const formData = new FormData();
    formData.append('fontFile', file);
    try {
        const response = await axios.post('/api/batch-image-watermarker/handle-upload-ttf', formData, {
            withCredentials: true,
        });
        params.value.fontName = fontNameTmp.value = response.data
        ElMessage.success('字体文件上传成功！')
    } catch (e) {
        ElMessage.error('字体文件上传失败！')
    }
};

const handleUpload = (file) => {
    const isJpeg = file.type === 'image/jpeg';
    if (!isJpeg) {
        ElMessage.error('只能上传 JPEG 格式图片！');
        return false;
    }
    const reader = new FileReader();
    reader.onload = (e) => {
        let imageUrl = e.target.result;
        backgroundImage.value = imageUrl;
        toPreviewImage();
        const img = new Image();
        img.src = imageUrl;
        img.onload = () => {
            const width = img.naturalWidth, height = img.naturalHeight;
            if (width !== 1200 || height !== 1200) {
                ElMessage.warning('图片分辨率不是 1200×1200 像素！');
            }
        };
    };
    reader.readAsDataURL(file);
    return false;
}

const customUpload = async (options) => {
    if (!await checkFont(params.value.fontName)) return
    isRunning.value = true
    try {
        successMatch.value = tableNoMatch.value = imageNoMatch.value = []
        progress.value = 0
        const {file} = options;
        const formData = new FormData();
        formData.append('username', store.auth.user.username);
        formData.append('file', file);
        formData.append('params', JSON.stringify(params.value));
        try {
            await axios.post('/api/batch-image-watermarker/start-task', formData, {
                withCredentials: true,
                responseType: 'blob'
            });
        } catch (e) {
            if (e.response?.status === 413) {
                ElMessage.error("文件大小超过服务器限制（500MB）");
            } else {
                ElMessage.error("上传失败，错误原因：" + (e.message || "未知异常"));
            }
            return;
        }
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

onMounted(async () => {
    presetModes.value = await get("/api/batch-image-watermarker/get-preset-style-params")
    customModes.value = await GET("/api/batch-image-watermarker/get-custom-style-params", {
        username: store.auth.user.username
    })
    defaultBackgroundImage = await get("/api/batch-image-watermarker/get-default-background-image")
    customModes.value.push({
        id: -1,
        username: store.auth.user.username,
        backgroundImage: defaultBackgroundImage,
        previewImage: await post("/api/batch-image-watermarker/get-preview-image", {
            params: params.value,
            base64: defaultBackgroundImage
        }),
        params: JSON.parse(JSON.stringify(defaultParams))
    })
    toParams()
})

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
.block-header {
    color: #409EFF;
    font-size: 16px;
    margin-bottom: 12px;
    font-weight: 500;
}

.selection-style {
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
.selection-style::-webkit-scrollbar {
    height: 8px;
    background: #f5f7fa;
}
.selection-style::-webkit-scrollbar-thumb {
    background: #c1d9ff;
    border-radius: 4px;
}

.control-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(64,158,255,0.1);
}

.params-container {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(64,158,255,0.1);
}
.params-panel {
    height: 600px;
    overflow-y: auto;
    padding-right: 10px;
}

.preview-panel {
    display: flex;
    height: 100%;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.preview-image {
    position: relative;
    aspect-ratio: 1/1;
    width: 100%;
    height: auto;
    min-height: 300px;
    border: 2px dashed #c0c4cc;
    margin-bottom: 15px;
    background: #f8f8f9;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}
.preview-img {
    width: 100%;
    height: 100%;
    object-fit: contain;
}
.preview-btn {
    position: absolute;
    top: -2px;
    right: -2px;
    width: 50px;
    height: 50px;
    background-color: #ff4949;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    opacity: 0;
    transform: translateY(-100%);
    transition:
        opacity 0.2s ease,
        transform 0.2s ease,
        box-shadow 0.2s ease;
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 4px rgba(0,0,0,0.15);
}
.preview-btn.is-visible {
    opacity: 1;
    transform: translateY(0);
}
.preview-btn:hover {
    background-color: #e62d2d;
    transform: translateY(-2px);
    box-shadow: 0 4px 6px rgba(0,0,0,0.25);
}
.btn-icon {
    font-size: 40px;
    color: #fff;
    font-weight: bold;
}

.custom-preview {
    position: relative;
    background: #f8f8f9;
    overflow: hidden;
}
.custom-btn {
    position: absolute;
    top: -2px;
    right: -2px;
    width: 50px;
    height: 50px;
    background-color: #ff4949;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    opacity: 0;
    transform: translateY(-100%);
    transition:
        opacity 0.2s ease,
        transform 0.2s ease,
        box-shadow 0.2s ease;
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 4px rgba(0,0,0,0.15);
}
.custom-btn.is-visible {
    opacity: 1;
    transform: translateY(0);
}
.custom-btn:hover {
    background-color: #e62d2d;
    transform: translateY(-2px);
    box-shadow: 0 4px 6px rgba(0,0,0,0.25);
}

.sub-panel {
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    position: relative;
    h4 {
        margin: -5px 0 15px -10px;
        padding: 0 10px;
        color: #409EFF;
        font-size: 14px;
        background: linear-gradient(to right, #f0f7ff, transparent);
    }
}
.sub-panel {
    border-left: 3px solid #409EFF;
    padding-left: 15px;
    margin-left: -10px;
    h4 {
        margin-top: 0;
        font-size: 13px;
        color: #606266;
    }
}

.radio-container {
    margin: 10px 0;
}

.optional-item {
    padding: 15px;
}
.optional-row {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    gap: 15px;
}

.button {
    align-self: center;
    height: 3rem;
    padding: 0 2rem;
    border-radius: 1.5rem;
    background: #999;
    color: #fff;
    border: none;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: background 0.3s ease;
    text-decoration: none;
    font-size: 0.875rem;
    align-content: center;
    text-align: center;
}
.button:hover {
    background: linear-gradient(
        82.3deg,
        rgba(150, 93, 233, 1) 10.8%,
        rgba(99, 88, 238, 1) 94.3%
    );
}
.button::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    transform: scaleX(0);
    transform-origin: 0 50%;
    width: 100%;
    height: 100%;
    border-radius: inherit;
    background: linear-gradient(
        82.3deg,
        rgba(150, 93, 233, 1) 10.8%,
        rgba(99, 88, 238, 1) 94.3%
    );
    transition: transform 0.475s ease;
}
.button:hover::before {
    transform: scaleX(1);
}
.button-content {
    position: relative;
    z-index: 1;
}

.collapse-title {
    font-size: 20px;
}

.header-with-button {
    position: relative;
    padding-right: 40px;
}
.right-button {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    padding: 8px 16px;
}

.content-container {
    position: relative;
}
.content-container.disabled {
    pointer-events: none;
}
.mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(250, 250, 250, 0.5);
    z-index: 100;
    pointer-events: none;
}
</style>
