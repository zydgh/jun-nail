import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailMemberVO, NailMemberForm, NailMemberQuery } from './types';

export const listNailMember = (query?: NailMemberQuery): AxiosPromise<NailMemberVO[]> => {
  return request({ url: '/member/member/list', method: 'get', params: query });
};

export const getNailMember = (id: string | number): AxiosPromise<NailMemberVO> => {
  return request({ url: '/member/member/' + id, method: 'get' });
};

export const addNailMember = (data: NailMemberForm) => {
  return request({ url: '/member/member', method: 'post', data });
};

export const updateNailMember = (data: NailMemberForm) => {
  return request({ url: '/member/member', method: 'put', data });
};

export const delNailMember = (id: string | number | Array<string | number>) => {
  return request({ url: '/member/member/' + id, method: 'delete' });
};

export const rechargeMember = (data: any) => {
  return request({ url: '/member/member/recharge', method: 'post', data });
};
export const adjustMember = (data: any) => {
  return request({ url: '/member/member/adjust', method: 'post', data });
};
export const birthdayMember = (days?: number) => {
  return request({ url: '/member/member/birthday', method: 'get', params: { days } });
};

