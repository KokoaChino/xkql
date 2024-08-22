import axios from "axios";
import {ElMessage} from "element-plus";

const defaultError = () => ElMessage.error('发生了一些错误，请联系管理员')
const defaultFailure = (message) => ElMessage.warning(message)

function POST(url, data, success, failure = defaultFailure, error = defaultError) {
    axios.post(url, data, { // 使用 axios 发送一个 POST 请求到指定的 url，携带 data 作为请求体
        headers: { // 设置请求头
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        withCredentials: true // 允许携带凭据
    }).then(({data}) => { // 请求成功后，使用解构赋值获取返回的 data
        if(data.success)
            success(data.message, data.status)
        else
            failure(data.message, data.status)
    }).catch(error) // 请求失败时，调用error函数处理错误
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
    }
}

function GET(url, success, failure = defaultFailure, error = defaultError) {
    axios.get(url, { // 使用 axios 发送一个 GET 请求到指定的 url
        withCredentials: true // 允许携带凭据
    }).then(({data}) => { // 请求成功后，使用解构赋值获取返回的 data
        if(data.success)
            success(data.message, data.status)
        else
            failure(data.message, data.status)
    }).catch(error) // 请求失败时，调用error函数处理错误
}

async function get(url) {
    try {
        const res = await axios.get(url, {
            withCredentials: true
        })
        return res.data
    } catch (e) {
        console.error('发送数据时出错：', e)
    }
}

export { GET, POST, post, get }