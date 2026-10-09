package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.nail.domain.NailEmployee;
import org.dromara.nail.domain.NailEmployeePerformance;
import org.dromara.nail.domain.NailServiceItem;
import org.dromara.nail.domain.bo.NailEmployeePerformanceBo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceRankVo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceVo;
import org.dromara.nail.domain.vo.NailEmployeeVo;
import org.dromara.nail.domain.vo.NailServiceItemVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailEmployeeMapper;
import org.dromara.nail.mapper.NailEmployeePerformanceMapper;
import org.dromara.nail.mapper.NailServiceItemMapper;
import org.dromara.nail.service.INailEmployeePerformanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工业绩服务实现
 */
@RequiredArgsConstructor
@Service
public class NailEmployeePerformanceServiceImpl
    implements INailEmployeePerformanceService {

    private final NailEmployeePerformanceMapper baseMapper;
    private final NailEmployeeMapper employeeMapper;
    private final NailServiceItemMapper itemMapper;

    @Override
    public NailEmployeePerformanceVo queryById(Long id) {
        LambdaQueryWrapper<NailEmployeePerformance> lqw = Wrappers.lambdaQuery();
        lqw.eq(NailEmployeePerformance::getId, id);
        NailDataScopeHelper.applyStoreScope(lqw, "NailEmployeePerformance");
        NailEmployeePerformanceVo vo = baseMapper.selectVoOne(lqw);
        if (vo != null) {
            enrich(List.of(vo));
        }
        return vo;
    }

    @Override
    public TableDataInfo<NailEmployeePerformanceVo> queryPageList(
        NailEmployeePerformanceBo bo, PageQuery pageQuery) {
        Page<NailEmployeePerformanceVo> result =
            baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailEmployeePerformanceVo> queryList(NailEmployeePerformanceBo bo) {
        List<NailEmployeePerformanceVo> list =
            baseMapper.selectVoList(buildQueryWrapper(bo));
        enrich(list);
        return list;
    }

    @Override
    public List<NailEmployeePerformanceRankVo> ranking(String period) {
        Date start = periodStart(period);
        LambdaQueryWrapper<NailEmployeePerformance> lqw = Wrappers.lambdaQuery();
        lqw.ge(NailEmployeePerformance::getConsumeTime, start);
        NailDataScopeHelper.applyStoreScope(lqw, "NailEmployeePerformance");
        List<NailEmployeePerformance> records = baseMapper.selectList(lqw);

        Map<Long, NailEmployeePerformanceRankVo> ranks = new LinkedHashMap<>();
        for (NailEmployeePerformance record : records) {
            NailEmployeePerformanceRankVo row = ranks.computeIfAbsent(
                record.getEmployeeId(),
                id -> new NailEmployeePerformanceRankVo(
                    0, id, "", BigDecimal.ZERO, 0L, 0L));
            row.setTotalAmount(row.getTotalAmount().add(record.getAmount()));
            row.setServiceCount(row.getServiceCount() + 1);
            if ("1".equals(record.getPraise())) {
                row.setPraiseCount(row.getPraiseCount() + 1);
            }
        }

        Map<Long, String> names = employeeNames(ranks.keySet());
        List<NailEmployeePerformanceRankVo> result = new ArrayList<>(ranks.values());
        result.sort((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()));
        for (int i = 0; i < result.size(); i++) {
            NailEmployeePerformanceRankVo row = result.get(i);
            row.setRank(i + 1);
            row.setEmployeeName(names.getOrDefault(row.getEmployeeId(), "未知员工"));
        }
        return result;
    }

    @Override
    public Map<String, Object> options() {
        LambdaQueryWrapper<NailEmployee> employeeQuery = Wrappers.lambdaQuery();
        employeeQuery.eq(NailEmployee::getStatus, "0");
        if (!LoginHelper.isSuperAdmin()) {
            Long currentEmployeeId = NailDataScopeHelper.currentEmployeeId();
            if (currentEmployeeId == null) {
                throw new ServiceException("当前账号未关联员工");
            }
            employeeQuery.eq(NailEmployee::getId, currentEmployeeId);
        }
        List<NailEmployeeVo> employees = employeeMapper.selectVoList(employeeQuery);

        LambdaQueryWrapper<NailServiceItem> itemQuery = Wrappers.lambdaQuery();
        itemQuery.eq(NailServiceItem::getStatus, "0");
        itemQuery.orderByAsc(NailServiceItem::getSort);
        List<NailServiceItemVo> items = itemMapper.selectVoList(itemQuery);
        return Map.of("employees", employees, "items", items);
    }

    @Override
    public Boolean insertByBo(NailEmployeePerformanceBo bo) {
        NailEmployeePerformance add =
            MapstructUtils.convert(bo, NailEmployeePerformance.class);
        bindEmployee(add);
        fillItemName(add);
        NailDataScopeHelper.fillStoreId(add);
        if (StringUtils.isBlank(add.getPraise())) {
            add.setPraise("0");
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailEmployeePerformanceBo bo) {
        NailEmployeePerformance exist = scopedEntity(bo.getId());
        NailEmployeePerformance update =
            MapstructUtils.convert(bo, NailEmployeePerformance.class);
        if (!LoginHelper.isSuperAdmin()) {
            update.setEmployeeId(exist.getEmployeeId());
            update.setStoreId(exist.getStoreId());
        } else {
            bindEmployee(update);
        }
        fillItemName(update);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            scopedEntity(id);
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    private LambdaQueryWrapper<NailEmployeePerformance> buildQueryWrapper(
        NailEmployeePerformanceBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailEmployeePerformance> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEmployeeId() != null,
            NailEmployeePerformance::getEmployeeId, bo.getEmployeeId());
        lqw.eq(bo.getStoreId() != null,
            NailEmployeePerformance::getStoreId, bo.getStoreId());
        lqw.eq(bo.getItemId() != null,
            NailEmployeePerformance::getItemId, bo.getItemId());
        lqw.like(StringUtils.isNotBlank(bo.getItemName()),
            NailEmployeePerformance::getItemName, bo.getItemName());
        lqw.like(StringUtils.isNotBlank(bo.getCustomerName()),
            NailEmployeePerformance::getCustomerName, bo.getCustomerName());
        lqw.like(StringUtils.isNotBlank(bo.getCustomerPhone()),
            NailEmployeePerformance::getCustomerPhone, bo.getCustomerPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getPraise()),
            NailEmployeePerformance::getPraise, bo.getPraise());
        lqw.between(params.get("beginConsumeTime") != null
                && params.get("endConsumeTime") != null,
            NailEmployeePerformance::getConsumeTime,
            params.get("beginConsumeTime"), params.get("endConsumeTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailEmployeePerformance");
        lqw.orderByDesc(NailEmployeePerformance::getConsumeTime);
        return lqw;
    }

    private void bindEmployee(NailEmployeePerformance entity) {
        if (!LoginHelper.isSuperAdmin()) {
            Long currentEmployeeId = NailDataScopeHelper.currentEmployeeId();
            if (currentEmployeeId == null) {
                throw new ServiceException("当前账号未关联员工，无法登记业绩");
            }
            entity.setEmployeeId(currentEmployeeId);
        }
        if (entity.getEmployeeId() == null) {
            throw new ServiceException("请选择员工");
        }
        NailEmployee employee = employeeMapper.selectById(entity.getEmployeeId());
        if (employee == null) {
            throw new ServiceException("员工不存在");
        }
        entity.setStoreId(employee.getStoreId());
    }

    private void fillItemName(NailEmployeePerformance entity) {
        if (entity.getItemId() == null) {
            return;
        }
        NailServiceItem item = itemMapper.selectById(entity.getItemId());
        if (item == null) {
            throw new ServiceException("服务项目不存在");
        }
        entity.setItemName(item.getItemName());
    }

    private NailEmployeePerformance scopedEntity(Long id) {
        LambdaQueryWrapper<NailEmployeePerformance> lqw = Wrappers.lambdaQuery();
        lqw.eq(NailEmployeePerformance::getId, id);
        NailDataScopeHelper.applyStoreScope(lqw, "NailEmployeePerformance");
        NailEmployeePerformance entity = baseMapper.selectOne(lqw);
        if (entity == null) {
            throw new ServiceException("业绩记录不存在或无权操作");
        }
        return entity;
    }

    private void enrich(List<NailEmployeePerformanceVo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Map<Long, String> names = employeeNames(
            list.stream().map(NailEmployeePerformanceVo::getEmployeeId).toList());
        list.forEach(row ->
            row.setEmployeeName(names.getOrDefault(row.getEmployeeId(), "未知员工")));
    }

    private Map<Long, String> employeeNames(Collection<Long> ids) {
        Map<Long, String> names = new LinkedHashMap<>();
        if (ids == null || ids.isEmpty()) {
            return names;
        }
        employeeMapper.selectBatchIds(ids).forEach(
            employee -> names.put(employee.getId(), employee.getName()));
        return names;
    }

    private Date periodStart(String period) {
        LocalDate today = LocalDate.now();
        LocalDate start = switch (StringUtils.defaultIfBlank(period, "day")) {
            case "week" -> today.with(DayOfWeek.MONDAY);
            case "month" -> today.withDayOfMonth(1);
            default -> today;
        };
        return Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
