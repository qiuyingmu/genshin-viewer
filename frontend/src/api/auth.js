import { postApi, getApi } from './client';

export const authApi = {
  login: (data) => postApi('/auth/login', data),
  refresh: (token) => postApi('/auth/refresh', { refreshToken: token }),
  me: () => getApi('/auth/me'),
};
