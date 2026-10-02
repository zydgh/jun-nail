package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailStore;
import org.dromara.nail.domain.bo.NailStoreBo;
import org.dromara.nail.domain.vo.NailStoreVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailStoreMapper;
import org.dromara.nail.service.INailStoreService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 门店Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailStoreServiceImpl implements INailStoreService {

    private final NailStoreMapper baseMapper;

    @Override
    public NailStoreVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailStoreVo> queryPageList(NailStoreBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailStore> lqw = buildQueryWrapper(bo);
        Page<NailStoreVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailStoreVo> queryList(NailStoreBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailStore> buildQueryWrapper(NailStoreBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailStore> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getStoreCode()), NailStore::getStoreCode, bo.getStoreCode());
        lqw.like(StringUtils.isNotBlank(bo.getStoreName()), NailStore::getStoreName, bo.getStoreName());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), NailStore::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), NailStore::getAddress, bo.getAddress());
        lqw.like(StringUtils.isNotBlank(bo.getManager()), NailStore::getManager, bo.getManager());
        lqw.eq(bo.getSort() != null, NailStore::getSort, bo.getSort());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailStore::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailStore::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailStore::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailStore");
        lqw.orderByDesc(NailStore::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailStoreBo bo) {
        NailStore add = MapstructUtils.convert(bo, NailStore.class);

        if (StringUtils.isBlank(add.getStoreCode())) {
            add.setStoreCode("MD" + System.currentTimeMillis());
        }

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailStoreBo bo) {
        NailStore update = MapstructUtils.convert(bo, NailStore.class);
        NailStoreVo exist = baseMapper.selectVoById(bo.getId());
        if (exist != null) {
            update.setStoreCode(exist.getStoreCode());
        }
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
