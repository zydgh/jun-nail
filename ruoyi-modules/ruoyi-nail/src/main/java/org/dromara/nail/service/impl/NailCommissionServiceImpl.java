package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailCommission;
import org.dromara.nail.domain.bo.NailCommissionBo;
import org.dromara.nail.domain.vo.NailCommissionVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailCommissionMapper;
import org.dromara.nail.service.INailCommissionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 员工提成Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailCommissionServiceImpl implements INailCommissionService {

    private final NailCommissionMapper baseMapper;

    @Override
    public NailCommissionVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailCommissionVo> queryPageList(NailCommissionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailCommission> lqw = buildQueryWrapper(bo);
        Page<NailCommissionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailCommissionVo> queryList(NailCommissionBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailCommission> buildQueryWrapper(NailCommissionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailCommission> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEmployeeId() != null, NailCommission::getEmployeeId, bo.getEmployeeId());
        lqw.eq(bo.getOrderId() != null, NailCommission::getOrderId, bo.getOrderId());
        lqw.like(StringUtils.isNotBlank(bo.getOrderNo()), NailCommission::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getCommissionRate() != null, NailCommission::getCommissionRate, bo.getCommissionRate());
        lqw.eq(bo.getCommissionAmount() != null, NailCommission::getCommissionAmount, bo.getCommissionAmount());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailCommission::getStatus, bo.getStatus());
        lqw.eq(bo.getSettleTime() != null, NailCommission::getSettleTime, bo.getSettleTime());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailCommission::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailCommission::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailCommission");
        lqw.orderByDesc(NailCommission::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailCommissionBo bo) {
        NailCommission add = MapstructUtils.convert(bo, NailCommission.class);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailCommissionBo bo) {
        NailCommission update = MapstructUtils.convert(bo, NailCommission.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
