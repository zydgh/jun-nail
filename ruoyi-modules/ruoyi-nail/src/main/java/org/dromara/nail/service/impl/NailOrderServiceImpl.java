package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailEmployee;
import org.dromara.nail.domain.NailOrder;
import org.dromara.nail.domain.NailOrderItem;
import org.dromara.nail.domain.bo.NailOrderBo;
import org.dromara.nail.domain.vo.NailOrderVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailEmployeeMapper;
import org.dromara.nail.mapper.NailOrderItemMapper;
import org.dromara.nail.mapper.NailOrderMapper;
import org.dromara.nail.service.INailOrderService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailOrderServiceImpl implements INailOrderService {

    private final NailOrderMapper baseMapper;
    private final NailEmployeeMapper employeeMapper;
    private final NailOrderItemMapper orderItemMapper;

    @Override
    public NailOrderVo queryById(Long id) {
        NailOrderVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            enrich(List.of(vo));
        }
        return vo;
    }

    @Override
    public TableDataInfo<NailOrderVo> queryPageList(NailOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailOrder> lqw = buildQueryWrapper(bo);
        Page<NailOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        enrich(result.getRecords());
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailOrderVo> queryList(NailOrderBo bo) {
        List<NailOrderVo> list = baseMapper.selectVoList(buildQueryWrapper(bo));
        enrich(list);
        return list;
    }

    private void enrich(List<NailOrderVo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<Long> employeeIds = list.stream().map(NailOrderVo::getEmployeeId).filter(id -> id != null).distinct().toList();
        Map<Long, String> names = employeeIds.isEmpty() ? Map.of() : employeeMapper.selectList(
            Wrappers.<NailEmployee>lambdaQuery().in(NailEmployee::getId, employeeIds)
        ).stream().collect(Collectors.toMap(NailEmployee::getId, NailEmployee::getName, (a, b) -> a));
        List<Long> orderIds = list.stream().map(NailOrderVo::getId).toList();
        Map<Long, String> itemNames = orderItemMapper.selectList(
            Wrappers.<NailOrderItem>lambdaQuery().in(NailOrderItem::getOrderId, orderIds)
        ).stream().collect(Collectors.toMap(NailOrderItem::getOrderId, NailOrderItem::getItemName, (a, b) -> a));
        list.forEach(row -> {
            row.setEmployeeName(names.getOrDefault(row.getEmployeeId(), ""));
            row.setItemName(itemNames.getOrDefault(row.getId(), ""));
        });
    }

    private LambdaQueryWrapper<NailOrder> buildQueryWrapper(NailOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailOrder> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getOrderNo()), NailOrder::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getMemberId() != null, NailOrder::getMemberId, bo.getMemberId());
        lqw.eq(bo.getEmployeeId() != null, NailOrder::getEmployeeId, bo.getEmployeeId());
        lqw.eq(bo.getStoreId() != null, NailOrder::getStoreId, bo.getStoreId());
        lqw.eq(bo.getOrderTime() != null, NailOrder::getOrderTime, bo.getOrderTime());
        lqw.like(StringUtils.isNotBlank(bo.getPayType()), NailOrder::getPayType, bo.getPayType());
        lqw.eq(bo.getTotalAmount() != null, NailOrder::getTotalAmount, bo.getTotalAmount());
        lqw.eq(bo.getDiscountAmount() != null, NailOrder::getDiscountAmount, bo.getDiscountAmount());
        lqw.eq(bo.getPayAmount() != null, NailOrder::getPayAmount, bo.getPayAmount());
        lqw.eq(bo.getPointAmount() != null, NailOrder::getPointAmount, bo.getPointAmount());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailOrder::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailOrder::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailOrder::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailOrder");
        lqw.orderByDesc(NailOrder::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailOrderBo bo) {
        NailOrder add = MapstructUtils.convert(bo, NailOrder.class);

        if (StringUtils.isBlank(add.getOrderNo())) {
            add.setOrderNo("DD" + System.currentTimeMillis());
        }
        if (add.getOrderTime() == null) {
            add.setOrderTime(new java.util.Date());
        }
        org.dromara.nail.helper.NailDataScopeHelper.fillStoreId(add);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailOrderBo bo) {
        NailOrder update = MapstructUtils.convert(bo, NailOrder.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
