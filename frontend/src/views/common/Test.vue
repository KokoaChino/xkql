<template>
    <Title/>
    <div class="container">
        <div>这里是开发者的测试平台哦！</div>
        <div class="emoji">✧⁺(・ω・)⁺✧</div>
    </div>
</template>



<script setup>
import { provide } from 'vue';
import Title from "@/components/layout/Title.vue";

provide("title", "Test");
</script>



<style scoped>
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 100px;
}

.emoji {
    animation: spin 2s infinite linear;
    margin-left: 10px;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>
