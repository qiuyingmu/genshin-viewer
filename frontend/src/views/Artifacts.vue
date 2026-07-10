<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">圣遗物</h1>
      <p class="page-subtitle">管理和浏览你的圣遗物库存</p>
      <button class="btn btn-primary" style="position:absolute;top:0;right:0" @click="showAddModal">+ 添加</button>
    </div>

    <div class="glass" style="display:flex;gap:12px;padding:16px 20px;margin-bottom:20px;align-items:flex-end;flex-wrap:wrap">
      <div style="display:flex;flex-direction:column;gap:4px;min-width:140px">
        <label style="font-size:0.75rem;color:var(--text-tertiary)">套装</label>
        <select class="form-select" v-model="filterSet" @change="load">
          <option value="all">全部套装</option>
          <option v-for="s in sets" :key="s.id" :value="s.id">{{ s.name }}</option>
        </select>
      </div>
      <div style="display:flex;flex-direction:column;gap:4px;min-width:120px">
        <label style="font-size:0.75rem;color:var(--text-tertiary)">部位</label>
        <select class="form-select" v-model="filterSlot" @change="load">
          <option value="all">全部部位</option>
          <option value="flower">生之花</option><option value="plume">死之羽</option>
          <option value="sands">时之沙</option><option value="goblet">空之杯</option>
          <option value="circlet">理之冠</option>
        </select>
      </div>
    </div>

    <div class="artifact-grid">
      <div v-for="(art,i) in artifacts" :key="art.id" class="glass" style="padding:16px;transition:transform 0.3s ease" @mouseenter="art._hover=true" @mouseleave="art._hover=false">
        <div style="display:flex;justify-content:space-between;margin-bottom:10px">
          <div>
            <div style="font-weight:600;font-size:0.88rem">{{ art.setName || art.setId }}</div>
            <div style="font-size:0.75rem;color:var(--text-tertiary)">{{ slotNames[art.slot] || art.slot }}</div>
          </div>
          <div style="display:flex;gap:2px;color:#fbbf24">
            <span v-for="n in art.stars" :key="n">&#9733;</span>
          </div>
        </div>
        <div style="display:flex;justify-content:space-between;padding:8px 0;border-bottom:1px solid var(--border-subtle)">
          <span style="font-size:0.82rem;color:var(--text-secondary)">{{ art.mainStat }}</span>
          <span style="font-weight:700">{{ art.mainValue }}</span>
        </div>
        <div v-if="art.subs" style="display:flex;flex-direction:column;gap:3px;margin-top:6px">
          <div v-for="s in art.subs" :key="s.name" style="display:flex;justify-content:space-between;font-size:0.8rem">
            <span style="color:var(--text-tertiary)">{{ s.name }}</span>
            <span style="font-weight:500">{{ s.value }}</span>
          </div>
        </div>
        <button v-if="art._hover" class="btn btn-danger btn-sm" style="margin-top:8px;width:100%" @click="del(art.id)">删除</button>
      </div>
      <div v-if="!artifacts.length" style="grid-column:1/-1;text-align:center;padding:60px;color:var(--text-tertiary)">
        还没有圣遗物，点击右上角添加
      </div>
    </div>

    <!-- Add Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal=false">
      <div class="modal glass">
        <h3 class="modal-title" style="font-family:var(--font-display);font-size:1.2rem;font-weight:600;margin-bottom:20px">添加圣遗物</h3>
        <form @submit.prevent="addArtifact">
          <div class="form-group" style="margin-bottom:14px">
            <label>套装</label>
            <select class="form-select" v-model="form.setId" required>
              <option v-for="s in sets" :key="s.id" :value="s.id">{{ s.name }}</option>
            </select>
          </div>
          <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:14px">
            <div class="form-group"><label>部位</label><select class="form-select" v-model="form.slot"><option value="flower">生之花</option><option value="plume">死之羽</option><option value="sands">时之沙</option><option value="goblet">空之杯</option><option value="circlet">理之冠</option></select></div>
            <div class="form-group"><label>星级</label><select class="form-select" v-model="form.stars"><option value="5">5星</option><option value="4">4星</option></select></div>
          </div>
          <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:14px">
            <div class="form-group"><label>主属性</label><input class="form-input" v-model="form.mainStat" placeholder="如 暴击率" required></div>
            <div class="form-group"><label>主属性值</label><input class="form-input" v-model="form.mainValue" placeholder="如 31.1%"></div>
          </div>
          <div style="display:flex;gap:8px;justify-content:flex-end;margin-top:20px">
            <button type="button" class="btn btn-ghost" @click="showModal=false">取消</button>
            <button type="submit" class="btn btn-primary">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { artifactApi } from '@/api/artifacts';

const artifactSets = [
  '悠古的磐岩','血染的骑士道','炽烈的炎之魔女','昔日宗室之仪','角斗士的终幕礼',
  '流浪大地的乐团','如雷的盛怒','翠绿之影','被怜爱的少女','逆飞的流星',
  '千岩牢固','苍白之火','绝缘之旗印','追忆之注连','华馆梦醒形骸记',
  '海染砗磲','辰砂往生录','来歆余响','深林的记忆','饰金之梦',
  '沙上楼阁史话','乐园遗落之花','花海甘露之光','水仙之梦','逐影猎人',
  '黄金剧团','烬城勇者绘卷','黑曜秘典','未竟的遐思','谐律异想断章',
];
const sets = ref(artifactSets.map(n => ({ id: n, name: n })));
const artifacts = ref([]);
const filterSet = ref('all');
const filterSlot = ref('all');
const showModal = ref(false);
const slotNames = { flower:'生之花', plume:'死之羽', sands:'时之沙', goblet:'空之杯', circlet:'理之冠' };

const form = ref({ setId:'', slot:'flower', stars:5, mainStat:'', mainValue:'' });

async function load() {
  try {
    const res = await artifactApi.list({ set: filterSet.value, slot: filterSlot.value });
    artifacts.value = (res.data || []).map(a => ({
      ...a,
      subs: typeof a.subs === 'string' ? JSON.parse(a.subs) : (a.subs || [])
    }));
  } catch (e) { /* ignore */ }
}

async function addArtifact() {
  try {
    await artifactApi.create({
      setId: form.value.setId,
      slot: form.value.slot,
      stars: parseInt(form.value.stars),
      level: 0,
      mainStat: form.value.mainStat,
      mainValue: form.value.mainValue,
      subs: [],
    });
    showModal.value = false;
    form.value = { setId:'', slot:'flower', stars:5, mainStat:'', mainValue:'' };
    load();
  } catch (e) { alert(e.response?.data?.message || '添加失败'); }
}

function showAddModal() { showModal.value = true; }

async function del(id) {
  try { await artifactApi.delete(id); load(); }
  catch (e) { alert('删除失败'); }
}

onMounted(() => { load(); });
</script>
