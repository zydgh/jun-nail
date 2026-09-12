<template>
  <div class="p-2">
    <el-row :gutter="16">
      <el-col v-for="item in cards" :key="item.label" :xs="24" :sm="12" :lg="6">
        <el-card shadow="hover" class="mb-4">
          <div class="text-sm text-gray-500">{{ item.label }}</div>
          <div class="text-2xl font-semibold mt-2">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="mb-4">
          <template #header>近7日会员新增</template>
          <div ref="memberChartRef" style="height: 320px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="mb-4">
          <template #header>近7日营业额</template>
          <div ref="revenueChartRef" style="height: 320px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24">
        <el-card shadow="hover">
          <template #header>本月员工业绩 TOP5</template>
          <div ref="rankChartRef" style="height: 320px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="NailDashboard" lang="ts">
import * as echarts from 'echarts';
import { getDashboardOverview } from '@/api/nail/dashboard';

const memberChartRef = ref<HTMLDivElement>();
const revenueChartRef = ref<HTMLDivElement>();
const rankChartRef = ref<HTMLDivElement>();
const cards = ref([
  { label: '在籍会员', value: '0' },
  { label: '在职员工', value: '0' },
  { label: '今日订单', value: '0' },
  { label: '今日营业额', value: '0' }
]);

const renderLine = (el: HTMLDivElement | undefined, title: string, names: string[], values: number[]) => {
  if (!el) {
    return;
  }
  const chart = echarts.init(el);
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: names },
    yAxis: { type: 'value' },
    series: [{ name: title, type: 'line', smooth: true, data: values, areaStyle: {} }]
  });
};

const load = async () => {
  const res = await getDashboardOverview();
  const data = res.data;
  cards.value = [
    { label: '在籍会员', value: String(data.memberCount ?? 0) },
    { label: '在职员工', value: String(data.employeeCount ?? 0) },
    { label: '今日订单', value: String(data.todayOrderCount ?? 0) },
    { label: '今日营业额', value: String(data.todayAmount ?? 0) }
  ];
  renderLine(
    memberChartRef.value,
    '新增会员',
    (data.memberTrend || []).map((i: any) => i.name),
    (data.memberTrend || []).map((i: any) => Number(i.value))
  );
  renderLine(
    revenueChartRef.value,
    '营业额',
    (data.revenueTrend || []).map((i: any) => i.name),
    (data.revenueTrend || []).map((i: any) => Number(i.value))
  );
  if (rankChartRef.value) {
    const chart = echarts.init(rankChartRef.value);
    chart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'value' },
      yAxis: { type: 'category', data: (data.employeeRank || []).map((i: any) => i.name) },
      series: [{ type: 'bar', data: (data.employeeRank || []).map((i: any) => Number(i.value)) }]
    });
  }
};

onMounted(() => {
  load();
});
</script>
