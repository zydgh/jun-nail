package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailServiceItem;
import org.dromara.nail.domain.bo.NailServiceItemBo;
import org.dromara.nail.domain.vo.NailServiceItemVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailServiceItemMapper;
import org.dromara.nail.service.INailServiceItemService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 美甲项目Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailServiceItemServiceImpl implements INailServiceItemService {

    private final NailServiceItemMapper baseMapper;

    @Override
    public NailServiceItemVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailServiceItemVo> queryPageList(NailServiceItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailServiceItem> lqw = buildQueryWrapper(bo);
        Page<NailServiceItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailServiceItemVo> queryList(NailServiceItemBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailServiceItem> buildQueryWrapper(NailServiceItemBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailServiceItem> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getItemCode()), NailServiceItem::getItemCode, bo.getItemCode());
        lqw.like(StringUtils.isNotBlank(bo.getItemName()), NailServiceItem::getItemName, bo.getItemName());
        lqw.like(StringUtils.isNotBlank(bo.getCategory()), NailServiceItem::getCategory, bo.getCategory());
        lqw.eq(bo.getDurationMin() != null, NailServiceItem::getDurationMin, bo.getDurationMin());
        lqw.eq(bo.getPrice() != null, NailServiceItem::getPrice, bo.getPrice());
        lqw.eq(bo.getMemberPrice() != null, NailServiceItem::getMemberPrice, bo.getMemberPrice());
        lqw.eq(bo.getSort() != null, NailServiceItem::getSort, bo.getSort());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailServiceItem::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailServiceItem::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailServiceItem::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailServiceItem");
        lqw.orderByDesc(NailServiceItem::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailServiceItemBo bo) {
        NailServiceItem add = MapstructUtils.convert(bo, NailServiceItem.class);

        if (StringUtils.isBlank(add.getItemCode())) {
            add.setItemCode("XM" + System.currentTimeMillis());
        }

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailServiceItemBo bo) {
        NailServiceItem update = MapstructUtils.convert(bo, NailServiceItem.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
