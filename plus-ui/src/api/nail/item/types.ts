export interface NailServiceItemVO {
  id?: number | string;
  /** 项目编号 */
  itemCode?: string;
  /** 项目名称 */
  itemName?: string;
  /** 分类 */
  category?: string;
  /** 时长(分钟) */
  durationMin?: number | string;
  /** 原价 */
  price?: number | string;
  /** 会员价 */
  memberPrice?: number | string;
  /** 排序 */
  sort?: number | string;
  /** 状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailServiceItemForm extends BaseEntity {
  id?: number | string;
  itemCode?: string;
  itemName?: string;
  category?: string;
  durationMin?: number | string;
  price?: number | string;
  memberPrice?: number | string;
  sort?: number | string;
  status?: string;
  remark?: string;
}

export interface NailServiceItemQuery extends PageQuery {
  itemCode?: string;
  itemName?: string;
  category?: string;
  durationMin?: number | string;
  price?: number | string;
  memberPrice?: number | string;
  sort?: number | string;
  status?: string;
  remark?: string;
}
