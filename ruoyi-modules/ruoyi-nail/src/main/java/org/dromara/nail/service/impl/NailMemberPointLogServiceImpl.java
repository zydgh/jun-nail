package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailMemberPointLog;
import org.dromara.nail.domain.bo.NailMemberPointLogBo;
import org.dromara.nail.domain.vo.NailMemberPointLogVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailMemberPointLogMapper;
import org.dromara.nail.service.INailMemberPointLogService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 积分变动Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailMemberPointLogServiceImpl implements INailMemberPointLogService {

    private final NailMemberPointLogMapper baseMapper;

    @Override
    public NailMemberPointLogVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailMemberPointLogVo> queryPageList(NailMemberPointLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailMemberPointLog> lqw = buildQueryWrapper(bo);
        Page<NailMemberPointLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailMemberPointLogVo> queryList(NailMemberPointLogBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailMemberPointLog> buildQueryWrapper(NailMemberPointLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailMemberPointLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMemberId() != null, NailMemberPointLog::getMemberId, bo.getMemberId());
        lqw.like(StringUtils.isNotBlank(bo.getChangeType()), NailMemberPointLog::getChangeType, bo.getChangeType());
        lqw.eq(bo.getPoint() != null, NailMemberPointLog::getPoint, bo.getPoint());
        lqw.eq(bo.getBeforePoint() != null, NailMemberPointLog::getBeforePoint, bo.getBeforePoint());
        lqw.eq(bo.getAfterPoint() != null, NailMemberPointLog::getAfterPoint, bo.getAfterPoint());
        lqw.like(StringUtils.isNotBlank(bo.getBizNo()), NailMemberPointLog::getBizNo, bo.getBizNo());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailMemberPointLog::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailMemberPointLog::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailMemberPointLog");
        lqw.orderByDesc(NailMemberPointLog::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailMemberPointLogBo bo) {
        NailMemberPointLog add = MapstructUtils.convert(bo, NailMemberPointLog.class);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailMemberPointLogBo bo) {
        NailMemberPointLog update = MapstructUtils.convert(bo, NailMemberPointLog.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
