<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">角色练度</h1>
      <p class="page-subtitle">查看你的角色培养进度</p>
      <button class="btn btn-primary" style="position:absolute;top:0;right:0" @click="showAdd=true">+ 添加角色</button>
    </div>

    <div class="char-grid">
      <div v-for="(c,i) in characters" :key="c.id" class="glass" style="overflow:hidden"
        @mouseenter="c._hover=true" @mouseleave="c._hover=false">
        <div :style="{padding:'20px 16px 14px',textAlign:'center',background:`linear-gradient(180deg, ${elements[c.element]||'transparent'}15, transparent)`}">
          <div :style="{display:'inline-flex',alignItems:'center',gap:'6px',fontSize:'0.75rem',padding:'3px 10px',borderRadius:'20px',marginBottom:'8px',background:(elements[c.element]||'#888')+'22',color:elements[c.element]||'#888'}">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor"><circle cx="12" cy="12" r="10" opacity="0.3"/><circle cx="12" cy="12" r="6"/></svg>
            {{ c.element }}
          </div>
          <div :style="{fontFamily:'var(--font-display)',fontSize:'1.1rem',fontWeight:600}">{{ c.charName || c.charId }}</div>
        </div>
        <div style="display:flex;gap:4px;justify-content:center;padding:0 16px 14px">
          <div v-for="n in 6" :key="n" :style="{width:'18px',height:'18px',borderRadius:'50%',border:'2px solid var(--border-subtle)',background: n<=c.constellation?'var(--accent)':'transparent',borderColor: n<=c.constellation?'var(--accent)':'var(--border-subtle)'}"></div>
        </div>
        <div style="padding:0 16px 16px;display:flex;flex-direction:column;gap:6px">
          <div style="display:flex;justify-content:space-between;font-size:0.82rem"><span style="color:var(--text-tertiary)">等级</span><span>Lv.{{ c.level }}</span></div>
          <div style="display:flex;justify-content:space-between;font-size:0.82rem"><span style="color:var(--text-tertiary)">好感度</span><span>{{ c.friendship }}/10</span></div>
          <div style="display:flex;justify-content:space-between;font-size:0.82rem"><span style="color:var(--text-tertiary)">命之座</span><span>{{ c.constellation }}/6</span></div>
          <div style="display:flex;justify-content:space-between;font-size:0.82rem"><span style="color:var(--text-tertiary)">天赋</span><span>{{ talentsStr(c) }}</span></div>
        </div>
        <button v-if="c._hover" class="btn btn-danger btn-sm" style="margin:0 16px 16px;width:calc(100% - 32px)" @click="del(c.id)">删除</button>
      </div>
      <div v-if="!characters.length" style="grid-column:1/-1;text-align:center;padding:60px;color:var(--text-tertiary)">还没有角色数据</div>
    </div>

    <!-- Add Modal -->
    <div v-if="showAdd" class="modal-overlay" @click.self="showAdd=false">
      <div class="modal glass">
        <h3 style="font-family:var(--font-display);font-size:1.2rem;font-weight:600;margin-bottom:20px">添加角色</h3>
        <div class="form-group" style="margin-bottom:14px">
          <label>角色名称</label>
          <input class="form-input" v-model="form.charId" placeholder="角色的英文 ID，如 raiden" />
        </div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:14px">
          <div class="form-group"><label>等级</label><input type="number" class="form-input" v-model.number="form.level" min="1" max="90"></div>
          <div class="form-group"><label>好感度</label><input type="number" class="form-input" v-model.number="form.friendship" min="0" max="10"></div>
        </div>
        <div class="form-group" style="margin-bottom:14px"><label>命之座</label><input type="number" class="form-input" v-model.number="form.constellation" min="0" max="6"></div>
        <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:12px;margin-bottom:14px">
          <div class="form-group"><label>普攻</label><input type="number" class="form-input" v-model.number="form.normal" min="1" max="10"></div>
          <div class="form-group"><label>E技能</label><input type="number" class="form-input" v-model.number="form.skill" min="1" max="10"></div>
          <div class="form-group"><label>Q大招</label><input type="number" class="form-input" v-model.number="form.burst" min="1" max="10"></div>
        </div>
        <div style="display:flex;gap:8px;justify-content:flex-end">
          <button class="btn btn-ghost" @click="showAdd=false">取消</button>
          <button class="btn btn-primary" @click="addChar">添加</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { characterApi } from '@/api/characters';

const characters = ref([]);
const showAdd = ref(false);
const form = ref({ charId:'', level:80, friendship:0, constellation:0, normal:1, skill:1, burst:1 });

const elements = { '火':'#ef7f45','水':'#4cc2f0','风':'#60c5b0','雷':'#b45fe0','草':'#7ec850','冰':'#a0d8ef','岩':'#d4a84b','无':'#ffffff' };

async function load() {
  try {
    const res = await characterApi.list();
    characters.value = (res.data || []).map(c => ({
      ...c,
      talents: typeof c.talents === 'string' ? JSON.parse(c.talents) : (c.talents || {}),
    }));
  } catch (e) { /* ignore */ }
}

function talentsStr(c) {
  const t = c.talents || {};
  return `${t.normal||1}/${t.skill||1}/${t.burst||1}`;
}

async function addChar() {
  try {
    await characterApi.create({
      charId: form.value.charId,
      level: form.value.level,
      friendship: form.value.friendship,
      constellation: form.value.constellation,
      talents: { normal: form.value.normal, skill: form.value.skill, burst: form.value.burst },
    });
    showAdd.value = false;
    load();
  } catch (e) { alert(e.response?.data?.message || '添加失败'); }
}

async function del(id) {
  try { await characterApi.delete(id); load(); }
  catch (e) { alert('删除失败'); }
}

onMounted(load);
</script>
