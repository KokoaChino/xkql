<template>
    <div class="container">
        <div class="card">
            <router-view v-slot="{ Component }">
                <transition name="el-fade-in-linear" mode="out-in">
                    <component :is="Component" class="card-content" />
                </transition>
            </router-view>
        </div>
    </div>
</template>



<script setup>


</script>



<style scoped>
.container {
    width: 100vw;
    height: 100vh;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
}

.card {
    width: 400px;
    background: white;
    border-radius: 12px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
    transition: all 0.3s ease;
    z-index: 1;
    height: 100vh;
}

@media (max-width: 600px) {
    .card {
        width: 90%;
        padding: 1.5rem;
    }
}
</style>
