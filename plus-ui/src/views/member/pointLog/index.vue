<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="变动类型" prop="changeType">
              <el-input v-model="queryParams.changeType" placeholder="请输入变动类型" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="业务单号" prop="bizNo">
              <el-input v-model="queryParams.bizNo" placeholder="请输入业务单号" clearable @keyup.enter="handleQuery" />
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
            <el-button v-hasPermi="['member:point:add']" type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:point:edit']" type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:point:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['member:point:export']" type="warning" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>

          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="会员ID" align="center" prop="memberId" show-overflow-tooltip />
        <el-table-column label="变动类型" align="center" prop="changeType" show-overflow-tooltip />
        <el-table-column label="变动积分" align="center" prop="point" show-overflow-tooltip />
        <el-table-column label="变动前积分" align="center" prop="beforePoint" show-overflow-tooltip />
        <el-table-column label="变动后积分" align="center" prop="afterPoint" show-overflow-tooltip />
        <el-table-column label="业务单号" align="center" prop="bizNo" show-overflow-tooltip />
        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['member:point:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['member:point:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="会员ID" prop="memberId">
          <el-input v-model="form.memberId" placeholder="请输入会员ID" />
        </el-form-item>
        <el-form-item label="变动类型" prop="changeType">
          <el-input v-model="form.changeType" placeholder="请输入变动类型" />
        </el-form-item>
        <el-form-item label="变动积分" prop="point">
          <el-input v-model="form.point" placeholder="请输入变动积分" />
        </el-form-item>
        <el-form-item label="变动前积分" prop="beforePoint">
          <el-input v-model="form.beforePoint" placeholder="请输入变动前积分" />
        </el-form-item>
        <el-form-item label="变动后积分" prop="afterPoint">
          <el-input v-model="form.afterPoint" placeholder="请输入变动后积分" />
        </el-form-item>
        <el-form-item label="业务单号" prop="bizNo">
          <el-input v-model="form.bizNo" placeholder="请输入业务单号" />
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

  </div>
</template>

<script setup name="NailMemberPointLog" lang="ts">
import { listNailMemberPointLog, getNailMemberPointLog, delNailMemberPointLog, addNailMemberPointLog, updateNailMemberPointLog } from '@/api/member/pointLog';
import { NailMemberPointLogVO, NailMemberPointLogQuery, NailMemberPointLogForm } from '@/api/member/pointLog/types';

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
  memberId: undefined,
  changeType: undefined,
  point: undefined,
  beforePoint: undefined,
  afterPoint: undefined,
  bizNo: undefined,
  remark: undefined

};
const data = reactive({
  form: { ...initFormData },
  queryParams: { pageNum: 1, pageSize: 10 },
  rules: {
  memberId: [{ required: true, message: '会员ID不能为空', trigger: 'blur' }],
  changeType: [{ required: true, message: '变动类型不能为空', trigger: 'blur' }],
  point: [{ required: true, message: '变动积分不能为空', trigger: 'blur' }],
  }
});
const { queryParams, form, rules } = toRefs(data);
const getList = async () => {
  loading.value = true;
  const res = await listNailMemberPointLog(queryParams.value);
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
const handleSelectionChange = (selection: NailMemberPointLogVO[]) => {
  ids.value = selection.map((item) => item.id!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '新增积分变动';
};
const handleUpdate = async (row?: NailMemberPointLogVO) => {
  reset();
  const id = row?.id || ids.value[0];
  const res = await getNailMemberPointLog(id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改积分变动';
};
const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateNailMemberPointLog(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addNailMemberPointLog(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};
const handleDelete = async (row?: NailMemberPointLogVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除积分变动编号为"' + _ids + '"的数据项？');
  await delNailMemberPointLog(_ids);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};
const handleExport = () => {
  proxy?.download('member/pointLog/export', { ...queryParams.value }, `nail_member_point_log_${new Date().getTime()}.xlsx`);
};

onMounted(() => {
  getList();
});
</script>
