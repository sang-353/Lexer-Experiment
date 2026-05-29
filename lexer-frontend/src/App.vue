<template>
  <el-container class="app-container">
    <el-header class="app-header">
      <el-menu mode="horizontal" :default-active="activeIndex" router>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/keywords">系统维护</el-menu-item>
        <el-menu-item index="/lexer">词法分析</el-menu-item>
      </el-menu>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const activeIndex = computed(() => route.path)
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
.app-container { height: 100vh; }
.app-header { padding: 0; }
</style>
