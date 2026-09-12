export interface NailOrderItemVO {
  id?: number | string;
  /** 订单ID */
  orderId?: number | string;
  /** 项目ID */
  itemId?: number | string;
  /** 项目名称 */
  itemName?: string;
  /** 数量 */
  quantity?: number | string;
  /** 单价 */
  price?: number | string;
  /** 金额 */
  amount?: number | string;
  /** 备注 */
  remark?: string;
}

export interface NailOrderItemForm extends BaseEntity {
  id?: number | string;
  orderId?: number | string;
  itemId?: number | string;
  itemName?: string;
  quantity?: number | string;
  price?: number | string;
  amount?: number | string;
  remark?: string;
}

export interface NailOrderItemQuery extends PageQuery {
  orderId?: number | string;
  itemId?: number | string;
  itemName?: string;
  quantity?: number | string;
  price?: number | string;
  amount?: number | string;
  remark?: string;
}
