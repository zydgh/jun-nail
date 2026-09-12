import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailMemberBalanceLogVO, NailMemberBalanceLogForm, NailMemberBalanceLogQuery } from './types';

export const listNailMemberBalanceLog = (query?: NailMemberBalanceLogQuery): AxiosPromise<NailMemberBalanceLogVO[]> => {
  return request({ url: '/member/balanceLog/list', method: 'get', params: query });
};

export const getNailMemberBalanceLog = (id: string | number): AxiosPromise<NailMemberBalanceLogVO> => {
  return request({ url: '/member/balanceLog/' + id, method: 'get' });
};

export const addNailMemberBalanceLog = (data: NailMemberBalanceLogForm) => {
  return request({ url: '/member/balanceLog', method: 'post', data });
};

export const updateNailMemberBalanceLog = (data: NailMemberBalanceLogForm) => {
  return request({ url: '/member/balanceLog', method: 'put', data });
};

export const delNailMemberBalanceLog = (id: string | number | Array<string | number>) => {
  return request({ url: '/member/balanceLog/' + id, method: 'delete' });
};

