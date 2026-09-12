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
import org.dromara.nail.domain.bo.NailEmployeeBo;
import org.dromara.nail.domain.vo.NailEmployeeVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailEmployeeMapper;
import org.dromara.nail.service.INailEmployeeService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 员工Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailEmployeeServiceImpl implements INailEmployeeService {

    private final NailEmployeeMapper baseMapper;

    @Override
    public NailEmployeeVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailEmployeeVo> queryPageList(NailEmployeeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailEmployee> lqw = buildQueryWrapper(bo);
        Page<NailEmployeeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailEmployeeVo> queryList(NailEmployeeBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailEmployee> buildQueryWrapper(NailEmployeeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailEmployee> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getEmployeeNo()), NailEmployee::getEmployeeNo, bo.getEmployeeNo());
        lqw.like(StringUtils.isNotBlank(bo.getName()), NailEmployee::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), NailEmployee::getPhone, bo.getPhone());
        lqw.like(StringUtils.isNotBlank(bo.getSex()), NailEmployee::getSex, bo.getSex());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), NailEmployee::getAvatar, bo.getAvatar());
        lqw.eq(bo.getStoreId() != null, NailEmployee::getStoreId, bo.getStoreId());
        lqw.like(StringUtils.isNotBlank(bo.getPosition()), NailEmployee::getPosition, bo.getPosition());
        lqw.like(StringUtils.isNotBlank(bo.getGrade()), NailEmployee::getGrade, bo.getGrade());
        lqw.eq(bo.getCommissionRate() != null, NailEmployee::getCommissionRate, bo.getCommissionRate());
        lqw.eq(bo.getHireDate() != null, NailEmployee::getHireDate, bo.getHireDate());
        lqw.eq(bo.getUserId() != null, NailEmployee::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getSkill()), NailEmployee::getSkill, bo.getSkill());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailEmployee::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailEmployee::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailEmployee::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailEmployee");
        lqw.orderByDesc(NailEmployee::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailEmployeeBo bo) {
        NailEmployee add = MapstructUtils.convert(bo, NailEmployee.class);

        if (StringUtils.isBlank(add.getEmployeeNo())) {
            add.setEmployeeNo("YG" + System.currentTimeMillis());
        }
        org.dromara.nail.helper.NailDataScopeHelper.fillStoreId(add);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailEmployeeBo bo) {
        NailEmployee update = MapstructUtils.convert(bo, NailEmployee.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
