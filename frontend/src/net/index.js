import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";
import { useStore } from "@/stores/index.js";

const defaultError = (err) => {
    const store = useStore();
    if (err.response?.status === 401) {
        store.auth.user = null;
        router.push('/login');
    }
    console.error(err);
}
const defaultFailure = (message) => ElMessage.warning(message)

function _GET(url, success, failure = defaultFailure, error = defaultError) {
    axios.get(url, {
        withCredentials: true
    }).then(({data}) => {
        if (data.success)
            success(data.message, data.status)
        else
            failure(data.message, data.status)
    }).catch(error)
}

function _POST(url, data, success, failure = defaultFailure, error = defaultError) {
    axios.post(url, data, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        withCredentials: true
    }).then(({data}) => {
        if (data.success)
            success(data.message, data.status)
        else
            failure(data.message, data.status)
    }).catch(error)
}

async function get(url) {
    try {
        const res = await axios.get(url, {
            withCredentials: true
        })
        return res.data
    } catch (e) {
        console.error('发送数据时出错：', e)
        throw e;
    }
}

async function post(url, data) {
    try {
        const res = await axios.post(url, JSON.stringify(data), {
            headers: {
                'Content-Type': 'application/json'
            },
            withCredentials: true
        })
        return res.data
    } catch (e) {
        console.error('发送数据时出错：', e)
        throw e;
    }
}

async function Post(url, data) {
    try {
        const res = await axios.post(url, data, {
            headers: {
                'Content-Type': 'application/json'
            },
            withCredentials: true
        })
        return res.data
    } catch (e) {
        console.error('发送数据时出错：', e)
        throw e;
    }
}

async function GET(url, params) {
    try {
        const res = await axios.get(url, {
            params: params,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            withCredentials: true
        })
        return res.data
    } catch (e) {
        console.error('发送数据时出错：', e)
        throw e;
    }
}

async function POST(url, data) {
    try {
        const res = await axios.post(url, data, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            withCredentials: true
        })
        return res.data
    } catch (e) {
        console.error('发送数据时出错：', e)
        throw e;
    }
}

export { _GET, _POST, get, post, GET, POST, Post }
