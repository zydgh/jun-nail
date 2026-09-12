export interface NailAppointmentVO {
  id?: number | string;
  /** 预约单号 */
  appointNo?: string;
  /** 会员ID */
  memberId?: number | string;
  /** 美甲师ID */
  employeeId?: number | string;
  /** 门店ID */
  storeId?: number | string;
  /** 项目ID */
  itemId?: number | string;
  /** 预约时间 */
  appointTime?: string;
  /** 时长(分钟) */
  durationMin?: number | string;
  /** 预约状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailAppointmentForm extends BaseEntity {
  id?: number | string;
  appointNo?: string;
  memberId?: number | string;
  employeeId?: number | string;
  storeId?: number | string;
  itemId?: number | string;
  appointTime?: string;
  durationMin?: number | string;
  status?: string;
  remark?: string;
}

export interface NailAppointmentQuery extends PageQuery {
  appointNo?: string;
  memberId?: number | string;
  employeeId?: number | string;
  storeId?: number | string;
  itemId?: number | string;
  appointTime?: string;
  durationMin?: number | string;
  status?: string;
  remark?: string;
}
