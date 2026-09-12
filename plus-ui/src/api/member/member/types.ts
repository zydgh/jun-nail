export interface NailMemberVO {
  id?: number | string;
  /** 会员编号 */
  memberNo?: string;
  /** 姓名 */
  name?: string;
  /** 手机号 */
  phone?: string;
  /** 性别 */
  sex?: string;
  /** 生日 */
  birthday?: string;
  /** 会员等级 */
  levelId?: number | string;
  /** 积分 */
  point?: number | string;
  /** 余额 */
  balance?: number | string;
  /** 累计充值 */
  totalRecharge?: number | string;
  /** 累计消费 */
  totalConsume?: number | string;
  /** 消费次数 */
  consumeTimes?: number | string;
  /** 最近到店 */
  lastVisitTime?: string;
  /** 所属门店 */
  storeId?: number | string;
  /** 来源 */
  source?: string;
  /** 标签 */
  tags?: string;
  /** 状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailMemberForm extends BaseEntity {
  id?: number | string;
  memberNo?: string;
  name?: string;
  phone?: string;
  sex?: string;
  birthday?: string;
  levelId?: number | string;
  point?: number | string;
  balance?: number | string;
  totalRecharge?: number | string;
  totalConsume?: number | string;
  consumeTimes?: number | string;
  lastVisitTime?: string;
  storeId?: number | string;
  source?: string;
  tags?: string;
  status?: string;
  remark?: string;
}

export interface NailMemberQuery extends PageQuery {
  memberNo?: string;
  name?: string;
  phone?: string;
  sex?: string;
  birthday?: string;
  levelId?: number | string;
  point?: number | string;
  balance?: number | string;
  totalRecharge?: number | string;
  totalConsume?: number | string;
  consumeTimes?: number | string;
  lastVisitTime?: string;
  storeId?: number | string;
  source?: string;
  tags?: string;
  status?: string;
  remark?: string;
}
