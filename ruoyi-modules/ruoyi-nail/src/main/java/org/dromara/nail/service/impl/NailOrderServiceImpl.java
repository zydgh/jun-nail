package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailOrder;
import org.dromara.nail.domain.bo.NailOrderBo;
import org.dromara.nail.domain.vo.NailOrderVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailOrderMapper;
import org.dromara.nail.service.INailOrderService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 订单Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailOrderServiceImpl implements INailOrderService {

    private final NailOrderMapper baseMapper;

    @Override
    public NailOrderVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailOrderVo> queryPageList(NailOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailOrder> lqw = buildQueryWrapper(bo);
        Page<NailOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailOrderVo> queryList(NailOrderBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
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
