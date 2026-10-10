<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="queryParams.orderNo" placeholder="请输入订单编号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="付款方式" prop="payType">
              <el-select v-model="queryParams.payType" placeholder="请选择付款方式" clearable>
                <el-option v-for="dict in nail_performance_pay_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="订单状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="订单状态" clearable>
                <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>
    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:order:add']" type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:order:edit']" type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:order:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:order:export']" type="warning" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:order:add']" type="warning" plain icon="ShoppingCart" @click="handleCheckout">收银下单</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="订单编号" align="center" prop="orderNo" min-width="140" show-overflow-tooltip />
        <el-table-column label="员工" align="center" prop="employeeName" min-width="100" />
        <el-table-column label="服务项目" align="center" prop="itemName" min-width="120" show-overflow-tooltip />
        <el-table-column label="客户" align="center" prop="customerName" min-width="100" />
        <el-table-column label="手机号" align="center" prop="customerPhone" min-width="120" />
        <el-table-column label="下单时间" align="center" prop="orderTime" width="165" />
        <el-table-column label="付款方式" align="center" prop="payType" min-width="100">
          <template #default="scope">
            <dict-tag :options="nail_performance_pay_type" :value="scope.row.payType" />
          </template>
        </el-table-column>
        <el-table-column label="实付金额" align="center" prop="payAmount" min-width="100" />
        <el-table-column label="好评" align="center" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.praise === '1' ? 'success' : 'info'">{{ scope.row.praise === '1' ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="好评图片" align="center" min-width="100">
          <template #default="scope">
            <image-preview v-if="scope.row.praise === '1' && scope.row.praiseImageUrls" :src="scope.row.praiseImageUrls" :width="48" :height="48" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="订单状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['nail:order:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['nail:order:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="会员ID" prop="memberId">
          <el-input v-model="form.memberId" placeholder="请输入会员ID" />
        </el-form-item>
        <el-form-item label="美甲师ID" prop="employeeId">
          <el-input v-model="form.employeeId" placeholder="请输入美甲师ID" />
        </el-form-item>
        <el-form-item label="门店ID" prop="storeId">
          <el-input v-model="form.storeId" placeholder="请输入门店ID" />
        </el-form-item>
        <el-form-item label="下单时间" prop="orderTime">
          <el-date-picker v-model="form.orderTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择下单时间" />
        </el-form-item>
        <el-form-item label="支付方式" prop="payType">
          <el-input v-model="form.payType" placeholder="请输入支付方式" />
        </el-form-item>
        <el-form-item label="订单总额" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入订单总额" />
        </el-form-item>
        <el-form-item label="优惠金额" prop="discountAmount">
          <el-input v-model="form.discountAmount" placeholder="请输入优惠金额" />
        </el-form-item>
        <el-form-item label="实付金额" prop="payAmount">
          <el-input v-model="form.payAmount" placeholder="请输入实付金额" />
        </el-form-item>
        <el-form-item label="赠送积分" prop="pointAmount">
          <el-input v-model="form.pointAmount" placeholder="请输入赠送积分" />
        </el-form-item>
        <el-form-item label="订单状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-card class="mt-2" shadow="hover">
      <template #header>
        <div class="ranking-header">
          <span>员工业绩排行</span>
          <el-radio-group v-model="rankPeriod" @change="loadRanking">
            <el-radio-button value="day">当天</el-radio-button>
            <el-radio-button value="week">本周</el-radio-button>
            <el-radio-button value="month">本月</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <div ref="amountChartRef" class="rank-chart"></div>
      <el-table border :data="rankingList" class="mt-2">
        <el-table-column label="排名" prop="rank" align="center" width="80" />
        <el-table-column label="员工" prop="employeeName" align="center" />
        <el-table-column label="实收金额" prop="totalAmount" align="right" />
        <el-table-column label="服务单数" prop="serviceCount" align="center" />
        <el-table-column label="好评数" prop="praiseCount" align="center" />
      </el-table>
    </el-card>

    <el-dialog v-model="checkoutOpen" title="收银下单" width="640px" append-to-body>
      <el-form ref="checkoutFormRef" :model="checkoutForm" :rules="checkoutRules" label-width="100px">
        <el-form-item label="员工" prop="employeeId">
          <el-select v-model="checkoutForm.employeeId" placeholder="请选择员工" filterable style="width: 100%">
            <el-option v-for="item in employeeOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="checkoutForm.customerName" placeholder="请输入客户姓名" @blur="handleCustomerBlur" />
        </el-form-item>
        <el-form-item label="客户手机号" prop="customerPhone">
          <el-input v-model="checkoutForm.customerPhone" placeholder="请输入客户手机号" maxlength="20" @blur="handleCustomerBlur" />
          <div class="member-tip">
            <el-tag v-if="memberChecked && isMember" type="success" size="small">会员</el-tag>
            <el-tag v-else-if="memberChecked && !isMember" type="info" size="small">非会员（原价）</el-tag>
          </div>
        </el-form-item>
        <el-form-item label="服务项目" prop="itemId">
          <el-select v-model="checkoutForm.itemId" placeholder="请选择项目" filterable style="width: 100%" @change="fillAmount">
            <el-option v-for="item in itemOptions" :key="item.id" :label="item.itemName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格类型">
          <el-radio-group v-model="priceType" :disabled="memberChecked && !isMember" @change="fillAmount">
            <el-radio-button value="price">原价</el-radio-button>
            <el-radio-button value="memberPrice" :disabled="memberChecked && !isMember">会员价</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="实收金额" prop="amount">
          <el-input-number v-model="checkoutForm.amount" :min="0.01" :precision="2" :step="10" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="付款方式" prop="payType">
          <el-select v-model="checkoutForm.payType" placeholder="请选择付款方式" style="width: 100%">
            <el-option v-for="dict in nail_performance_pay_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户好评">
          <el-switch v-model="checkoutForm.praise" active-value="1" inactive-value="0" active-text="好评" inactive-text="未好评" />
        </el-form-item>
        <el-form-item v-if="checkoutForm.praise === '1'" label="好评图片">
          <image-upload v-model="checkoutForm.praiseImages" :limit="5" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="checkoutForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" :loading="buttonLoading" @click="submitCheckout">确 定</el-button>
        <el-button @click="checkoutOpen = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="NailOrder" lang="ts">
import { listNailOrder, getNailOrder, delNailOrder, addNailOrder, updateNailOrder, checkoutOrder, getNailOrderOptions, checkNailOrderMember, getNailOrderRanking } from '@/api/nail/order';
import { NailOrderVO } from '@/api/nail/order/types';
import { NailEmployeeVO } from '@/api/employee/employee/types';
import { NailServiceItemVO } from '@/api/nail/item/types';
import * as echarts from 'echarts';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable, nail_performance_pay_type } = toRefs<any>(proxy?.useDict('sys_normal_disable', 'nail_performance_pay_type'));
const dataList = ref([] as any[]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const queryFormRef = ref<ElFormInstance>();
const formRef = ref<ElFormInstance>();
const dialog = reactive<DialogOption>({ visible: false, title: '' });
const initFormData = {
  id: undefined,
  orderNo: undefined,
  memberId: undefined,
  employeeId: undefined,
  storeId: undefined,
  orderTime: undefined,
  payType: undefined,
  totalAmount: undefined,
  discountAmount: undefined,
  payAmount: undefined,
  pointAmount: undefined,
  status: undefined,
  remark: undefined

};
const data = reactive({
  form: { ...initFormData },
  queryParams: { pageNum: 1, pageSize: 10 },
  rules: {

  }
});
const { queryParams, form, rules } = toRefs(data);
const getList = async () => {
  loading.value = true;
  const res = await listNailOrder(queryParams.value);
  dataList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};
const cancel = () => {
  reset();
  dialog.visible = false;
};
const reset = () => {
  form.value = { ...initFormData };
  formRef.value?.resetFields();
};
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};
const handleSelectionChange = (selection: NailOrderVO[]) => {
  ids.value = selection.map((item) => item.id!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '新增订单';
};
const handleUpdate = async (row?: NailOrderVO) => {
  reset();
  const id = row?.id || ids.value[0];
  const res = await getNailOrder(id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改订单';
};
const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateNailOrder(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addNailOrder(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};
const handleDelete = async (row?: NailOrderVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除订单编号为"' + _ids + '"的数据项？');
  await delNailOrder(_ids);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};
const handleExport = () => {
  proxy?.download('nail/order/export', { ...queryParams.value }, `nail_order_${new Date().getTime()}.xlsx`);
};

const checkoutOpen = ref(false);
const checkoutFormRef = ref<ElFormInstance>();
const employeeOptions = ref<NailEmployeeVO[]>([]);
const itemOptions = ref<NailServiceItemVO[]>([]);
const isMember = ref(false);
const memberChecked = ref(false);
const priceType = ref<'price' | 'memberPrice'>('price');
const rankPeriod = ref<'day' | 'week' | 'month'>('day');
const rankingList = ref<any[]>([]);
const amountChartRef = ref<HTMLDivElement>();
const amountChart = shallowRef<echarts.ECharts>();
const checkoutForm = reactive({
  memberId: undefined as any,
  employeeId: undefined as any,
  customerName: '',
  customerPhone: '',
  itemId: undefined as any,
  quantity: 1,
  amount: undefined as number | undefined,
  payType: 'cash',
  praise: '0',
  praiseImages: undefined as string | undefined,
  remark: ''
});
const checkoutRules = {
  employeeId: [{ required: true, message: '请选择员工', trigger: 'change' }],
  customerName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  customerPhone: [{ required: true, message: '请输入客户手机号', trigger: 'blur' }],
  itemId: [{ required: true, message: '请选择服务项目', trigger: 'change' }],
  amount: [{ required: true, message: '请输入实收金额', trigger: 'blur' }],
  payType: [{ required: true, message: '请选择付款方式', trigger: 'change' }]
};
const fillAmount = () => {
  const item = itemOptions.value.find((row) => String(row.id) === String(checkoutForm.itemId));
  if (!item) return;
  const raw = priceType.value === 'memberPrice' ? item.memberPrice : item.price;
  const num = Number(raw || item.price || 0);
  if (num > 0) checkoutForm.amount = num;
};
const handleCustomerBlur = async () => {
  if (!checkoutForm.customerPhone && !checkoutForm.customerName) {
    isMember.value = false;
    memberChecked.value = false;
    priceType.value = 'price';
    fillAmount();
    return;
  }
  const res = await checkNailOrderMember({ phone: checkoutForm.customerPhone, name: checkoutForm.customerName });
  isMember.value = !!res.data?.member;
  memberChecked.value = true;
  checkoutForm.memberId = isMember.value ? res.data?.memberId : undefined;
  priceType.value = isMember.value ? 'memberPrice' : 'price';
  fillAmount();
};
const handleCheckout = async () => {
  const res = await getNailOrderOptions();
  employeeOptions.value = res.data?.employees || [];
  itemOptions.value = res.data?.items || [];
  Object.assign(checkoutForm, {
    memberId: undefined,
    employeeId: employeeOptions.value.length === 1 ? employeeOptions.value[0].id : undefined,
    customerName: '',
    customerPhone: '',
    itemId: undefined,
    quantity: 1,
    amount: undefined,
    payType: 'cash',
    praise: '0',
    praiseImages: undefined,
    remark: ''
  });
  isMember.value = false;
  memberChecked.value = false;
  priceType.value = 'price';
  checkoutOpen.value = true;
};
const submitCheckout = () => {
  checkoutFormRef.value?.validate(async (valid) => {
    if (!valid) return;
    buttonLoading.value = true;
    try {
      const payload = { ...checkoutForm };
      if (payload.praise !== '1') payload.praiseImages = undefined;
      await checkoutOrder(payload);
      proxy?.$modal.msgSuccess('下单成功');
      checkoutOpen.value = false;
      await getList();
      await loadRanking();
    } finally {
      buttonLoading.value = false;
    }
  });
};
const renderChart = () => {
  nextTick(() => {
    if (amountChartRef.value && !amountChart.value) {
      amountChart.value = echarts.init(amountChartRef.value);
    }
    const names = rankingList.value.map((item) => item.employeeName || '未知员工');
    const amounts = rankingList.value.map((item) => Number(item.totalAmount || 0));
    amountChart.value?.setOption({
      title: { text: '业绩金额统计', left: 'center', textStyle: { fontSize: 14 } },
      tooltip: { trigger: 'axis' },
      grid: { left: 56, right: 24, top: 48, bottom: 40 },
      xAxis: { type: 'category', data: names },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: amounts, barMaxWidth: 48, itemStyle: { color: '#e85d4c', borderRadius: [6, 6, 0, 0] } }]
    }, true);
  });
};
const loadRanking = async () => {
  const res = await getNailOrderRanking(rankPeriod.value);
  rankingList.value = res.data || [];
  renderChart();
};

onMounted(() => {
  getList();
  loadRanking();
});
</script>

<style scoped>
.ranking-header { display: flex; align-items: center; justify-content: space-between; }
.rank-chart { width: 100%; height: 320px; }
.member-tip { margin-top: 6px; min-height: 22px; }
</style>
