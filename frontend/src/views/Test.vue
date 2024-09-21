<template>
    <Title/>
</template>



<script setup>
import Title from '@/components/module/Title.vue';
import { provide } from "vue";

provide("title", "Test");
</script>



<style scoped>

</style>
