import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailMemberPointLogVO, NailMemberPointLogForm, NailMemberPointLogQuery } from './types';

export const listNailMemberPointLog = (query?: NailMemberPointLogQuery): AxiosPromise<NailMemberPointLogVO[]> => {
  return request({ url: '/member/pointLog/list', method: 'get', params: query });
};

export const getNailMemberPointLog = (id: string | number): AxiosPromise<NailMemberPointLogVO> => {
  return request({ url: '/member/pointLog/' + id, method: 'get' });
};

export const addNailMemberPointLog = (data: NailMemberPointLogForm) => {
  return request({ url: '/member/pointLog', method: 'post', data });
};

export const updateNailMemberPointLog = (data: NailMemberPointLogForm) => {
  return request({ url: '/member/pointLog', method: 'put', data });
};

export const delNailMemberPointLog = (id: string | number | Array<string | number>) => {
  return request({ url: '/member/pointLog/' + id, method: 'delete' });
};

