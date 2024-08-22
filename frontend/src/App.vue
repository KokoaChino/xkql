<script setup>
import {GET} from "@/net";
import {useStore} from "@/stores";
import router from "@/router";

const store = useStore()

if(store.auth.user == null) {
    GET('/api/user/me', (message) => {
        store.auth.user = message
        router.push('/index')
    }, () => {
        store.auth.user = null
    })
}
</script>

<template>
  <router-view/>
</template>

<style scoped>

</style>
