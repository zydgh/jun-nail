package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailMemberLevel;
import org.dromara.nail.domain.bo.NailMemberLevelBo;
import org.dromara.nail.domain.vo.NailMemberLevelVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailMemberLevelMapper;
import org.dromara.nail.service.INailMemberLevelService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 会员等级Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailMemberLevelServiceImpl implements INailMemberLevelService {

    private final NailMemberLevelMapper baseMapper;

    @Override
    public NailMemberLevelVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailMemberLevelVo> queryPageList(NailMemberLevelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailMemberLevel> lqw = buildQueryWrapper(bo);
        Page<NailMemberLevelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailMemberLevelVo> queryList(NailMemberLevelBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailMemberLevel> buildQueryWrapper(NailMemberLevelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailMemberLevel> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getLevelName()), NailMemberLevel::getLevelName, bo.getLevelName());
        lqw.eq(bo.getDiscount() != null, NailMemberLevel::getDiscount, bo.getDiscount());
        lqw.eq(bo.getUpgradePoint() != null, NailMemberLevel::getUpgradePoint, bo.getUpgradePoint());
        lqw.eq(bo.getSort() != null, NailMemberLevel::getSort, bo.getSort());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailMemberLevel::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailMemberLevel::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailMemberLevel::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailMemberLevel");
        lqw.orderByDesc(NailMemberLevel::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailMemberLevelBo bo) {
        NailMemberLevel add = MapstructUtils.convert(bo, NailMemberLevel.class);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailMemberLevelBo bo) {
        NailMemberLevel update = MapstructUtils.convert(bo, NailMemberLevel.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
