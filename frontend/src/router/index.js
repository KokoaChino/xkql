import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from "@/stores";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL), // 使用 HTML5 的 history 模式，基础 URL 来自环境变量
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
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
            component: () => import('@/views/IndexView.vue'),
            meta: {
                title: '主页',
                icon: '/favicon.png'
            }
        }, {
            path: '/test',
            name: 'test',
            component: () => import('@/views/Test.vue'),
            meta: {
                title: 'Test',
                icon: '/icon/Test.png'
            }
        }, {
            path: '/guess-number',
            name: 'guess-number',
            component: () => import('@/views/GuessNumber.vue'),
            meta: {
                title: '猜数字游戏',
                icon: '/icon/GuessNumber.png'
            }
        }, {
            path: '/guess-number/playing',
            name: 'guess-number-playing',
            component: () => import('@/views/GuessNumberPlaying.vue')
        }, {
            path: '/guess-number/historical-record',
            name: 'guess-number-historical-record',
            component: () => import('@/views/GuessNumberHistoricalRecord.vue')
        }, {
            path: '/character-map',
            name: 'character-map',
            component: () => import('@/views/CharacterMap.vue'),
            meta: {
                title: '角色自截图',
                icon: '/icon/CharacterMap.png'
            }
        }, {
            path: '/character-map/sub',
            name: 'character-map/sub',
            component: () => import('@/views/CharacterMapSub.vue')
        }, {
            path: '/link-game',
            name: 'link-game',
            component: () => import('@/views/LinkGame.vue')
        }, {
            path: '/link-game/set-game',
            name: 'link-game/set-game',
            component: () => import('@/views/LinkGameLibrary.vue')
        }, {
            path: '/link-game/game-start',
            name: 'link-game/game-start',
            component: () => import('@/views/LinkGamePlaying.vue')
        }, {
            path: '/link-game/historical-record',
            name: 'link-game/historical-record',
            component: () => import('@/views/LinkGameHistoricalRecord.vue')
        }, {
            path: '/echo-scoring-system',
            name: 'echo-scoring-system',
            component: () => import('@/views/EchoScoringSystem.vue')
        }, {
            path: '/echo-scoring-system/main',
            name: 'echo-scoring-system/main',
            component: () => import('@/views/EchoScoringSystemMain.vue')
        }
    ]
})

router.beforeEach((to, from, next) => { // 在每次路由导航发生前进行拦截
    const store = useStore() // 使用 useStore 获取 Vuex 状态管理实例
    if(store.auth.user != null && to.name.startsWith('welcome-')) { // 如果用户已登录且目标路由是 welcome 开头的路由
        next('/index') // 重定向到 '/index' 页面
    } else if(store.auth.user == null && to.fullPath.startsWith('/index')) { // 如果用户未登录且目标路由是 '/index'
        next('/') // 重定向到根页面 '/'
    } else if(to.matched.length === 0) { // 如果没有匹配到任何路由
        next('/index') // 重定向到 '/index' 页面
    } else {
        next() // 否则，继续导航
    }
})

export default router // 导出 router 实例，以便在其他地方使用
