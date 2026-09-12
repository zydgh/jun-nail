import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailServiceItemVO, NailServiceItemForm, NailServiceItemQuery } from './types';

export const listNailServiceItem = (query?: NailServiceItemQuery): AxiosPromise<NailServiceItemVO[]> => {
  return request({ url: '/nail/item/list', method: 'get', params: query });
};

export const getNailServiceItem = (id: string | number): AxiosPromise<NailServiceItemVO> => {
  return request({ url: '/nail/item/' + id, method: 'get' });
};

export const addNailServiceItem = (data: NailServiceItemForm) => {
  return request({ url: '/nail/item', method: 'post', data });
};

export const updateNailServiceItem = (data: NailServiceItemForm) => {
  return request({ url: '/nail/item', method: 'put', data });
};

export const delNailServiceItem = (id: string | number | Array<string | number>) => {
  return request({ url: '/nail/item/' + id, method: 'delete' });
};

