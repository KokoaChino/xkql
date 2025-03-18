<template>
    <Title/>
    <div class="main-container">
        <div class="container">
            <div class="for" v-for="(url, index) in urls" :key="index">
                <div class="card-sub">
                    <img :src="url" :alt="url" class="card__image">
                    <div class="card__content">
                        <p class="card__title">{{ list[index][0] }}</p>
                        <p class="card__description">
                            {{ map[list[index][0]] }}
                        </p>
                        <div class="grid">
                            <button class="button" @click="move(list[index][0])">
                                <span class="button-content">查看图片</span>
                            </button>
                            <a class="button" :href="list[index][2]" :download="list[index][3]">
                                <span class="button-content">下载图片</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>



<script setup>
import { get, post } from "@/net/index.js";
import router from "@/router/index.js";
import { ref, onMounted } from 'vue';
import Title from '@/components/layout/Title.vue';
import { provide } from "vue";

provide("title", "角色自截图");

const list = ref([]), urls = ref([])
const map = ref({
    '保登心爱': '个性活泼开朗，不过带有粗心大意的小毛病。寄住在Rabbit House咖啡厅的女孩子。十分喜欢可爱和毛茸茸的东西，像妹妹一样疼爱着香风智乃。 擅长数学与物理，有一瞬间算出三位数乘法的实力，而文科的成绩却非常惨淡。',
    '香风智乃': '咖啡店Rabbit House老板的孙女。身形娇小却意外地能干，店内杂务也几乎由她一手包办，个性冷静又沉默寡言，但其实是在人际交往上有点笨拙。不坦率，超容易害羞，招牌动作是将盘子之类的遮住自己的嘴巴，工作时只要一有空就会偷偷写作业。虽然年纪小，但十分在意自己的身高和身材，努力不吃甜食。言行举止颇为成熟，偶尔有孩子气的一面，但自己并不喜欢被当成小孩子看待。休息的日子喜欢在家里度过，爱好是国际象棋、瓶中船、拼图。羡慕能够轻松与他人自来熟的心爱，自己也不明白这是什么原因。',
    '天天座理世': '保登心爱在Rabbit House打工的前辈。爱吐槽，其实非常会照顾人。因为爸爸是军人的关系，所以行事作风也很有军人的感觉，力气也很大，可以轻松拿重物。招牌发型是双马尾，如果被人称赞会更卖力的把事情做好。',
    '宇治松千夜': '日式甜点屋甘兔庵老板的女儿，心爱的同班同学。擅长做各式各样的和果子，平时最大的兴趣是把刚研发完成的和果子取诗情画意的名字。是个温柔又有气质的少女，平常给人聪明的印象，某些时候却会变得傻傻的。平常也担任装傻的角色，会和心爱临时组成相声搭档。不擅长运动，也没什么体力，但非常擅长躲避球中避开飞来的球球。',
    '桐间纱路': '住在甘兔庵的仓库的少女，千夜的青梅竹马。个性有点小别扭又容易害羞，华丽的金色鬈发总让人误以为是千金大小姐，但是实际上过着贫困的生活。为了不想让其他人知道，便养成了大小姐的言行举止。为了赚钱，平常到多家店铺打工。',
    '条河麻耶': '是香风智乃、奈津惠的中学同学兼好友。性格非常活泼，有些大大咧咧，平时会不带敬语地直呼别人的名字。有些小恶魔系，喜欢恶作剧，也善于靠卖萌让心爱、理世等人屈服。',
    '奈津惠': '香风智乃的同龄好友，“智麻惠队”成员之一。柔和而友善，个性淡淡。很甜蜜，总是心情愉快，但是自己确实不愿意在别人面前做事。尽管会不经常炫耀，但确实是一位才华横溢的芭蕾舞演员，以举止优雅而著称。',
    '保登摩卡': '保登心爱的姐姐，是四兄弟姐妹中最小的心爱所模仿、崇拜的存在，充满了姐姐的气场。在老家的面包房中工作，擅长做松松软软的面包。天然、喜欢可爱的事物，但整体性格比心爱要成熟稳重一点。妹控一枚，十分在意心爱对自己的感受，会因为心爱生自己的气而惊慌失措和消沉。会把比自己小的女孩当做自己的妹妹，摸头杀瞬间攻略理世和智乃酱。',
    '青山蓝山': '职业小说作家，最有名的作品是《咖啡因战士》。外表少年，内心成熟，拥有沙质卷发和天蓝色眼睛。性格精致，说话慢条斯理，善于解读人心，喜欢捉弄后辈。曾因丢失笔而辞去小说家工作，在Rabbit House打工。笔失而复得后，她重拾信心，继续写作，并以纱路为灵感创作新小说《咖啡因战士》，同时在酒吧兼差。兴趣广泛，喜欢观察人以寻找创作灵感，偶尔帮助千夜构思新作名字，但家务能力欠佳。',
    '提比': '是香风智乃的爷爷，是一只会说话的安哥拉兔。也是兔子屋的老板，会占卜。饲养在Rabbit House。它的特点是蓬松的头发，通常在智乃的头顶就位。看起来智乃的爷爷有时会从这只兔子发出声音，但事实是个谜。',
    '豆馅': '是甘兔庵的招牌兔，一般没有大事就不会动，是一只害羞的公兔，有一天遇到来店里的提比因此恋上。偶尔会被乌鸦抓到天上，但是掉下来时都会掉到食物上。',
    '野雁': '桐间纱路收留的兔子。毛发整体为灰色，造型从各种意义上都非常像一个不良少兔，各种细节体现出叛逆和不良的性格。',
    '宇佐美瑞希': '美术部中唯一的普通人，也是唯一在正常画图的部员。憧憬内卷昴的绘画才能。特征为褐色短发、戴着兔子发饰。性格纯真且个性温柔，很容易被捉弄。偶尔会因为被捉弄、愤怒或害羞的时候使用暴力，被昴戏称为邪恶暴力女干部。常因为害羞而难以表达对昴的感情，但实际上相当关心与在意内卷昴。',
    '塞西莉亚': '某天来到劳伦斯居住街道的圣女。和劳伦斯一起生活。被街上的人们所仰慕，也会在教会进行烦恼咨询。虽然人们面前表现得很完美，但在劳伦斯面前却呈现出一幅懒散的样子。习惯称呼“劳伦斯”为“劳伦”。',
})
const order = ['保登心爱', '香风智乃', '天天座理世', '宇治松千夜', '桐间纱路', '条河麻耶', '奈津惠', '保登摩卡', '青山蓝山', '提比', '豆馅', '野雁', '宇佐美瑞希', '塞西莉亚']

const move = async (name) => {
    await post("/api/character-map/set-name", name)
    await router.push('/character-map/sub')
}

onMounted( async () => {
    list.value = await get("/api/character-map/get-character-map-catalogue");
    list.value.sort((a, b) => order.indexOf(a[0]) - order.indexOf(b[0]))
    for (let i = 0; i < list.value.length; i++) {
        urls.value.push('/角色图/' + list.value[i][0] + '/' + list.value[i][1]);
    }
})
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}

.main-container {
    width: 100%;
    max-width: 1680px;
    margin: 0 auto;
    box-sizing: border-box;
}
.container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
    gap: 16px;
    padding: 16px;
}

body {
    margin: 0;
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
}

.card-sub {
    position: relative;
    width: 100%;
    height: 100%;
    background-color: #f2f2f2;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    perspective: 1000px;
    box-shadow: 0 0 0 5px #ffffff80;
    transition: all 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.card__image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: all 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.card-sub:hover {
    transform: scale(1.05);
    box-shadow: 0 8px 16px rgba(255, 255, 255, 0.2);
}
.card__content {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    padding: 20px;
    box-sizing: border-box;
    background-color: #f2f2f2;
    transform: rotateX(-90deg);
    transform-origin: bottom;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    transition: transform 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.card-sub:hover .card__content {
    transform: rotateX(0deg);
}
.card__title {
    margin: 0;
    font-size: 24px;
    color: #333;
    font-weight: 700;
}
.card__description {
    margin: 10px 0;
    font-size: 14px;
    color: #777;
    line-height: 1.4;
}

.grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    width: 100%;
    gap: 20px;
}

.button {
    align-self: center;
    height: 3rem;
    padding: 0 2rem;
    border-radius: 1.5rem;
    background: #3d3a4e;
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
</style>
