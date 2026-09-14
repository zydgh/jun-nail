<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="会员编号" prop="memberNo">
              <el-input v-model="queryParams.memberNo" placeholder="请输入会员编号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable>
                <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="来源" prop="source">
              <el-input v-model="queryParams.source" placeholder="请输入来源" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标签" prop="tags">
              <el-input v-model="queryParams.tags" placeholder="请输入标签" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="状态" clearable>
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
            <el-button v-hasPermi="['member:member:add']" type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:member:edit']" type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:member:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:member:export']" type="warning" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:member:recharge']" type="warning" plain icon="Wallet" :disabled="single" @click="handleRecharge">充值</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:member:list']" type="info" plain icon="Present" @click="handleBirthday">生日提醒</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="会员编号" align="center" prop="memberNo" show-overflow-tooltip />
        <el-table-column label="姓名" align="center" prop="name" show-overflow-tooltip />
        <el-table-column label="手机号" align="center" prop="phone" show-overflow-tooltip />
        <el-table-column label="性别" align="center" prop="sex" width="80">
          <template #default="scope">
            <dict-tag :options="sys_user_sex" :value="scope.row.sex" />
          </template>
        </el-table-column>
        <el-table-column label="生日" align="center" prop="birthday" width="160" />
        <el-table-column label="会员等级" align="center" prop="levelId" show-overflow-tooltip />
        <el-table-column label="积分" align="center" prop="point" show-overflow-tooltip />
        <el-table-column label="余额" align="center" prop="balance" show-overflow-tooltip />
        <el-table-column label="累计充值" align="center" prop="totalRecharge" show-overflow-tooltip />
        <el-table-column label="累计消费" align="center" prop="totalConsume" show-overflow-tooltip />
        <el-table-column label="消费次数" align="center" prop="consumeTimes" show-overflow-tooltip />
        <el-table-column label="最近到店" align="center" prop="lastVisitTime" width="160" />
        <el-table-column label="所属门店" align="center" prop="storeId" show-overflow-tooltip />
        <el-table-column label="来源" align="center" prop="source" show-overflow-tooltip />
        <el-table-column label="标签" align="center" prop="tags" show-overflow-tooltip />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['member:member:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['member:member:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="会员编号" prop="memberNo">
          <el-input v-model="form.memberNo" placeholder="请输入会员编号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-date-picker v-model="form.birthday" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择生日" />
        </el-form-item>
        <el-form-item label="会员等级" prop="levelId">
          <el-input v-model="form.levelId" placeholder="请输入会员等级" />
        </el-form-item>
        <el-form-item label="积分" prop="point">
          <el-input v-model="form.point" placeholder="请输入积分" />
        </el-form-item>
        <el-form-item label="余额" prop="balance">
          <el-input v-model="form.balance" placeholder="请输入余额" />
        </el-form-item>
        <el-form-item label="累计充值" prop="totalRecharge">
          <el-input v-model="form.totalRecharge" placeholder="请输入累计充值" />
        </el-form-item>
        <el-form-item label="累计消费" prop="totalConsume">
          <el-input v-model="form.totalConsume" placeholder="请输入累计消费" />
        </el-form-item>
        <el-form-item label="消费次数" prop="consumeTimes">
          <el-input v-model="form.consumeTimes" placeholder="请输入消费次数" />
        </el-form-item>
        <el-form-item label="最近到店" prop="lastVisitTime">
          <el-date-picker v-model="form.lastVisitTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择最近到店" />
        </el-form-item>
        <el-form-item label="所属门店" prop="storeId">
          <el-input v-model="form.storeId" placeholder="请输入所属门店" />
        </el-form-item>
        <el-form-item label="来源" prop="source">
          <el-input v-model="form.source" placeholder="请输入来源" />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
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

    <el-dialog v-model="rechargeOpen" title="会员充值" width="480px" append-to-body>
      <el-form :model="rechargeForm" label-width="90px">
        <el-form-item label="充值金额"><el-input v-model="rechargeForm.amount" /></el-form-item>
        <el-form-item label="赠送金额"><el-input v-model="rechargeForm.giftAmount" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="rechargeForm.remark" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitRecharge">确 定</el-button>
        <el-button @click="rechargeOpen = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="NailMember" lang="ts">
import { listNailMember, getNailMember, delNailMember, addNailMember, updateNailMember } from '@/api/member/member';
import { NailMemberVO, NailMemberQuery, NailMemberForm } from '@/api/member/member/types';
import { rechargeMember, birthdayMember } from '@/api/member/member';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable, sys_user_sex } = toRefs<any>(proxy?.useDict('sys_normal_disable', 'sys_user_sex'));
const dataList = ref([] as any[]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const memberNos = ref<string[]>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const queryFormRef = ref<ElFormInstance>();
const formRef = ref<ElFormInstance>();
const dialog = reactive<DialogOption>({ visible: false, title: '' });
const initFormData = {
  id: undefined,
  memberNo: undefined,
  name: undefined,
  phone: undefined,
  sex: undefined,
  birthday: undefined,
  levelId: undefined,
  point: undefined,
  balance: undefined,
  totalRecharge: undefined,
  totalConsume: undefined,
  consumeTimes: undefined,
  lastVisitTime: undefined,
  storeId: undefined,
  source: undefined,
  tags: undefined,
  status: undefined,
  remark: undefined

};
const data = reactive({
  form: { ...initFormData },
  queryParams: { pageNum: 1, pageSize: 10 },
  rules: {
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  phone: [{ required: true, message: '手机号不能为空', trigger: 'blur' }],
  }
});
const { queryParams, form, rules } = toRefs(data);
const getList = async () => {
  loading.value = true;
  const res = await listNailMember(queryParams.value);
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
const handleSelectionChange = (selection: NailMemberVO[]) => {
  ids.value = selection.map((item) => item.id!);
  memberNos.value = selection.map((item) => item.memberNo!).filter(Boolean);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '新增会员';
};
const handleUpdate = async (row?: NailMemberVO) => {
  reset();
  const id = row?.id || ids.value[0];
  const res = await getNailMember(id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改会员';
};
const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateNailMember(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addNailMember(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};
const handleDelete = async (row?: NailMemberVO) => {
  const _ids = row?.id || ids.value;
  const _memberNos = row?.memberNo || memberNos.value.join('、');
  await proxy?.$modal.confirm('是否确认删除会员编号为"' + _memberNos + '"的数据项？');
  await delNailMember(_ids);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};
const handleExport = () => {
  proxy?.download('member/member/export', { ...queryParams.value }, `nail_member_${new Date().getTime()}.xlsx`);
};

const rechargeOpen = ref(false);
const rechargeForm = reactive({ memberId: undefined as any, amount: undefined as any, giftAmount: 0, remark: '' });
const handleRecharge = () => {
  rechargeForm.memberId = ids.value[0];
  rechargeForm.amount = undefined;
  rechargeForm.giftAmount = 0;
  rechargeForm.remark = '';
  rechargeOpen.value = true;
};
const submitRecharge = async () => {
  await rechargeMember(rechargeForm);
  proxy?.$modal.msgSuccess('充值成功');
  rechargeOpen.value = false;
  await getList();
};
const handleBirthday = async () => {
  const res = await birthdayMember(7);
  const names = (res.data || []).map((i: any) => i.name).join('、') || '近7天暂无生日会员';
  proxy?.$modal.msgSuccess(names);
};

onMounted(() => {
  getList();
});
</script>
