export interface NailMemberLevelVO {
  id?: number | string;
  /** 等级名称 */
  levelName?: string;
  /** 折扣 */
  discount?: number | string;
  /** 升级积分 */
  upgradePoint?: number | string;
  /** 排序 */
  sort?: number | string;
  /** 状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailMemberLevelForm extends BaseEntity {
  id?: number | string;
  levelName?: string;
  discount?: number | string;
  upgradePoint?: number | string;
  sort?: number | string;
  status?: string;
  remark?: string;
}

export interface NailMemberLevelQuery extends PageQuery {
  levelName?: string;
  discount?: number | string;
  upgradePoint?: number | string;
  sort?: number | string;
  status?: string;
  remark?: string;
}
