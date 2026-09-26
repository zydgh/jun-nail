<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="预约单号" prop="appointNo">
              <el-input v-model="queryParams.appointNo" placeholder="请输入预约单号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="会员" prop="memberId">
              <el-select v-model="queryParams.memberId" placeholder="请选择会员" clearable filterable>
                <el-option
                  v-for="item in memberOptions"
                  :key="item.id"
                  :label="memberLabel(item)"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="门店" prop="storeId">
              <el-select v-model="queryParams.storeId" placeholder="请选择门店" clearable filterable>
                <el-option v-for="item in storeOptions" :key="item.id" :label="item.storeName" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="美甲师" prop="employeeId">
              <el-select v-model="queryParams.employeeId" placeholder="请选择美甲师" clearable filterable>
                <el-option
                  v-for="item in employeeOptions"
                  :key="item.id"
                  :label="employeeLabel(item)"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="项目" prop="itemId">
              <el-select v-model="queryParams.itemId" placeholder="请选择项目" clearable filterable>
                <el-option v-for="item in itemOptions" :key="item.id" :label="item.itemName" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="预约状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="预约状态" clearable>
                <el-option v-for="dict in nail_appoint_status" :key="dict.value" :label="dict.label" :value="dict.value" />
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
            <el-button v-hasPermi="['nail:appointment:add']" type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:appointment:edit']" type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:appointment:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['nail:appointment:export']" type="warning" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>

          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="预约单号" align="center" prop="appointNo" show-overflow-tooltip />
        <el-table-column label="会员" align="center" prop="memberId" show-overflow-tooltip>
          <template #default="scope">
            {{ memberNameMap[String(scope.row.memberId)] || scope.row.memberId || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="门店" align="center" prop="storeId" show-overflow-tooltip>
          <template #default="scope">
            {{ storeNameMap[String(scope.row.storeId)] || scope.row.storeId || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="美甲师" align="center" prop="employeeId" show-overflow-tooltip>
          <template #default="scope">
            {{ employeeNameMap[String(scope.row.employeeId)] || scope.row.employeeId || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="项目" align="center" prop="itemId" show-overflow-tooltip>
          <template #default="scope">
            {{ itemNameMap[String(scope.row.itemId)] || scope.row.itemId || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="预约时间" align="center" prop="appointTime" width="160" />
        <el-table-column label="时长(分钟)" align="center" prop="durationMin" show-overflow-tooltip />
        <el-table-column label="预约状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="nail_appoint_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['nail:appointment:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['nail:appointment:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item v-if="form.id" label="预约单号" prop="appointNo">
          <el-input v-model="form.appointNo" disabled />
        </el-form-item>
        <el-form-item label="会员" prop="memberId">
          <el-select v-model="form.memberId" placeholder="请选择会员" clearable filterable style="width: 100%">
            <el-option
              v-for="item in enabledMemberOptions"
              :key="item.id"
              :label="memberLabel(item)"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="门店" prop="storeId">
          <el-select
            v-model="form.storeId"
            placeholder="请选择门店"
            clearable
            filterable
            style="width: 100%"
            @change="handleStoreChange"
          >
            <el-option v-for="item in enabledStoreOptions" :key="item.id" :label="item.storeName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="美甲师" prop="employeeId">
          <el-select v-model="form.employeeId" placeholder="请选择美甲师，可不选" clearable filterable style="width: 100%">
            <el-option
              v-for="item in enabledEmployeeOptions"
              :key="item.id"
              :label="employeeLabel(item)"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目" prop="itemId">
          <el-select
            v-model="form.itemId"
            placeholder="请选择项目"
            clearable
            filterable
            style="width: 100%"
            @change="handleItemChange"
          >
            <el-option
              v-for="item in enabledItemOptions"
              :key="item.id"
              :label="itemLabel(item)"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="appointTime">
          <el-date-picker v-model="form.appointTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择预约时间" />
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="durationMin">
          <el-input v-model="form.durationMin" placeholder="请输入时长(分钟)" />
        </el-form-item>
        <el-form-item label="预约状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in nail_appoint_status" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
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

<script setup name="NailAppointment" lang="ts">
import { listNailAppointment, getNailAppointment, delNailAppointment, addNailAppointment, updateNailAppointment } from '@/api/nail/appointment';
import { NailAppointmentVO, NailAppointmentQuery, NailAppointmentForm } from '@/api/nail/appointment/types';
import { listNailStore } from '@/api/store/store';
import { NailStoreVO } from '@/api/store/store/types';
import { listNailEmployee } from '@/api/employee/employee';
import { NailEmployeeVO } from '@/api/employee/employee/types';
import { listNailServiceItem } from '@/api/nail/item';
import { NailServiceItemVO } from '@/api/nail/item/types';
import { listNailMember } from '@/api/member/member';
import { NailMemberVO } from '@/api/member/member/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { nail_appoint_status } = toRefs<any>(proxy?.useDict('nail_appoint_status'));
const dataList = ref([] as any[]);
const memberOptions = ref<NailMemberVO[]>([]);
const storeOptions = ref<NailStoreVO[]>([]);
const employeeOptions = ref<NailEmployeeVO[]>([]);
const itemOptions = ref<NailServiceItemVO[]>([]);
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
  appointNo: undefined,
  memberId: undefined,
  employeeId: undefined,
  storeId: undefined,
  itemId: undefined,
  appointTime: undefined,
  durationMin: undefined,
  status: '0',
  remark: undefined

};
const data = reactive({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    appointNo: undefined,
    memberId: undefined,
    storeId: undefined,
    employeeId: undefined,
    itemId: undefined,
    status: undefined
  } as NailAppointmentQuery,
  rules: {
  memberId: [{ required: true, message: '会员不能为空', trigger: 'change' }],
  appointTime: [{ required: true, message: '预约时间不能为空', trigger: 'blur' }],
  }
});
const { queryParams, form, rules } = toRefs(data);

const isEnabled = (status?: string) => status === '0' || status == null || status === '';

const enabledMemberOptions = computed(() => memberOptions.value.filter((item) => isEnabled(item.status)));
const enabledStoreOptions = computed(() => storeOptions.value.filter((item) => isEnabled(item.status)));
const enabledItemOptions = computed(() => itemOptions.value.filter((item) => isEnabled(item.status)));
const enabledEmployeeOptions = computed(() =>
  employeeOptions.value.filter((item) => {
    if (!isEnabled(item.status)) {
      return false;
    }
    if (form.value.storeId == null || form.value.storeId === '') {
      return true;
    }
    return item.storeId == null || String(item.storeId) === String(form.value.storeId);
  })
);

const memberNameMap = computed(() => {
  const map: Record<string, string> = {};
  memberOptions.value.forEach((item) => {
    if (item.id != null) {
      map[String(item.id)] = item.name || item.memberNo || '';
    }
  });
  return map;
});
const storeNameMap = computed(() => {
  const map: Record<string, string> = {};
  storeOptions.value.forEach((item) => {
    if (item.id != null) {
      map[String(item.id)] = item.storeName || '';
    }
  });
  return map;
});
const employeeNameMap = computed(() => {
  const map: Record<string, string> = {};
  employeeOptions.value.forEach((item) => {
    if (item.id != null) {
      map[String(item.id)] = item.name || '';
    }
  });
  return map;
});
const itemNameMap = computed(() => {
  const map: Record<string, string> = {};
  itemOptions.value.forEach((item) => {
    if (item.id != null) {
      map[String(item.id)] = item.itemName || '';
    }
  });
  return map;
});

const memberLabel = (item: NailMemberVO) => {
  const name = item.name || '';
  const phone = item.phone || '';
  const memberNo = item.memberNo || '';
  if (name && phone) {
    return `${name}（${phone}）`;
  }
  if (name && memberNo) {
    return `${name}（${memberNo}）`;
  }
  return name || memberNo || phone || String(item.id ?? '');
};
const employeeLabel = (item: NailEmployeeVO) => {
  if (item.employeeNo) {
    return `${item.name || ''}（${item.employeeNo}）`;
  }
  return item.name || String(item.id ?? '');
};
const itemLabel = (item: NailServiceItemVO) => {
  if (item.durationMin != null && item.durationMin !== '') {
    return `${item.itemName || ''}（${item.durationMin}分钟）`;
  }
  return item.itemName || String(item.id ?? '');
};

const loadSelectOptions = async () => {
  const [memberRes, storeRes, employeeRes, itemRes] = await Promise.all([
    listNailMember({ pageNum: 1, pageSize: 500 }),
    listNailStore({ pageNum: 1, pageSize: 500 }),
    listNailEmployee({ pageNum: 1, pageSize: 500 }),
    listNailServiceItem({ pageNum: 1, pageSize: 500 })
  ]);
  memberOptions.value = memberRes.rows || [];
  storeOptions.value = storeRes.rows || [];
  employeeOptions.value = employeeRes.rows || [];
  itemOptions.value = itemRes.rows || [];
};

const handleStoreChange = () => {
  if (form.value.employeeId == null || form.value.employeeId === '') {
    return;
  }
  const matched = enabledEmployeeOptions.value.some((item) => String(item.id) === String(form.value.employeeId));
  if (!matched) {
    form.value.employeeId = undefined;
  }
};

const handleItemChange = (itemId?: string | number) => {
  if (itemId == null || itemId === '') {
    return;
  }
  const item = itemOptions.value.find((row) => String(row.id) === String(itemId));
  if (item?.durationMin != null && item.durationMin !== '') {
    form.value.durationMin = item.durationMin;
  }
};

const getList = async () => {
  loading.value = true;
  const res = await listNailAppointment(queryParams.value);
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
const handleSelectionChange = (selection: NailAppointmentVO[]) => {
  ids.value = selection.map((item) => item.id!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '新增预约';
};
const handleUpdate = async (row?: NailAppointmentVO) => {
  reset();
  const id = row?.id || ids.value[0];
  const res = await getNailAppointment(id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改预约';
};
const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateNailAppointment(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addNailAppointment(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};
const handleDelete = async (row?: NailAppointmentVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除预约编号为"' + _ids + '"的数据项？');
  await delNailAppointment(_ids);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};
const handleExport = () => {
  proxy?.download('nail/appointment/export', { ...queryParams.value }, `nail_appointment_${new Date().getTime()}.xlsx`);
};

onMounted(async () => {
  await loadSelectOptions();
  getList();
});
</script>
