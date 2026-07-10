import { getApi, postApi, delApi } from './client';

export const characterApi = {
  list: () => getApi('/characters'),
  create: (data) => postApi('/characters', data),
  delete: (id) => delApi(`/characters/${id}`),
};
