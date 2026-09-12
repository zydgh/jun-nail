export interface NailMemberPointLogVO {
  id?: number | string;
  /** 会员ID */
  memberId?: number | string;
  /** 变动类型 */
  changeType?: string;
  /** 变动积分 */
  point?: number | string;
  /** 变动前积分 */
  beforePoint?: number | string;
  /** 变动后积分 */
  afterPoint?: number | string;
  /** 业务单号 */
  bizNo?: string;
  /** 备注 */
  remark?: string;
}

export interface NailMemberPointLogForm extends BaseEntity {
  id?: number | string;
  memberId?: number | string;
  changeType?: string;
  point?: number | string;
  beforePoint?: number | string;
  afterPoint?: number | string;
  bizNo?: string;
  remark?: string;
}

export interface NailMemberPointLogQuery extends PageQuery {
  memberId?: number | string;
  changeType?: string;
  point?: number | string;
  beforePoint?: number | string;
  afterPoint?: number | string;
  bizNo?: string;
  remark?: string;
}
