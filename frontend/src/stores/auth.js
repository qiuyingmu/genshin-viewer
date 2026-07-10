import { defineStore } from 'pinia';
import { authApi } from '@/api/auth';
import { ref, computed } from 'vue';

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null);
  const accessToken = ref(localStorage.getItem('access_token') || '');
  const refreshToken = ref(localStorage.getItem('refresh_token') || '');

  const isLoggedIn = computed(() => !!accessToken.value);
  const username = computed(() => user.value?.username || '');

  async function login(data) {
    const res = await authApi.login(data);
    setTokens(res.data.accessToken, res.data.refreshToken);
    user.value = res.data.user;
    return res.data;
  }

  async function fetchMe() {
    try {
      const res = await authApi.me();
      user.value = res.data;
    } catch {
      logout();
    }
  }

  function setTokens(access, refresh) {
    accessToken.value = access;
    refreshToken.value = refresh;
    localStorage.setItem('access_token', access);
    localStorage.setItem('refresh_token', refresh);
  }

  function logout() {
    user.value = null;
    accessToken.value = '';
    refreshToken.value = '';
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }

  // Initialize from localStorage
  if (accessToken.value) {
    fetchMe();
  }

  return { user, accessToken, refreshToken, isLoggedIn, username, login, logout, fetchMe };
});
