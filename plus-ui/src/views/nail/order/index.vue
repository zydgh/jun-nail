<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="queryParams.orderNo" placeholder="请输入订单编号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="支付方式" prop="payType">
              <el-input v-model="queryParams.payType" placeholder="请输入支付方式" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="订单编号" align="center" prop="orderNo" show-overflow-tooltip />
        <el-table-column label="会员ID" align="center" prop="memberId" show-overflow-tooltip />
        <el-table-column label="美甲师ID" align="center" prop="employeeId" show-overflow-tooltip />
        <el-table-column label="门店ID" align="center" prop="storeId" show-overflow-tooltip />
        <el-table-column label="下单时间" align="center" prop="orderTime" width="160" />
        <el-table-column label="支付方式" align="center" prop="payType" show-overflow-tooltip />
        <el-table-column label="订单总额" align="center" prop="totalAmount" show-overflow-tooltip />
        <el-table-column label="优惠金额" align="center" prop="discountAmount" show-overflow-tooltip />
        <el-table-column label="实付金额" align="center" prop="payAmount" show-overflow-tooltip />
        <el-table-column label="赠送积分" align="center" prop="pointAmount" show-overflow-tooltip />
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

    <el-dialog v-model="checkoutOpen" title="收银下单" width="560px" append-to-body>
      <el-form :model="checkoutForm" label-width="100px">
        <el-form-item label="会员ID"><el-input v-model="checkoutForm.memberId" /></el-form-item>
        <el-form-item label="美甲师ID"><el-input v-model="checkoutForm.employeeId" /></el-form-item>
        <el-form-item label="门店ID"><el-input v-model="checkoutForm.storeId" /></el-form-item>
        <el-form-item label="项目ID"><el-input v-model="checkoutForm.itemId" /></el-form-item>
        <el-form-item label="数量"><el-input v-model="checkoutForm.quantity" /></el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="checkoutForm.payType">
            <el-option label="余额" value="balance" />
            <el-option label="微信" value="wechat" />
            <el-option label="支付宝" value="alipay" />
            <el-option label="现金" value="cash" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitCheckout">确 定</el-button>
        <el-button @click="checkoutOpen = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="NailOrder" lang="ts">
import { listNailOrder, getNailOrder, delNailOrder, addNailOrder, updateNailOrder } from '@/api/nail/order';
import { NailOrderVO, NailOrderQuery, NailOrderForm } from '@/api/nail/order/types';
import { checkoutOrder } from '@/api/nail/order';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));
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
const checkoutForm = reactive({ memberId: undefined as any, employeeId: undefined as any, storeId: undefined as any, itemId: undefined as any, quantity: 1, payType: 'cash' });
const handleCheckout = () => { checkoutOpen.value = true; };
const submitCheckout = async () => {
  await checkoutOrder(checkoutForm);
  proxy?.$modal.msgSuccess('下单成功');
  checkoutOpen.value = false;
  await getList();
};

onMounted(() => {
  getList();
});
</script>
