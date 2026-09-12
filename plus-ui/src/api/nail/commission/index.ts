import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailCommissionVO, NailCommissionForm, NailCommissionQuery } from './types';

export const listNailCommission = (query?: NailCommissionQuery): AxiosPromise<NailCommissionVO[]> => {
  return request({ url: '/nail/commission/list', method: 'get', params: query });
};

export const getNailCommission = (id: string | number): AxiosPromise<NailCommissionVO> => {
  return request({ url: '/nail/commission/' + id, method: 'get' });
};

export const addNailCommission = (data: NailCommissionForm) => {
  return request({ url: '/nail/commission', method: 'post', data });
};

export const updateNailCommission = (data: NailCommissionForm) => {
  return request({ url: '/nail/commission', method: 'put', data });
};

export const delNailCommission = (id: string | number | Array<string | number>) => {
  return request({ url: '/nail/commission/' + id, method: 'delete' });
};

