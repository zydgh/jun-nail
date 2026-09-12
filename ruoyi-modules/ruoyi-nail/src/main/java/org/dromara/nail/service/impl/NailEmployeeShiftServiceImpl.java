package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailEmployeeShift;
import org.dromara.nail.domain.bo.NailEmployeeShiftBo;
import org.dromara.nail.domain.vo.NailEmployeeShiftVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailEmployeeShiftMapper;
import org.dromara.nail.service.INailEmployeeShiftService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 员工排班Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailEmployeeShiftServiceImpl implements INailEmployeeShiftService {

    private final NailEmployeeShiftMapper baseMapper;

    @Override
    public NailEmployeeShiftVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailEmployeeShiftVo> queryPageList(NailEmployeeShiftBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailEmployeeShift> lqw = buildQueryWrapper(bo);
        Page<NailEmployeeShiftVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailEmployeeShiftVo> queryList(NailEmployeeShiftBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailEmployeeShift> buildQueryWrapper(NailEmployeeShiftBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailEmployeeShift> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEmployeeId() != null, NailEmployeeShift::getEmployeeId, bo.getEmployeeId());
        lqw.eq(bo.getStoreId() != null, NailEmployeeShift::getStoreId, bo.getStoreId());
        lqw.eq(bo.getShiftDate() != null, NailEmployeeShift::getShiftDate, bo.getShiftDate());
        lqw.eq(StringUtils.isNotBlank(bo.getStartTime()), NailEmployeeShift::getStartTime, bo.getStartTime());
        lqw.eq(StringUtils.isNotBlank(bo.getEndTime()), NailEmployeeShift::getEndTime, bo.getEndTime());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailEmployeeShift::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailEmployeeShift::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailEmployeeShift::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailEmployeeShift");
        lqw.orderByDesc(NailEmployeeShift::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailEmployeeShiftBo bo) {
        NailEmployeeShift add = MapstructUtils.convert(bo, NailEmployeeShift.class);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailEmployeeShiftBo bo) {
        NailEmployeeShift update = MapstructUtils.convert(bo, NailEmployeeShift.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
