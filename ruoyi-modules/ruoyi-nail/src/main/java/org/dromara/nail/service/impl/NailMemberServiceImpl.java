package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailMember;
import org.dromara.nail.domain.bo.NailMemberBo;
import org.dromara.nail.domain.vo.NailMemberVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailMemberMapper;
import org.dromara.nail.service.INailMemberService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 会员Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailMemberServiceImpl implements INailMemberService {

    private final NailMemberMapper baseMapper;

    @Override
    public NailMemberVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailMemberVo> queryPageList(NailMemberBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailMember> lqw = buildQueryWrapper(bo);
        Page<NailMemberVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailMemberVo> queryList(NailMemberBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailMember> buildQueryWrapper(NailMemberBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailMember> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getMemberNo()), NailMember::getMemberNo, bo.getMemberNo());
        lqw.like(StringUtils.isNotBlank(bo.getName()), NailMember::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), NailMember::getPhone, bo.getPhone());
        lqw.like(StringUtils.isNotBlank(bo.getSex()), NailMember::getSex, bo.getSex());
        lqw.eq(bo.getBirthday() != null, NailMember::getBirthday, bo.getBirthday());
        lqw.eq(bo.getLevelId() != null, NailMember::getLevelId, bo.getLevelId());
        lqw.eq(bo.getPoint() != null, NailMember::getPoint, bo.getPoint());
        lqw.eq(bo.getBalance() != null, NailMember::getBalance, bo.getBalance());
        lqw.eq(bo.getTotalRecharge() != null, NailMember::getTotalRecharge, bo.getTotalRecharge());
        lqw.eq(bo.getTotalConsume() != null, NailMember::getTotalConsume, bo.getTotalConsume());
        lqw.eq(bo.getConsumeTimes() != null, NailMember::getConsumeTimes, bo.getConsumeTimes());
        lqw.eq(bo.getLastVisitTime() != null, NailMember::getLastVisitTime, bo.getLastVisitTime());
        lqw.eq(bo.getStoreId() != null, NailMember::getStoreId, bo.getStoreId());
        lqw.like(StringUtils.isNotBlank(bo.getSource()), NailMember::getSource, bo.getSource());
        lqw.like(StringUtils.isNotBlank(bo.getTags()), NailMember::getTags, bo.getTags());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailMember::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailMember::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailMember::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailMember");
        lqw.orderByDesc(NailMember::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailMemberBo bo) {
        NailMember add = MapstructUtils.convert(bo, NailMember.class);

        if (StringUtils.isBlank(add.getMemberNo())) {
            add.setMemberNo("HY" + System.currentTimeMillis());
        }
        if (add.getPoint() == null) {
            add.setPoint(0);
        }
        if (add.getBalance() == null) {
            add.setBalance(java.math.BigDecimal.ZERO);
        }
        if (add.getTotalRecharge() == null) {
            add.setTotalRecharge(java.math.BigDecimal.ZERO);
        }
        if (add.getTotalConsume() == null) {
            add.setTotalConsume(java.math.BigDecimal.ZERO);
        }
        if (add.getConsumeTimes() == null) {
            add.setConsumeTimes(0);
        }
        org.dromara.nail.helper.NailDataScopeHelper.fillStoreId(add);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailMemberBo bo) {
        NailMember update = MapstructUtils.convert(bo, NailMember.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
