<template>
  <div class="keyword-manage">
    <div class="toolbar">
      <el-input v-model="newKeyword" placeholder="输入新关键字" style="width: 200px" @keyup.enter="handleAdd" />
      <el-button type="primary" @click="handleAdd">添加</el-button>
    </div>
    <el-table :data="keywords" border stripe style="width: 100%; margin-top: 16px">
      <el-table-column prop="value" label="关键字" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="startEdit(row)">修改</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="editVisible" title="修改关键字" width="400px">
      <el-input v-model="editValue" placeholder="输入新关键字" />
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getKeywords, addKeyword, updateKeyword, deleteKeyword } from '../api/index.js'

const keywords = ref([])
const newKeyword = ref('')
const editVisible = ref(false)
const editValue = ref('')
let editingRow = null

async function loadKeywords() {
  const res = await getKeywords()
  keywords.value = res.data.map(k => ({ value: k }))
}

async function handleAdd() {
  const kw = newKeyword.value.trim()
  if (!kw) return
  try {
    await addKeyword(kw)
    ElMessage.success('添加成功')
    newKeyword.value = ''
    loadKeywords()
  } catch (err) {
    if (err.response?.status === 409) {
      ElMessage.warning('关键字已存在')
    }
  }
}

function startEdit(row) {
  editingRow = row
  editValue.value = row.value
  editVisible.value = true
}

async function handleUpdate() {
  const nk = editValue.value.trim()
  if (!nk) return
  await updateKeyword(editingRow.value, nk)
  ElMessage.success('修改成功')
  editVisible.value = false
  loadKeywords()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该关键字？', '提示', { type: 'warning' })
  await deleteKeyword(row.value)
  ElMessage.success('删除成功')
  loadKeywords()
}

onMounted(loadKeywords)
</script>

<style scoped>
.keyword-manage { padding: 20px; }
.toolbar { display: flex; gap: 12px; }
</style>
