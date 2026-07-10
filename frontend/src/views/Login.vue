<template>
  <div class="login-page">
    <div class="login-card">
      <h1>原神查看器</h1>
      <p>登录后查看和管理你的游戏数据</p>

      <div style="margin-bottom:12px">
        <button class="btn" :class="isLogin?'btn-primary':'btn-ghost'" @click="isLogin=true" style="margin-right:8px">登录</button>
        <button class="btn" :class="!isLogin?'btn-primary':'btn-ghost'" @click="isLogin=false">注册</button>
      </div>

      <div v-if="error" style="color:#ef4444;font-size:0.85rem;margin-bottom:12px;padding:8px 12px;background:rgba(239,68,68,0.08);border-radius:8px">{{ error }}</div>

      <form @submit.prevent="submit">
        <div class="form-group" style="margin-bottom:14px">
          <label style="font-size:0.82rem;color:var(--text-secondary);display:block;margin-bottom:4px">用户名</label>
          <input class="form-input" v-model="username" placeholder="输入用户名" required />
        </div>
        <div class="form-group" style="margin-bottom:20px">
          <label style="font-size:0.82rem;color:var(--text-secondary);display:block;margin-bottom:4px">密码</label>
          <input class="form-input" type="password" v-model="password" placeholder="输入密码" required />
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;justify-content:center;padding:12px" :disabled="loading">
          <span v-if="loading" class="loading-spinner"></span>
          {{ loading ? '处理中...' : (isLogin ? '登录' : '注册') }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const auth = useAuthStore();

const isLogin = ref(true);
const username = ref('');
const password = ref('');
const loading = ref(false);
const error = ref('');

async function submit() {
  error.value = '';
  loading.value = true;
  try {
    if (isLogin.value) {
      await auth.login({ username: username.value, password: password.value });
    } else {
      await auth.register({ username: username.value, password: password.value });
    }
    router.push('/');
  } catch (e) {
    error.value = e.response?.data?.message || e.message || '操作失败';
  } finally {
    loading.value = false;
  }
}
</script>
