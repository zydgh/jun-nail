export interface NailMemberBalanceLogVO {
  id?: number | string;
  /** 会员ID */
  memberId?: number | string;
  /** 变动类型 */
  changeType?: string;
  /** 变动金额 */
  amount?: number | string;
  /** 变动前余额 */
  beforeBalance?: number | string;
  /** 变动后余额 */
  afterBalance?: number | string;
  /** 业务单号 */
  bizNo?: string;
  /** 备注 */
  remark?: string;
}

export interface NailMemberBalanceLogForm extends BaseEntity {
  id?: number | string;
  memberId?: number | string;
  changeType?: string;
  amount?: number | string;
  beforeBalance?: number | string;
  afterBalance?: number | string;
  bizNo?: string;
  remark?: string;
}

export interface NailMemberBalanceLogQuery extends PageQuery {
  memberId?: number | string;
  changeType?: string;
  amount?: number | string;
  beforeBalance?: number | string;
  afterBalance?: number | string;
  bizNo?: string;
  remark?: string;
}
