export interface NailEmployeeVO {
  id?: number | string;
  /** 员工编号 */
  employeeNo?: string;
  /** 姓名 */
  name?: string;
  /** 手机号 */
  phone?: string;
  /** 性别 */
  sex?: string;
  /** 头像 */
  avatar?: string;
  /** 所属门店 */
  storeId?: number | string;
  /** 职位 */
  position?: string;
  /** 等级 */
  grade?: string;
  /** 提成比例 */
  commissionRate?: number | string;
  /** 入职日期 */
  hireDate?: string;
  /** 关联用户 */
  userId?: number | string;
  /** 擅长项目 */
  skill?: string;
  /** 状态 */
  status?: string;
  /** 备注 */
  remark?: string;
}

export interface NailEmployeeForm extends BaseEntity {
  id?: number | string;
  employeeNo?: string;
  name?: string;
  phone?: string;
  sex?: string;
  avatar?: string;
  storeId?: number | string;
  position?: string;
  grade?: string;
  commissionRate?: number | string;
  hireDate?: string;
  userId?: number | string;
  skill?: string;
  status?: string;
  remark?: string;
}

export interface NailEmployeeQuery extends PageQuery {
  employeeNo?: string;
  name?: string;
  phone?: string;
  sex?: string;
  avatar?: string;
  storeId?: number | string;
  position?: string;
  grade?: string;
  commissionRate?: number | string;
  hireDate?: string;
  userId?: number | string;
  skill?: string;
  status?: string;
  remark?: string;
}
