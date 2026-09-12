package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailOrderItem;
import org.dromara.nail.domain.bo.NailOrderItemBo;
import org.dromara.nail.domain.vo.NailOrderItemVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailOrderItemMapper;
import org.dromara.nail.service.INailOrderItemService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 订单明细Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailOrderItemServiceImpl implements INailOrderItemService {

    private final NailOrderItemMapper baseMapper;

    @Override
    public NailOrderItemVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailOrderItemVo> queryPageList(NailOrderItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailOrderItem> lqw = buildQueryWrapper(bo);
        Page<NailOrderItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailOrderItemVo> queryList(NailOrderItemBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailOrderItem> buildQueryWrapper(NailOrderItemBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailOrderItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOrderId() != null, NailOrderItem::getOrderId, bo.getOrderId());
        lqw.eq(bo.getItemId() != null, NailOrderItem::getItemId, bo.getItemId());
        lqw.like(StringUtils.isNotBlank(bo.getItemName()), NailOrderItem::getItemName, bo.getItemName());
        lqw.eq(bo.getQuantity() != null, NailOrderItem::getQuantity, bo.getQuantity());
        lqw.eq(bo.getPrice() != null, NailOrderItem::getPrice, bo.getPrice());
        lqw.eq(bo.getAmount() != null, NailOrderItem::getAmount, bo.getAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailOrderItem::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailOrderItem::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailOrderItem");
        lqw.orderByDesc(NailOrderItem::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailOrderItemBo bo) {
        NailOrderItem add = MapstructUtils.convert(bo, NailOrderItem.class);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailOrderItemBo bo) {
        NailOrderItem update = MapstructUtils.convert(bo, NailOrderItem.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
