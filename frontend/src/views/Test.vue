<template>
    <Title/>
</template>



<script setup>
import Title from '@/components/module/Title.vue';
import { ref, provide } from "vue";

provide("title", "Test");
</script>



<style scoped>

</style>
