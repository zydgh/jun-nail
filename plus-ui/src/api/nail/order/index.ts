import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailOrderVO, NailOrderForm, NailOrderQuery } from './types';

export const listNailOrder = (query?: NailOrderQuery): AxiosPromise<NailOrderVO[]> => {
  return request({ url: '/nail/order/list', method: 'get', params: query });
};

export const getNailOrder = (id: string | number): AxiosPromise<NailOrderVO> => {
  return request({ url: '/nail/order/' + id, method: 'get' });
};

export const addNailOrder = (data: NailOrderForm) => {
  return request({ url: '/nail/order', method: 'post', data });
};

export const updateNailOrder = (data: NailOrderForm) => {
  return request({ url: '/nail/order', method: 'put', data });
};

export const delNailOrder = (id: string | number | Array<string | number>) => {
  return request({ url: '/nail/order/' + id, method: 'delete' });
};

export const checkoutOrder = (data: any) => {
  return request({ url: '/nail/order/checkout', method: 'post', data });
};

