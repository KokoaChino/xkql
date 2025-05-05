import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from "@/stores";
import { _GET } from "@/net/index.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/common/Welcome.vue'),
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: () => import('@/components/welcome/LoginPage.vue')
                }, {
                    path: 'register',
                    name: 'welcome-register',
                    component: () => import('@/components/welcome/RegisterPage.vue')
                }, {
                    path: 'forget',
                    name: 'welcome-forget',
                    component: () => import('@/components/welcome/ForgetPage.vue')
                }
            ]
        }, {
            path: '/index',
            name: 'index',
            component: () => import('@/views/common/Index.vue'),
            meta: {
                title: '主页',
                icon: '/favicon.png'
            }
        }, {
            path: '/test',
            name: 'test',
            component: () => import('@/views/common/Test.vue'),
            meta: {
                title: 'Test',
                icon: '/icon/Test.png'
            }
        }, {
            path: '/guess-number',
            name: 'guess-number',
            component: () => import('@/views/guessNumber/GuessNumber.vue'),
            meta: {
                title: '猜数字游戏',
                icon: '/icon/GuessNumber.png'
            }
        }, {
            path: '/guess-number/playing',
            name: 'guess-number-playing',
            component: () => import('@/views/guessNumber/Playing.vue')
        }, {
            path: '/guess-number/historical-record',
            name: 'guess-number-historical-record',
            component: () => import('@/views/guessNumber/HistoricalRecord.vue')
        }, {
            path: '/character-map',
            name: 'character-map',
            component: () => import('@/views/characterMap/CharacterMap.vue'),
            meta: {
                title: '角色自截图',
                icon: '/icon/CharacterMap.png'
            }
        }, {
            path: '/character-map/sub',
            name: 'character-map/sub',
            component: () => import('@/views/characterMap/CharacterMapSub.vue')
        }, {
            path: '/link-game',
            name: 'link-game',
            component: () => import('@/views/linkGame/LinkGame.vue'),
            meta: {
                title: '连连看游戏',
                icon: '/icon/LinkGame.png'
            }
        }, {
            path: '/link-game/set-game',
            name: 'link-game/set-game',
            component: () => import('@/views/linkGame/Library.vue')
        }, {
            path: '/link-game/game-start',
            name: 'link-game/game-start',
            component: () => import('@/views/linkGame/Playing.vue')
        }, {
            path: '/link-game/historical-record',
            name: 'link-game/historical-record',
            component: () => import('@/views/linkGame/HistoricalRecord.vue')
        }, {
            path: '/crawler',
            name: 'crawler',
            component: () => import('@/views/crawler/Crawler.vue'),
            meta: {
                title: '简单爬虫脚本',
                icon: '/icon/LinkGame.png'
            }
        }, {
            path: '/batch-image-watermarker',
            name: 'batch-image-watermarker',
            component: () => import('@/views/batchImageWatermarker/BatchImageWatermarker.vue'),
            meta: {
                title: '批量图片水印处理',
                icon: '/icon/BatchImageWatermarker.png'
            }
        }, {
            path: '/document',
            name: 'document',
            component: () => import('@/views/common/Document.vue'),
            meta: {
                title: '文档中心',
                icon: '/icon/Document.png'
            }
        }
    ]
})

router.beforeEach(async (to, from, next) => {
    const store = useStore();
    try {
        await _GET('/api/user/me',
            (message) => store.auth.user = message,
            () => store.auth.user = null
        );
    } catch (e) {
        store.auth.user = null
    }
    if (store.auth.user && to.name.startsWith('welcome')) {
        next('/index');
    } else if (!store.auth.user && !to.name.startsWith('welcome-')) {
        next('/');
    } else {
        next();
    }
});

export default router
