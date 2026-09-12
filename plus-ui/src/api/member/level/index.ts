import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailMemberLevelVO, NailMemberLevelForm, NailMemberLevelQuery } from './types';

export const listNailMemberLevel = (query?: NailMemberLevelQuery): AxiosPromise<NailMemberLevelVO[]> => {
  return request({ url: '/member/level/list', method: 'get', params: query });
};

export const getNailMemberLevel = (id: string | number): AxiosPromise<NailMemberLevelVO> => {
  return request({ url: '/member/level/' + id, method: 'get' });
};

export const addNailMemberLevel = (data: NailMemberLevelForm) => {
  return request({ url: '/member/level', method: 'post', data });
};

export const updateNailMemberLevel = (data: NailMemberLevelForm) => {
  return request({ url: '/member/level', method: 'put', data });
};

export const delNailMemberLevel = (id: string | number | Array<string | number>) => {
  return request({ url: '/member/level/' + id, method: 'delete' });
};

