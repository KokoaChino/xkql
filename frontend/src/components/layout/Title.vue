<template>
    <div class="card-title">
        <div class="text">{{ title }}</div>
    </div>
</template>



<script setup>
import { defineOptions } from 'vue';
import { inject } from "vue";

const title = inject("title");

defineOptions({
    name: "Title"
});
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}

.card-title {
    width: 100%;
    height: 100px;
    border: solid 1px #202222;
    background-size: 20px 20px;
    background: white;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    flex-direction: column;
    color: #fff;
    margin-bottom: 20px;
}
.card-title .text {
    font-weight: bolder;
    font-size: 3rem;
    background: black;
    background-clip: text;
    color: transparent;
}
</style>
