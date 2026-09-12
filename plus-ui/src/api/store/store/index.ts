import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailStoreVO, NailStoreForm, NailStoreQuery } from './types';

export const listNailStore = (query?: NailStoreQuery): AxiosPromise<NailStoreVO[]> => {
  return request({ url: '/store/store/list', method: 'get', params: query });
};

export const getNailStore = (id: string | number): AxiosPromise<NailStoreVO> => {
  return request({ url: '/store/store/' + id, method: 'get' });
};

export const addNailStore = (data: NailStoreForm) => {
  return request({ url: '/store/store', method: 'post', data });
};

export const updateNailStore = (data: NailStoreForm) => {
  return request({ url: '/store/store', method: 'put', data });
};

export const delNailStore = (id: string | number | Array<string | number>) => {
  return request({ url: '/store/store/' + id, method: 'delete' });
};

