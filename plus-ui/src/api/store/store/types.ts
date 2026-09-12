export interface NailStoreVO {
  id?: number | string;
  /** 门店编号 */
  storeCode?: string;
  /** 门店名称 */
  storeName?: string;
  /** 联系电话 */
  phone?: string;
  /** 门店地址 */
  address?: string;
  /** 负责人 */
  manager?: string;
  /** 排序 */
  sort?: number | string;
  /** 状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailStoreForm extends BaseEntity {
  id?: number | string;
  storeCode?: string;
  storeName?: string;
  phone?: string;
  address?: string;
  manager?: string;
  sort?: number | string;
  status?: string;
  remark?: string;
}

export interface NailStoreQuery extends PageQuery {
  storeCode?: string;
  storeName?: string;
  phone?: string;
  address?: string;
  manager?: string;
  sort?: number | string;
  status?: string;
  remark?: string;
}
