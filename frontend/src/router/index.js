import { createRouter, createWebHistory } from 'vue-router'
import {useStore} from "@/stores";

const router = createRouter({ // 创建 router 实例并配置路由
    history: createWebHistory(import.meta.env.BASE_URL), // 使用 HTML5 的 history 模式，基础 URL 来自环境变量
    routes: [ // 定义一个数组，包含所有的路由配置
        {
            path: '/', // 根路径
            name: 'welcome', // 路由的名称为 'welcome'
            component: () => import('@/views/WelcomeView.vue'), // 使用懒加载方式引入 WelcomeView 组件
            children: [ // 定义该路由的子路由
                {
                    path: '', // 空路径
                    name: 'welcome-login', // 子路由名称为 'welcome-login'
                    component: () => import('@/components/welcome/LoginPage.vue') // 懒加载 LoginPage 组件
                }, {
                    path: 'register', // 路径为 'register'
                    name: 'welcome-register', // 子路由名称为 'welcome-register'
                    component: () => import('@/components/welcome/RegisterPage.vue') // 懒加载 RegisterPage 组件
                }, {
                    path: 'forget', // 路径为 'forget'
                    name: 'welcome-forget', // 子路由名称为 'welcome-forget'
                    component: () => import('@/components/welcome/ForgetPage.vue') // 懒加载 ForgetPage 组件
                }
            ]
        }, {
            path: '/index', // 路径为 '/index'
            name: 'index', // 路由名称为 'index'
            component: () => import('@/views/IndexView.vue') // 懒加载 IndexView 组件
        }, {
            path: '/guess-number',
            name: 'guess-number',
            component: () => import('@/views/GuessNumber.vue'),
        }, {
            path: '/guess-number/playing',
            name: 'guess-number-playing',
            component: () => import('@/views/GuessNumberPlaying.vue')
        }, {
            path: '/guess-number/historical-record',
            name: 'guess-number-historical-record',
            component: () => import('@/views/GuessNumberHistoricalRecord.vue')
        }
    ]
})

router.beforeEach((to, from, next) => { // 在每次路由导航发生前进行拦截
    const store = useStore() // 使用 useStore 获取 Vuex 状态管理实例
    if(store.auth.user != null && to.name.startsWith('welcome-')) { // 如果用户已登录且目标路由是 welcome 开头的路由
        next('/index') // 重定向到 '/index' 页面
    } else if(store.auth.user == null && to.fullPath.startsWith('/index')) { // 如果用户未登录且目标路由是 '/index'
        next('/') // 重定向到根页面 '/'
    } else if(to.matched.length === 0){ // 如果没有匹配到任何路由
        next('/index') // 重定向到 '/index' 页面
    } else {
        next() // 否则，继续导航
    }
})

export default router // 导出 router 实例，以便在其他地方使用
