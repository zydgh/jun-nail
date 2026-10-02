<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="门店编号" prop="storeCode">
              <el-input v-model="queryParams.storeCode" placeholder="请输入门店编号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="门店名称" prop="storeName">
              <el-input v-model="queryParams.storeName" placeholder="请输入门店名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="queryParams.phone" placeholder="请输入联系电话" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="负责人" prop="manager">
              <el-input v-model="queryParams.manager" placeholder="请输入负责人" clearable @keyup.enter="handleQuery" />
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
            <el-button v-hasPermi="['store:store:add']" type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['store:store:edit']" type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['store:store:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['store:store:export']" type="warning" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>

          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="门店编号" align="center" prop="storeCode" show-overflow-tooltip />
        <el-table-column label="门店名称" align="center" prop="storeName" show-overflow-tooltip />
        <el-table-column label="联系电话" align="center" prop="phone" show-overflow-tooltip />
        <el-table-column label="门店地址" align="center" prop="address" show-overflow-tooltip />
        <el-table-column label="负责人" align="center" prop="manager" show-overflow-tooltip />
        <el-table-column label="排序" align="center" prop="sort" show-overflow-tooltip />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['store:store:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['store:store:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item v-if="form.id" label="门店编号" prop="storeCode">
          <el-input v-model="form.storeCode" disabled />
        </el-form-item>
        <el-form-item label="门店名称" prop="storeName">
          <el-input v-model="form.storeName" placeholder="请输入门店名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="门店地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入门店地址" />
        </el-form-item>
        <el-form-item label="负责人" prop="manager">
          <el-input v-model="form.manager" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
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

<script setup name="NailStore" lang="ts">
import { listNailStore, getNailStore, delNailStore, addNailStore, updateNailStore } from '@/api/store/store';
import { NailStoreVO, NailStoreQuery, NailStoreForm } from '@/api/store/store/types';

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
  storeCode: undefined,
  storeName: undefined,
  phone: undefined,
  address: undefined,
  manager: undefined,
  sort: undefined,
  status: undefined,
  remark: undefined

};
const data = reactive({
  form: { ...initFormData },
  queryParams: { pageNum: 1, pageSize: 10 },
  rules: {
  storeName: [{ required: true, message: '门店名称不能为空', trigger: 'blur' }],
  }
});
const { queryParams, form, rules } = toRefs(data);
const getList = async () => {
  loading.value = true;
  const res = await listNailStore(queryParams.value);
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
const handleSelectionChange = (selection: NailStoreVO[]) => {
  ids.value = selection.map((item) => item.id!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '新增门店';
};
const handleUpdate = async (row?: NailStoreVO) => {
  reset();
  const id = row?.id || ids.value[0];
  const res = await getNailStore(id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改门店';
};
const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateNailStore(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addNailStore(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};
const handleDelete = async (row?: NailStoreVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除门店编号为"' + _ids + '"的数据项？');
  await delNailStore(_ids);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};
const handleExport = () => {
  proxy?.download('store/store/export', { ...queryParams.value }, `nail_store_${new Date().getTime()}.xlsx`);
};

onMounted(() => {
  getList();
});
</script>
