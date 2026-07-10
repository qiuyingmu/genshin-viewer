import { getApi, postApi, delApi } from './client';

export const artifactApi = {
  list: (params) => getApi('/artifacts', params),
  create: (data) => postApi('/artifacts', data),
  delete: (id) => delApi(`/artifacts/${id}`),
};
