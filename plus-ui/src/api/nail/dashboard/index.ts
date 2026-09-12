import request from '@/utils/request';

export const getDashboardOverview = () => {
  return request({
    url: '/nail/dashboard/overview',
    method: 'get'
  });
};
