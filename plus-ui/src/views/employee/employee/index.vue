<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="员工编号" prop="employeeNo">
              <el-input v-model="queryParams.employeeNo" placeholder="请输入员工编号" clearable @keyup.enter="handleQuery" />
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
            <el-form-item label="职位" prop="position">
              <el-input v-model="queryParams.position" placeholder="请输入职位" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="等级" prop="grade">
              <el-input v-model="queryParams.grade" placeholder="请输入等级" clearable @keyup.enter="handleQuery" />
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
            <el-button v-hasPermi="['employee:employee:add']" type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['employee:employee:edit']" type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['employee:employee:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['employee:employee:export']" type="warning" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>

          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="员工编号" align="center" prop="employeeNo" show-overflow-tooltip />
        <el-table-column label="姓名" align="center" prop="name" show-overflow-tooltip />
        <el-table-column label="手机号" align="center" prop="phone" show-overflow-tooltip />
        <el-table-column label="性别" align="center" prop="sex" width="80">
          <template #default="scope">
            <dict-tag :options="sys_user_sex" :value="scope.row.sex" />
          </template>
        </el-table-column>
        <el-table-column label="头像" align="center" prop="avatarUrl" width="100">
          <template #default="scope">
            <image-preview :src="scope.row.avatarUrl" :width="50" :height="50" />
          </template>
        </el-table-column>
        <el-table-column label="所属门店" align="center" prop="storeId" show-overflow-tooltip />
        <el-table-column label="职位" align="center" prop="position" show-overflow-tooltip />
        <el-table-column label="等级" align="center" prop="grade" show-overflow-tooltip />
        <el-table-column label="提成比例" align="center" prop="commissionRate" show-overflow-tooltip />
        <el-table-column label="入职日期" align="center" prop="hireDate" width="160" />
        <el-table-column label="关联用户" align="center" prop="userId" show-overflow-tooltip />
        <el-table-column label="擅长项目" align="center" prop="skill" show-overflow-tooltip />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['employee:employee:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['employee:employee:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="员工编号" prop="employeeNo">
          <el-input v-model="form.employeeNo" placeholder="请输入员工编号" />
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
        <el-form-item label="头像" prop="avatar">
          <image-upload v-model="form.avatar" :limit="1" />
        </el-form-item>
        <el-form-item label="所属门店" prop="storeId">
          <el-input v-model="form.storeId" placeholder="请输入所属门店" />
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="等级" prop="grade">
          <el-input v-model="form.grade" placeholder="请输入等级" />
        </el-form-item>
        <el-form-item label="提成比例" prop="commissionRate">
          <el-input v-model="form.commissionRate" placeholder="请输入提成比例" />
        </el-form-item>
        <el-form-item label="入职日期" prop="hireDate">
          <el-date-picker v-model="form.hireDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择入职日期" />
        </el-form-item>
        <el-form-item label="关联用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入关联用户" />
        </el-form-item>
        <el-form-item label="擅长项目" prop="skill">
          <el-input v-model="form.skill" type="textarea" placeholder="请输入擅长项目" />
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

  </div>
</template>

<script setup name="NailEmployee" lang="ts">
import { listNailEmployee, getNailEmployee, delNailEmployee, addNailEmployee, updateNailEmployee } from '@/api/employee/employee';
import { NailEmployeeVO, NailEmployeeQuery, NailEmployeeForm } from '@/api/employee/employee/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable, sys_user_sex } = toRefs<any>(proxy?.useDict('sys_normal_disable', 'sys_user_sex'));
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
  employeeNo: undefined,
  name: undefined,
  phone: undefined,
  sex: undefined,
  avatar: undefined,
  storeId: undefined,
  position: undefined,
  grade: undefined,
  commissionRate: undefined,
  hireDate: undefined,
  userId: undefined,
  skill: undefined,
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
  const res = await listNailEmployee(queryParams.value);
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
const handleSelectionChange = (selection: NailEmployeeVO[]) => {
  ids.value = selection.map((item) => item.id!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '新增员工';
};
const handleUpdate = async (row?: NailEmployeeVO) => {
  reset();
  const id = row?.id || ids.value[0];
  const res = await getNailEmployee(id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改员工';
};
const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateNailEmployee(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addNailEmployee(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};
const handleDelete = async (row?: NailEmployeeVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除员工编号为"' + _ids + '"的数据项？');
  await delNailEmployee(_ids);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};
const handleExport = () => {
  proxy?.download('employee/employee/export', { ...queryParams.value }, `nail_employee_${new Date().getTime()}.xlsx`);
};

onMounted(() => {
  getList();
});
</script>
