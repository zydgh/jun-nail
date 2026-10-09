<template>
  <div class="p-2">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="业绩记录" name="records">
        <el-card class="mb-[10px]" shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="员工" prop="employeeId">
              <el-select v-model="queryParams.employeeId" placeholder="请选择员工" clearable filterable>
                <el-option v-for="item in employeeOptions" :key="item.id" :label="employeeLabel(item)" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="客户" prop="customerName">
              <el-input v-model="queryParams.customerName" placeholder="客户姓名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="手机号" prop="customerPhone">
              <el-input v-model="queryParams.customerPhone" placeholder="客户手机号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="消费时间">
              <el-date-picker
                v-model="dateRange"
                type="datetimerange"
                value-format="YYYY-MM-DD HH:mm:ss"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="hover">
          <template #header>
            <el-row :gutter="10">
              <el-col :span="1.5">
                <el-button v-hasPermi="['employee:performance:add']" type="primary" plain icon="Plus" @click="handleAdd"> 新增业绩 </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button v-hasPermi="['employee:performance:edit']" type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">
                  修改
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  v-hasPermi="['employee:performance:remove']"
                  type="danger"
                  plain
                  icon="Delete"
                  :disabled="multiple"
                  @click="handleDelete()"
                >
                  删除
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button v-hasPermi="['employee:performance:export']" type="warning" plain icon="Download" @click="handleExport"> 导出 </el-button>
              </el-col>
              <right-toolbar v-model:show-search="showSearch" @query-table="getList" />
            </el-row>
          </template>

          <el-table v-loading="loading" border :data="dataList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="员工" prop="employeeName" align="center" min-width="100" />
            <el-table-column label="业绩金额" prop="amount" align="right" min-width="110">
              <template #default="scope">¥ {{ Number(scope.row.amount || 0).toFixed(2) }}</template>
            </el-table-column>
            <el-table-column label="服务项目" prop="itemName" align="center" min-width="130" show-overflow-tooltip />
            <el-table-column label="客户姓名" prop="customerName" align="center" min-width="100" />
            <el-table-column label="客户手机号" prop="customerPhone" align="center" min-width="130" />
            <el-table-column label="消费时间" prop="consumeTime" align="center" width="165" />
            <el-table-column label="好评" prop="praise" align="center" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.praise === '1' ? 'success' : 'info'">
                  {{ scope.row.praise === '1' ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="备注" prop="remark" align="center" min-width="130" show-overflow-tooltip />
            <el-table-column label="操作" align="center" width="130" fixed="right">
              <template #default="scope">
                <el-button v-hasPermi="['employee:performance:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
                <el-button v-hasPermi="['employee:performance:remove']" link type="danger" icon="Delete" @click="handleDelete(scope.row)" />
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="total > 0"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            :total="total"
            @pagination="getList"
          />
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="业绩排行榜" name="ranking">
        <el-card shadow="hover">
          <template #header>
            <div class="ranking-header">
              <span>员工业绩排行榜</span>
              <el-radio-group v-model="rankPeriod" @change="getRanking">
                <el-radio-button value="day">当天</el-radio-button>
                <el-radio-button value="week">本周</el-radio-button>
                <el-radio-button value="month">本月</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div v-loading="rankLoading" class="ranking-body">
            <el-row :gutter="16">
              <el-col :xs="24" :lg="14">
                <div ref="amountChartRef" class="rank-chart"></div>
              </el-col>
              <el-col :xs="24" :lg="10">
                <div ref="countChartRef" class="rank-chart"></div>
              </el-col>
            </el-row>
            <el-table border :data="rankingList" class="mt-4">
              <el-table-column label="排名" prop="rank" align="center" width="90">
                <template #default="scope">
                  <span :class="`rank rank-${scope.row.rank}`">{{ scope.row.rank }}</span>
                </template>
              </el-table-column>
              <el-table-column label="员工" prop="employeeName" align="center" />
              <el-table-column label="业绩金额" prop="totalAmount" align="right">
                <template #default="scope">
                  <strong class="amount">¥ {{ Number(scope.row.totalAmount || 0).toFixed(2) }}</strong>
                </template>
              </el-table-column>
              <el-table-column label="服务单数" prop="serviceCount" align="center" />
              <el-table-column label="好评数" prop="praiseCount" align="center" />
            </el-table>
            <el-empty v-if="!rankLoading && rankingList.length === 0" description="当前周期暂无业绩记录" />
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="620px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="员工" prop="employeeId">
          <el-select v-model="form.employeeId" placeholder="请选择员工" clearable filterable style="width: 100%">
            <el-option v-for="item in enabledEmployeeOptions" :key="item.id" :label="employeeLabel(item)" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="业绩金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0.01" :precision="2" :step="10" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="服务项目" prop="itemId">
          <el-select v-model="form.itemId" placeholder="请选择项目" clearable filterable style="width: 100%">
            <el-option v-for="item in enabledItemOptions" :key="item.id" :label="item.itemName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="客户手机号" prop="customerPhone">
          <el-input v-model="form.customerPhone" placeholder="请输入客户手机号" maxlength="20" />
        </el-form-item>
        <el-form-item label="消费时间" prop="consumeTime">
          <el-date-picker
            v-model="form.consumeTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择消费时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="客户好评" prop="praise">
          <el-switch v-model="form.praise" active-value="1" inactive-value="0" active-text="好评" inactive-text="未好评" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="NailEmployeePerformance" lang="ts">
import {
  addNailEmployeePerformance,
  delNailEmployeePerformance,
  getNailEmployeePerformance,
  getNailEmployeePerformanceOptions,
  getNailEmployeePerformanceRanking,
  listNailEmployeePerformance,
  updateNailEmployeePerformance
} from '@/api/employee/performance';
import {
  NailEmployeePerformanceForm,
  NailEmployeePerformanceQuery,
  NailEmployeePerformanceRankVO,
  NailEmployeePerformanceVO
} from '@/api/employee/performance/types';
import { NailEmployeeVO } from '@/api/employee/employee/types';
import { NailServiceItemVO } from '@/api/nail/item/types';
import * as echarts from 'echarts';
import dayjs from 'dayjs';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const activeTab = ref('records');
const loading = ref(false);
const rankLoading = ref(false);
const buttonLoading = ref(false);
const showSearch = ref(true);
const dataList = ref<NailEmployeePerformanceVO[]>([]);
const rankingList = ref<NailEmployeePerformanceRankVO[]>([]);
const employeeOptions = ref<NailEmployeeVO[]>([]);
const itemOptions = ref<NailServiceItemVO[]>([]);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const rankPeriod = ref<'day' | 'week' | 'month'>('day');
const dateRange = ref<[string, string]>();
const queryFormRef = ref<ElFormInstance>();
const formRef = ref<ElFormInstance>();
const amountChartRef = ref<HTMLDivElement>();
const countChartRef = ref<HTMLDivElement>();
const amountChart = shallowRef<echarts.ECharts>();
const countChart = shallowRef<echarts.ECharts>();
const dialog = reactive<DialogOption>({ visible: false, title: '' });
const periodLabel = computed(() => {
  if (rankPeriod.value === 'week') {
    return '本周';
  }
  if (rankPeriod.value === 'month') {
    return '本月';
  }
  return '当天';
});

const initFormData: NailEmployeePerformanceForm = {
  id: undefined,
  employeeId: undefined,
  amount: undefined,
  itemId: undefined,
  customerName: undefined,
  customerPhone: undefined,
  consumeTime: undefined,
  praise: '0',
  remark: undefined
};

const data = reactive({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    employeeId: undefined,
    customerName: undefined,
    customerPhone: undefined
  } as NailEmployeePerformanceQuery,
  rules: {
    employeeId: [{ required: true, message: '请选择员工', trigger: 'change' }],
    amount: [{ required: true, message: '请输入业绩金额', trigger: 'blur' }],
    itemId: [{ required: true, message: '请选择服务项目', trigger: 'change' }],
    customerName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
    customerPhone: [{ required: true, message: '请输入客户手机号', trigger: 'blur' }],
    consumeTime: [{ required: true, message: '请选择消费时间', trigger: 'change' }]
  }
});
const { form, queryParams, rules } = toRefs(data);

const enabledEmployeeOptions = computed(() => employeeOptions.value.filter((item) => !item.status || item.status === '0'));
const enabledItemOptions = computed(() => itemOptions.value.filter((item) => !item.status || item.status === '0'));
const employeeLabel = (item: NailEmployeeVO) => (item.employeeNo ? `${item.name}（${item.employeeNo}）` : item.name || String(item.id));

const loadOptions = async () => {
  const res = await getNailEmployeePerformanceOptions();
  employeeOptions.value = res.data?.employees || [];
  itemOptions.value = res.data?.items || [];
};

const getList = async () => {
  loading.value = true;
  try {
    const params = proxy?.addDateRange(queryParams.value, dateRange.value, 'ConsumeTime');
    const res = await listNailEmployeePerformance(params);
    dataList.value = res.rows || [];
    total.value = res.total;
  } finally {
    loading.value = false;
  }
};

const ensureCharts = () => {
  if (amountChartRef.value && !amountChart.value) {
    amountChart.value = echarts.init(amountChartRef.value);
  }
  if (countChartRef.value && !countChart.value) {
    countChart.value = echarts.init(countChartRef.value);
  }
};

const renderRankingCharts = () => {
  nextTick(() => {
    ensureCharts();
    const names = rankingList.value.map((item) => item.employeeName || '未知员工').reverse();
    const amounts = rankingList.value.map((item) => Number(item.totalAmount || 0)).reverse();
    const serviceCounts = rankingList.value.map((item) => Number(item.serviceCount || 0));
    const praiseCounts = rankingList.value.map((item) => Number(item.praiseCount || 0));
    const categories = rankingList.value.map((item) => item.employeeName || '未知员工');

    amountChart.value?.setOption(
      {
        title: {
          text: `${periodLabel.value}业绩金额排行`,
          left: 'center',
          textStyle: { fontSize: 14, fontWeight: 600 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          valueFormatter: (value: number) => `¥ ${Number(value || 0).toFixed(2)}`
        },
        grid: { left: 80, right: 24, top: 48, bottom: 24 },
        xAxis: { type: 'value', axisLabel: { formatter: (value: number) => `¥${value}` } },
        yAxis: { type: 'category', data: names },
        series: [
          {
            name: '业绩金额',
            type: 'bar',
            data: amounts,
            barMaxWidth: 28,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#f6a96a' },
                { offset: 1, color: '#e85d4c' }
              ]),
              borderRadius: [0, 6, 6, 0]
            },
            label: {
              show: true,
              position: 'right',
              formatter: (params: any) => `¥ ${Number(params.value || 0).toFixed(0)}`
            }
          }
        ]
      },
      true
    );

    countChart.value?.setOption(
      {
        title: {
          text: `${periodLabel.value}服务与好评`,
          left: 'center',
          textStyle: { fontSize: 14, fontWeight: 600 }
        },
        tooltip: { trigger: 'axis' },
        legend: { top: 28, data: ['服务单数', '好评数'] },
        grid: { left: 48, right: 16, top: 64, bottom: 28 },
        xAxis: {
          type: 'category',
          data: categories,
          axisLabel: { interval: 0, rotate: categories.length > 4 ? 30 : 0 }
        },
        yAxis: { type: 'value', minInterval: 1 },
        series: [
          {
            name: '服务单数',
            type: 'bar',
            data: serviceCounts,
            barMaxWidth: 24,
            itemStyle: { color: '#4c8bf5', borderRadius: [4, 4, 0, 0] }
          },
          {
            name: '好评数',
            type: 'bar',
            data: praiseCounts,
            barMaxWidth: 24,
            itemStyle: { color: '#3cb371', borderRadius: [4, 4, 0, 0] }
          }
        ]
      },
      true
    );

    amountChart.value?.resize();
    countChart.value?.resize();
  });
};

const getRanking = async () => {
  rankLoading.value = true;
  try {
    const res = await getNailEmployeePerformanceRanking(rankPeriod.value);
    rankingList.value = res.data || [];
    renderRankingCharts();
  } finally {
    rankLoading.value = false;
  }
};

const handleTabChange = (name: string | number) => {
  if (name === 'ranking') {
    getRanking();
  }
};

const handleResize = () => {
  amountChart.value?.resize();
  countChart.value?.resize();
};
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};
const resetQuery = () => {
  dateRange.value = undefined;
  queryFormRef.value?.resetFields();
  handleQuery();
};
const reset = () => {
  form.value = {
    ...initFormData,
    consumeTime: dayjs().format('YYYY-MM-DD HH:mm:ss')
  };
  formRef.value?.resetFields();
};
const cancel = () => {
  dialog.visible = false;
  reset();
};
const handleSelectionChange = (selection: NailEmployeePerformanceVO[]) => {
  ids.value = selection.map((item) => item.id!);
  single.value = selection.length !== 1;
  multiple.value = selection.length === 0;
};
const handleAdd = () => {
  reset();
  if (enabledEmployeeOptions.value.length === 1) {
    form.value.employeeId = enabledEmployeeOptions.value[0].id;
  }
  dialog.title = '新增业绩记录';
  dialog.visible = true;
};
const handleUpdate = async (row?: NailEmployeePerformanceVO) => {
  reset();
  const id = row?.id || ids.value[0];
  const res = await getNailEmployeePerformance(id);
  Object.assign(form.value, res.data);
  dialog.title = '修改业绩记录';
  dialog.visible = true;
};
const submitForm = () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) return;
    buttonLoading.value = true;
    try {
      if (form.value.id) {
        await updateNailEmployeePerformance(form.value);
      } else {
        await addNailEmployeePerformance(form.value);
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    } finally {
      buttonLoading.value = false;
    }
  });
};
const handleDelete = async (row?: NailEmployeePerformanceVO) => {
  const selectedIds = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除选中的业绩记录？');
  await delNailEmployeePerformance(selectedIds);
  proxy?.$modal.msgSuccess('删除成功');
  await getList();
};
const handleExport = () => {
  const params = proxy?.addDateRange(queryParams.value, dateRange.value, 'ConsumeTime');
  proxy?.download('employee/performance/export', params, `employee_performance_${Date.now()}.xlsx`);
};

onMounted(async () => {
  await loadOptions();
  await getList();
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  amountChart.value?.dispose();
  countChart.value?.dispose();
});
</script>

<style scoped>
.ranking-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.ranking-body {
  min-height: 240px;
}

.rank-chart {
  width: 100%;
  height: 360px;
}

.rank {
  display: inline-flex;
  width: 30px;
  height: 30px;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--el-fill-color);
  font-weight: 700;
}

.rank-1 {
  background: #f6c344;
  color: #7a4c00;
}

.rank-2 {
  background: #d9dce3;
  color: #4b5563;
}

.rank-3 {
  background: #dca56b;
  color: #6b3c16;
}

.amount {
  color: var(--el-color-danger);
}
</style>
