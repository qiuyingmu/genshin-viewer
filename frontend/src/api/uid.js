import { getApi } from './client';

export const uidApi = {
  query: (uid) => getApi(`/uid/${uid}`),
};
