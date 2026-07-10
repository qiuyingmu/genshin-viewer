<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">抽卡记录</h1>
      <p class="page-subtitle">祈愿统计与保底追踪</p>
      <button class="btn btn-primary" style="position:absolute;top:0;right:0" @click="showImport=true">+ 导入记录</button>
    </div>

    <div class="gacha-stats-grid">
      <div class="stat-card" style="justify-content:center"><div style="text-align:center"><div class="stat-value">{{ stats.total }}</div><div class="stat-label">总祈愿</div></div></div>
      <div class="stat-card" style="justify-content:center"><div style="text-align:center"><div class="stat-value">{{ stats.fiveStar }}</div><div class="stat-label">五星</div></div></div>
      <div class="stat-card" style="justify-content:center"><div style="text-align:center"><div class="stat-value">{{ stats.fourStar }}</div><div class="stat-label">四星</div></div></div>
      <div class="stat-card" style="justify-content:center"><div style="text-align:center"><div class="stat-value">{{ pityCount }}</div><div class="stat-label">保底计数</div></div></div>
    </div>

    <div class="glass" style="padding:20px;margin-bottom:16px">
      <div style="display:flex;gap:4px;margin-bottom:16px;background:var(--bg-secondary);border-radius:8px;padding:3px">
        <button v-for="b in banners" :key="b.key" class="btn" :class="banner===b.key?'btn-primary':'btn-ghost'" style="flex:1;justify-content:center" @click="banner=b.key;load()">{{ b.label }}</button>
      </div>
    </div>

    <div style="display:flex;flex-direction:column;gap:6px">
      <div v-for="g in list" :key="g.id" class="glass" style="display:flex;align-items:center;gap:12px;padding:10px 16px">
        <div :style="{width:'28px',height:'28px',borderRadius:'50%',display:'flex',alignItems:'center',justifyContent:'center',fontSize:'0.7rem',fontWeight:700,color:'white',flexShrink:0,background: g.rarity===5?'linear-gradient(135deg,#fbbf24,#f59e0b)':g.rarity===4?'linear-gradient(135deg,#c084fc,#a855f7)':'linear-gradient(135deg,#93c5fd,#3b82f6)'}">{{ g.rarity }}</div>
        <span style="flex:1;font-weight:500;font-size:0.88rem">{{ g.name }}</span>
        <span style="font-size:0.75rem;color:var(--text-tertiary);min-width:40px">{{ g.type }}</span>
        <span style="font-size:0.75rem;color:var(--text-tertiary);min-width:140px;text-align:right">{{ formatTime(g.gachaTime) }}</span>
      </div>
      <div v-if="!list.length" style="text-align:center;padding:60px;color:var(--text-tertiary)">暂无抽卡记录</div>
    </div>

    <!-- Import Modal -->
    <div v-if="showImport" class="modal-overlay" @click.self="showImport=false">
      <div class="modal glass">
        <h3 style="font-family:var(--font-display);font-size:1.2rem;font-weight:600;margin-bottom:12px">导入抽卡记录</h3>
        <p style="font-size:0.85rem;color:var(--text-secondary);margin-bottom:16px">粘贴JSON数据</p>
        <textarea class="form-input" v-model="importData" rows="6" placeholder='[{"name":"雷电将军","rarity":5,"type":"角色","banner":"character","time":"2026-07-08T12:00:00"}]' style="font-family:monospace;font-size:0.8rem;resize:vertical"></textarea>
        <div style="display:flex;gap:8px;justify-content:flex-end;margin-top:16px">
          <button class="btn btn-ghost" @click="showImport=false">取消</button>
          <button class="btn btn-primary" @click="doImport">导入</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { gachaApi } from '@/api/gacha';

const banners = [
  { key:'character', label:'角色活动' }, { key:'weapon', label:'武器活动' },
  { key:'standard', label:'常驻' }, { key:'beginner', label:'新手' },
];
const banner = ref('character');
const list = ref([]);
const stats = reactive({ total:0, fiveStar:0, fourStar:0 });
const showImport = ref(false);
const importData = ref('');
const pityCount = ref(0);

async function load() {
  try {
    const [listRes, statsRes] = await Promise.all([
      gachaApi.list({ banner: banner.value }),
      gachaApi.stats(),
    ]);
    list.value = (listRes.data || []).sort((a, b) => new Date(b.gachaTime) - new Date(a.gachaTime));
    Object.assign(stats, statsRes.data || {});
    const idx = list.value.findIndex(g => g.rarity === 5);
    pityCount.value = idx >= 0 ? idx : list.value.length;
  } catch (e) { /* ignore */ }
}

async function doImport() {
  try {
    const data = JSON.parse(importData.value);
    if (!Array.isArray(data)) throw new Error('需要数组格式');
    await gachaApi.importBatch({ items: data });
    showImport.value = false;
    importData.value = '';
    load();
  } catch (e) { alert('数据格式错误: ' + e.message); }
}

function formatTime(t) {
  if (!t) return '';
  const d = new Date(t);
  return d.toLocaleDateString('zh-CN',{month:'short',day:'numeric'}) + ' ' + d.toLocaleTimeString('zh-CN',{hour:'2-digit',minute:'2-digit'});
}

onMounted(load);
</script>
