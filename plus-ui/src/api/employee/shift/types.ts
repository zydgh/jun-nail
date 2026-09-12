export interface NailEmployeeShiftVO {
  id?: number | string;
  /** 员工ID */
  employeeId?: number | string;
  /** 门店ID */
  storeId?: number | string;
  /** 排班日期 */
  shiftDate?: string;
  /** 开始时间 */
  startTime?: string;
  /** 结束时间 */
  endTime?: string;
  /** 状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailEmployeeShiftForm extends BaseEntity {
  id?: number | string;
  employeeId?: number | string;
  storeId?: number | string;
  shiftDate?: string;
  startTime?: string;
  endTime?: string;
  status?: string;
  remark?: string;
}

export interface NailEmployeeShiftQuery extends PageQuery {
  employeeId?: number | string;
  storeId?: number | string;
  shiftDate?: string;
  startTime?: string;
  endTime?: string;
  status?: string;
  remark?: string;
}
