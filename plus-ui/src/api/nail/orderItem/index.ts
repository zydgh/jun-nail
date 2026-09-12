import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailOrderItemVO, NailOrderItemForm, NailOrderItemQuery } from './types';

export const listNailOrderItem = (query?: NailOrderItemQuery): AxiosPromise<NailOrderItemVO[]> => {
  return request({ url: '/nail/orderItem/list', method: 'get', params: query });
};

export const getNailOrderItem = (id: string | number): AxiosPromise<NailOrderItemVO> => {
  return request({ url: '/nail/orderItem/' + id, method: 'get' });
};

export const addNailOrderItem = (data: NailOrderItemForm) => {
  return request({ url: '/nail/orderItem', method: 'post', data });
};

export const updateNailOrderItem = (data: NailOrderItemForm) => {
  return request({ url: '/nail/orderItem', method: 'put', data });
};

export const delNailOrderItem = (id: string | number | Array<string | number>) => {
  return request({ url: '/nail/orderItem/' + id, method: 'delete' });
};

