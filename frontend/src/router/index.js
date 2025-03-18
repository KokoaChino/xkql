import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from "@/stores";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/common/WelcomeView.vue'),
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
            component: () => import('@/views/common/IndexView.vue'),
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
            component: () => import('@/views/GuessNumber/GuessNumber.vue'),
            meta: {
                title: '猜数字游戏',
                icon: '/icon/GuessNumber.png'
            }
        }, {
            path: '/guess-number/playing',
            name: 'guess-number-playing',
            component: () => import('@/views/GuessNumber/GuessNumberPlaying.vue')
        }, {
            path: '/guess-number/historical-record',
            name: 'guess-number-historical-record',
            component: () => import('@/views/GuessNumber/GuessNumberHistoricalRecord.vue')
        }, {
            path: '/character-map',
            name: 'character-map',
            component: () => import('@/views/CharacterMap/CharacterMap.vue'),
            meta: {
                title: '角色自截图',
                icon: '/icon/CharacterMap.png'
            }
        }, {
            path: '/character-map/sub',
            name: 'character-map/sub',
            component: () => import('@/views/CharacterMap/CharacterMapSub.vue')
        }, {
            path: '/link-game',
            name: 'link-game',
            component: () => import('@/views/LinkGame/LinkGame.vue')
        }, {
            path: '/link-game/set-game',
            name: 'link-game/set-game',
            component: () => import('@/views/LinkGame/LinkGameLibrary.vue')
        }, {
            path: '/link-game/game-start',
            name: 'link-game/game-start',
            component: () => import('@/views/LinkGame/LinkGamePlaying.vue')
        }, {
            path: '/link-game/historical-record',
            name: 'link-game/historical-record',
            component: () => import('@/views/LinkGame/LinkGameHistoricalRecord.vue')
        }
    ]
})

router.beforeEach((to, from, next) => {
    const store = useStore()
    if(store.auth.user != null && to.name.startsWith('welcome-')) {
        next('/index')
    } else if(store.auth.user == null && to.fullPath.startsWith('/index')) {
        next('/')
    } else if(to.matched.length === 0) {
        next('/index')
    } else {
        next()
    }
})

export default router
