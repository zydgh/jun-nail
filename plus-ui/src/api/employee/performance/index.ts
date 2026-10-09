import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import {
  NailEmployeePerformanceForm,
  NailEmployeePerformanceQuery,
  NailEmployeePerformanceRankVO,
  NailEmployeePerformanceVO
} from './types';

export const listNailEmployeePerformance = (
  query?: NailEmployeePerformanceQuery
): AxiosPromise<NailEmployeePerformanceVO[]> => {
  return request({ url: '/employee/performance/list', method: 'get', params: query });
};

export const getNailEmployeePerformance = (
  id: string | number
): AxiosPromise<NailEmployeePerformanceVO> => {
  return request({ url: `/employee/performance/${id}`, method: 'get' });
};

export const addNailEmployeePerformance = (data: NailEmployeePerformanceForm) => {
  return request({ url: '/employee/performance', method: 'post', data });
};

export const updateNailEmployeePerformance = (data: NailEmployeePerformanceForm) => {
  return request({ url: '/employee/performance', method: 'put', data });
};

export const delNailEmployeePerformance = (
  ids: string | number | Array<string | number>
) => {
  return request({ url: `/employee/performance/${ids}`, method: 'delete' });
};

export const getNailEmployeePerformanceRanking = (
  period: 'day' | 'week' | 'month'
): AxiosPromise<NailEmployeePerformanceRankVO[]> => {
  return request({
    url: '/employee/performance/ranking',
    method: 'get',
    params: { period }
  });
};
