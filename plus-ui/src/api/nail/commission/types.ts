export interface NailCommissionVO {
  id?: number | string;
  /** 员工ID */
  employeeId?: number | string;
  /** 订单ID */
  orderId?: number | string;
  /** 订单编号 */
  orderNo?: string;
  /** 提成比例 */
  commissionRate?: number | string;
  /** 提成金额 */
  commissionAmount?: number | string;
  /** 结算状态 */
  status?: string;
  /** 结算时间 */
  settleTime?: string;
  /** 备注 */
  remark?: string;
}

export interface NailCommissionForm extends BaseEntity {
  id?: number | string;
  employeeId?: number | string;
  orderId?: number | string;
  orderNo?: string;
  commissionRate?: number | string;
  commissionAmount?: number | string;
  status?: string;
  settleTime?: string;
  remark?: string;
}

export interface NailCommissionQuery extends PageQuery {
  employeeId?: number | string;
  orderId?: number | string;
  orderNo?: string;
  commissionRate?: number | string;
  commissionAmount?: number | string;
  status?: string;
  settleTime?: string;
  remark?: string;
}
