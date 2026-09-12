import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { NailAppointmentVO, NailAppointmentForm, NailAppointmentQuery } from './types';

export const listNailAppointment = (query?: NailAppointmentQuery): AxiosPromise<NailAppointmentVO[]> => {
  return request({ url: '/nail/appointment/list', method: 'get', params: query });
};

export const getNailAppointment = (id: string | number): AxiosPromise<NailAppointmentVO> => {
  return request({ url: '/nail/appointment/' + id, method: 'get' });
};

export const addNailAppointment = (data: NailAppointmentForm) => {
  return request({ url: '/nail/appointment', method: 'post', data });
};

export const updateNailAppointment = (data: NailAppointmentForm) => {
  return request({ url: '/nail/appointment', method: 'put', data });
};

export const delNailAppointment = (id: string | number | Array<string | number>) => {
  return request({ url: '/nail/appointment/' + id, method: 'delete' });
};

