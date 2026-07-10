<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">旅行者笔记</h1>
      <p class="page-subtitle">你的提瓦特冒险概览</p>
    </div>

    <div class="uid-query-card">
      <div class="uid-input-wrapper">
        <span class="uid-prefix">UID</span>
        <input class="uid-input" v-model="uid" placeholder="输入 UID 查询角色展柜" maxlength="9" @keyup.enter="queryUid" />
      </div>
      <button class="btn-uid" @click="queryUid" :disabled="uidLoading">
        <span v-if="uidLoading" class="loading-spinner"></span>
        {{ uidLoading ? '查询中...' : '查询' }}
      </button>
    </div>

    <div v-if="uidError" style="color:#ef4444;font-size:0.85rem;margin-bottom:16px;padding:12px;background:rgba(239,68,68,0.08);border-radius:8px">{{ uidError }}</div>

    <div class="dashboard-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background:linear-gradient(135deg,#a78bfa,#7c3aed)">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><path d="M12 2L20 7V12C20 16.42 16.42 20 12 20C7.58 20 4 16.42 4 12V7L12 2Z"/></svg>
        </div>
        <div><div class="stat-value">{{ stats.artifacts }}</div><div class="stat-label">圣遗物</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:linear-gradient(135deg,#fbbf24,#f59e0b)">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
        </div>
        <div><div class="stat-value">{{ stats.gacha }}</div><div class="stat-label">总祈愿</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:linear-gradient(135deg,#60a5fa,#3b82f6)">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        </div>
        <div><div class="stat-value">{{ stats.characters }}</div><div class="stat-label">角色</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:linear-gradient(135deg,#34d399,#10b981)">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><circle cx="12" cy="8" r="7"/><polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88"/></svg>
        </div>
        <div><div class="stat-value">Lv.{{ stats.avgLevel }}</div><div class="stat-label">平均等级</div></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { artifactApi } from '@/api/artifacts';
import { characterApi } from '@/api/characters';
import { gachaApi } from '@/api/gacha';
import { uidApi } from '@/api/uid';

const uid = ref(localStorage.getItem('last_uid') || '');
const uidLoading = ref(false);
const uidError = ref('');

const stats = reactive({ artifacts: 0, gacha: 0, characters: 0, avgLevel: 0 });

async function loadStats() {
  try {
    const [artRes, charRes, gachaRes] = await Promise.all([
      artifactApi.list(),
      characterApi.list(),
      gachaApi.stats(),
    ]);
    const chars = charRes.data || [];
    stats.artifacts = (artRes.data || []).length;
    stats.characters = chars.length;
    stats.gacha = gachaRes.data?.total || 0;
    stats.avgLevel = chars.length ? Math.round(chars.reduce((s, c) => s + (c.level || 0), 0) / chars.length) : 0;
  } catch (e) { /* ignore */ }
}

async function queryUid() {
  const val = uid.value.trim();
  if (!val || !/^\d{8,9}$/.test(val)) { uidError.value = '请输入正确的 UID（8-9位数字）'; return; }
  uidError.value = '';
  uidLoading.value = true;
  localStorage.setItem('last_uid', val);
  try {
    const res = await uidApi.query(val);
    const data = res.data;
    if (data.avatarInfoList?.length > 0) {
      // Parse and save characters from Enka data
      // For now, show success
      uidError.value = '';
      loadStats();
    }
  } catch (e) {
    uidError.value = e.response?.data?.message || e.message || '查询失败';
  } finally {
    uidLoading.value = false;
  }
}

onMounted(loadStats);
</script>
