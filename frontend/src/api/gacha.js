import { getApi, postApi, delApi } from './client';

export const gachaApi = {
  list: (params) => getApi('/gacha', params),
  importBatch: (data) => postApi('/gacha/import', data),
  stats: () => getApi('/gacha/stats'),
  delete: (id) => delApi(`/gacha/${id}`),
};
