export interface NailOrderVO {
  id?: number | string;
  /** 订单编号 */
  orderNo?: string;
  /** 会员ID */
  memberId?: number | string;
  /** 美甲师ID */
  employeeId?: number | string;
  /** 门店ID */
  storeId?: number | string;
  /** 下单时间 */
  orderTime?: string;
  /** 支付方式 */
  payType?: string;
  /** 订单总额 */
  totalAmount?: number | string;
  /** 优惠金额 */
  discountAmount?: number | string;
  /** 实付金额 */
  payAmount?: number | string;
  /** 赠送积分 */
  pointAmount?: number | string;
  /** 订单状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailOrderForm extends BaseEntity {
  id?: number | string;
  orderNo?: string;
  memberId?: number | string;
  employeeId?: number | string;
  storeId?: number | string;
  orderTime?: string;
  payType?: string;
  totalAmount?: number | string;
  discountAmount?: number | string;
  payAmount?: number | string;
  pointAmount?: number | string;
  status?: string;
  remark?: string;
}

export interface NailOrderQuery extends PageQuery {
  orderNo?: string;
  memberId?: number | string;
  employeeId?: number | string;
  storeId?: number | string;
  orderTime?: string;
  payType?: string;
  totalAmount?: number | string;
  discountAmount?: number | string;
  payAmount?: number | string;
  pointAmount?: number | string;
  status?: string;
  remark?: string;
}
