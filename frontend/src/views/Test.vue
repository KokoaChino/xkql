<template>
    <Title/>
</template>



<script setup>
import Title from '@/components/module/Title.vue';
import { ref, provide, onMounted } from "vue";
import { post, POST } from "@/net"

provide("title", "Test");
</script>



<style scoped>

</style>
