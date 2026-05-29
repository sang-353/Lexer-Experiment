import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import KeywordManage from '../views/KeywordManage.vue'
import LexerAnalysis from '../views/LexerAnalysis.vue'

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/keywords', name: 'Keywords', component: KeywordManage },
  { path: '/lexer', name: 'Lexer', component: LexerAnalysis }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
