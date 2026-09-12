package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailMemberBalanceLog;
import org.dromara.nail.domain.bo.NailMemberBalanceLogBo;
import org.dromara.nail.domain.vo.NailMemberBalanceLogVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailMemberBalanceLogMapper;
import org.dromara.nail.service.INailMemberBalanceLogService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 余额变动Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailMemberBalanceLogServiceImpl implements INailMemberBalanceLogService {

    private final NailMemberBalanceLogMapper baseMapper;

    @Override
    public NailMemberBalanceLogVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailMemberBalanceLogVo> queryPageList(NailMemberBalanceLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailMemberBalanceLog> lqw = buildQueryWrapper(bo);
        Page<NailMemberBalanceLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailMemberBalanceLogVo> queryList(NailMemberBalanceLogBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailMemberBalanceLog> buildQueryWrapper(NailMemberBalanceLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailMemberBalanceLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMemberId() != null, NailMemberBalanceLog::getMemberId, bo.getMemberId());
        lqw.like(StringUtils.isNotBlank(bo.getChangeType()), NailMemberBalanceLog::getChangeType, bo.getChangeType());
        lqw.eq(bo.getAmount() != null, NailMemberBalanceLog::getAmount, bo.getAmount());
        lqw.eq(bo.getBeforeBalance() != null, NailMemberBalanceLog::getBeforeBalance, bo.getBeforeBalance());
        lqw.eq(bo.getAfterBalance() != null, NailMemberBalanceLog::getAfterBalance, bo.getAfterBalance());
        lqw.like(StringUtils.isNotBlank(bo.getBizNo()), NailMemberBalanceLog::getBizNo, bo.getBizNo());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailMemberBalanceLog::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailMemberBalanceLog::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailMemberBalanceLog");
        lqw.orderByDesc(NailMemberBalanceLog::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailMemberBalanceLogBo bo) {
        NailMemberBalanceLog add = MapstructUtils.convert(bo, NailMemberBalanceLog.class);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailMemberBalanceLogBo bo) {
        NailMemberBalanceLog update = MapstructUtils.convert(bo, NailMemberBalanceLog.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
