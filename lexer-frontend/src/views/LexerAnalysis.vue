<template>
  <div class="lexer-analysis">
    <div class="panels">
      <div class="panel-left">
        <div class="panel-header">源代码</div>
        <el-input
          v-model="sourceCode"
          type="textarea"
          :rows="22"
          placeholder="请输入或粘贴源代码..."
        />
        <div class="actions">
          <el-button type="primary" @click="handleAnalyze" :loading="loading">词法分析</el-button>
          <el-button @click="handleClear">清空</el-button>
        </div>
      </div>
      <div class="panel-right">
        <div class="panel-header">Token 序列</div>
        <el-table :data="tokens" border stripe height="500" empty-text="点击「词法分析」开始分析">
          <el-table-column prop="line" label="行号" width="60" />
          <el-table-column prop="lexeme" label="单词内容" />
          <el-table-column prop="type" label="单词种类" width="140" />
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { analyzeSource } from '../api/index.js'

const sourceCode = ref(`program p
var i : integer ;
     x : array[1..16] of integer ;
begin
  i := 5 + 10 ;
  x[i] := 0 ;
end .`)
const tokens = ref([])
const loading = ref(false)

async function handleAnalyze() {
  const code = sourceCode.value.trim()
  if (!code) {
    ElMessage.warning('请输入源代码')
    return
  }
  loading.value = true
  try {
    const res = await analyzeSource(code)
    tokens.value = res.data
  } catch {
    ElMessage.error('分析失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

function handleClear() {
  sourceCode.value = ''
  tokens.value = []
}
</script>

<style scoped>
.lexer-analysis { padding: 20px; height: calc(100vh - 80px); }
.panels { display: flex; gap: 20px; height: 100%; }
.panel-left { flex: 1; display: flex; flex-direction: column; }
.panel-right { flex: 1; display: flex; flex-direction: column; }
.panel-header { font-size: 16px; font-weight: bold; margin-bottom: 10px; color: #303133; }
.actions { margin-top: 12px; display: flex; gap: 12px; }
</style>
