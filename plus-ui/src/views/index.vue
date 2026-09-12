<template>
  <div class="p-2">
    <el-card shadow="hover" class="mb-4">
      <div class="flex flex-wrap items-center justify-between gap-2">
        <div>
          <div class="text-xl font-semibold">君君美甲工作台</div>
          <div class="text-sm text-gray-500 mt-1">今日预约、收银、会员生日与员工业绩一览</div>
        </div>
        <el-tag type="success">本月营业额 {{ monthAmount }}</el-tag>
      </div>
    </el-card>
    <nail-dashboard />
  </div>
</template>

<script setup name="Index" lang="ts">
import NailDashboard from '@/views/nail/dashboard/index.vue';
import { getDashboardOverview } from '@/api/nail/dashboard';

const monthAmount = ref('0.00');

onMounted(async () => {
  try {
    const res = await getDashboardOverview();
    monthAmount.value = String(res.data.monthAmount ?? '0.00');
  } catch {
    monthAmount.value = '--';
  }
});
</script>
