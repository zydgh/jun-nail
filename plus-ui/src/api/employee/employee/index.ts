import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailEmployeeVO, NailEmployeeForm, NailEmployeeQuery } from './types';

export const listNailEmployee = (query?: NailEmployeeQuery): AxiosPromise<NailEmployeeVO[]> => {
  return request({ url: '/employee/employee/list', method: 'get', params: query });
};

export const getNailEmployee = (id: string | number): AxiosPromise<NailEmployeeVO> => {
  return request({ url: '/employee/employee/' + id, method: 'get' });
};

export const addNailEmployee = (data: NailEmployeeForm) => {
  return request({ url: '/employee/employee', method: 'post', data });
};

export const updateNailEmployee = (data: NailEmployeeForm) => {
  return request({ url: '/employee/employee', method: 'put', data });
};

export const delNailEmployee = (id: string | number | Array<string | number>) => {
  return request({ url: '/employee/employee/' + id, method: 'delete' });
};

