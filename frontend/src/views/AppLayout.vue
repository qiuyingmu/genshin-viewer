<template>
  <nav class="sidebar">
    <div class="sidebar-brand" @click="$router.push('/')">
      <div class="brand-icon">
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg>
      </div>
      <span class="brand-text">原神查看器</span>
    </div>

    <div class="sidebar-nav">
      <button class="nav-item" :class="{active:$route.path==='/'}" @click="$router.push('/')">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
        <span>仪表盘</span>
      </button>
      <button class="nav-item" :class="{active:$route.path==='/artifacts'}" @click="$router.push('/artifacts')">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L20 7V12C20 16.42 16.42 20 12 20C7.58 20 4 16.42 4 12V7L12 2Z"/></svg>
        <span>圣遗物</span>
      </button>
      <button class="nav-item" :class="{active:$route.path==='/gacha'}" @click="$router.push('/gacha')">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
        <span>抽卡记录</span>
      </button>
      <button class="nav-item" :class="{active:$route.path==='/characters'}" @click="$router.push('/characters')">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        <span>角色练度</span>
      </button>
    </div>

    <div class="sidebar-footer">
      <div style="font-size:0.75rem;color:var(--text-tertiary);padding:4px 12px 8px">旅行者 {{ auth.username }}</div>
      <button class="nav-item" @click="logout">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
        <span>退出</span>
      </button>
    </div>
  </nav>

  <button class="theme-toggle" @click="toggleTheme">
    <svg width="20" height="20" v-if="isDark" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <circle cx="12" cy="12" r="5"/><line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/><line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
    </svg>
    <svg width="20" height="20" v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>
    </svg>
  </button>

  <main class="main-content">
    <router-view />
  </main>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const auth = useAuthStore();

const isDark = ref(document.documentElement.getAttribute('data-theme') === 'dark' ||
  localStorage.getItem('genshin_theme') === 'dark');

function toggleTheme() {
  isDark.value = !isDark.value;
  document.documentElement.setAttribute('data-theme', isDark.value ? 'dark' : 'light');
  localStorage.setItem('genshin_theme', isDark.value ? 'dark' : 'light');
}

function logout() {
  auth.logout();
  router.push('/login');
}
</script>
