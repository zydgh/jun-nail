export interface NailEmployeePerformanceVO {
  id?: number | string;
  employeeId?: number | string;
  employeeName?: string;
  storeId?: number | string;
  amount?: number;
  itemId?: number | string;
  itemName?: string;
  customerName?: string;
  customerPhone?: string;
  consumeTime?: string;
  praise?: string;
  remark?: string;
}

export interface NailEmployeePerformanceForm extends BaseEntity {
  id?: number | string;
  employeeId?: number | string;
  amount?: number;
  itemId?: number | string;
  customerName?: string;
  customerPhone?: string;
  consumeTime?: string;
  praise?: string;
  remark?: string;
}

export interface NailEmployeePerformanceQuery extends PageQuery {
  employeeId?: number | string;
  itemId?: number | string;
  customerName?: string;
  customerPhone?: string;
  praise?: string;
  params?: {
    beginConsumeTime?: string;
    endConsumeTime?: string;
  };
}

export interface NailEmployeePerformanceRankVO {
  rank: number;
  employeeId: number | string;
  employeeName: string;
  totalAmount: number;
  serviceCount: number;
  praiseCount: number;
}
