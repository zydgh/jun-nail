import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailEmployeeShiftVO, NailEmployeeShiftForm, NailEmployeeShiftQuery } from './types';

export const listNailEmployeeShift = (query?: NailEmployeeShiftQuery): AxiosPromise<NailEmployeeShiftVO[]> => {
  return request({ url: '/employee/shift/list', method: 'get', params: query });
};

export const getNailEmployeeShift = (id: string | number): AxiosPromise<NailEmployeeShiftVO> => {
  return request({ url: '/employee/shift/' + id, method: 'get' });
};

export const addNailEmployeeShift = (data: NailEmployeeShiftForm) => {
  return request({ url: '/employee/shift', method: 'post', data });
};

export const updateNailEmployeeShift = (data: NailEmployeeShiftForm) => {
  return request({ url: '/employee/shift', method: 'put', data });
};

export const delNailEmployeeShift = (id: string | number | Array<string | number>) => {
  return request({ url: '/employee/shift/' + id, method: 'delete' });
};

