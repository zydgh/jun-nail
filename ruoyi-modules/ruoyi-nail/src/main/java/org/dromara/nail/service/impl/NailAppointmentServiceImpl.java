package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.NailAppointment;
import org.dromara.nail.domain.bo.NailAppointmentBo;
import org.dromara.nail.domain.vo.NailAppointmentVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailAppointmentMapper;
import org.dromara.nail.service.INailAppointmentService;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class NailAppointmentServiceImpl implements INailAppointmentService {

    private static final String STATUS_CANCELLED = "2";
    private static final int DEFAULT_DURATION_MIN = 60;

    private final NailAppointmentMapper baseMapper;

    @Override
    public NailAppointmentVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<NailAppointmentVo> queryPageList(NailAppointmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NailAppointment> lqw = buildQueryWrapper(bo);
        Page<NailAppointmentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<NailAppointmentVo> queryList(NailAppointmentBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<NailAppointment> buildQueryWrapper(NailAppointmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NailAppointment> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getAppointNo()), NailAppointment::getAppointNo, bo.getAppointNo());
        lqw.eq(bo.getMemberId() != null, NailAppointment::getMemberId, bo.getMemberId());
        lqw.eq(bo.getEmployeeId() != null, NailAppointment::getEmployeeId, bo.getEmployeeId());
        lqw.eq(bo.getStoreId() != null, NailAppointment::getStoreId, bo.getStoreId());
        lqw.eq(bo.getItemId() != null, NailAppointment::getItemId, bo.getItemId());
        lqw.eq(bo.getAppointTime() != null, NailAppointment::getAppointTime, bo.getAppointTime());
        lqw.eq(bo.getDurationMin() != null, NailAppointment::getDurationMin, bo.getDurationMin());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), NailAppointment::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), NailAppointment::getRemark, bo.getRemark());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            NailAppointment::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        NailDataScopeHelper.applyStoreScope(lqw, "NailAppointment");
        lqw.orderByDesc(NailAppointment::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(NailAppointmentBo bo) {
        NailAppointment add = MapstructUtils.convert(bo, NailAppointment.class);

        if (StringUtils.isBlank(add.getAppointNo())) {
            add.setAppointNo("YY" + System.currentTimeMillis());
        }
        if (StringUtils.isBlank(add.getStatus())) {
            add.setStatus("0");
        }
        if (add.getDurationMin() == null || add.getDurationMin() <= 0) {
            add.setDurationMin(DEFAULT_DURATION_MIN);
        }
        org.dromara.nail.helper.NailDataScopeHelper.fillStoreId(add);
        checkEmployeeTimeConflict(add);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(NailAppointmentBo bo) {
        NailAppointment update = MapstructUtils.convert(bo, NailAppointment.class);
        NailAppointmentVo exist = baseMapper.selectVoById(bo.getId());
        if (exist != null) {
            update.setAppointNo(exist.getAppointNo());
        }
        if (update.getDurationMin() == null || update.getDurationMin() <= 0) {
            update.setDurationMin(exist != null && exist.getDurationMin() != null
                ? exist.getDurationMin() : DEFAULT_DURATION_MIN);
        }
        checkEmployeeTimeConflict(update);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    private void checkEmployeeTimeConflict(NailAppointment appointment) {
        if (appointment.getEmployeeId() == null || appointment.getAppointTime() == null) {
            return;
        }
        if (STATUS_CANCELLED.equals(appointment.getStatus())) {
            return;
        }
        int duration = resolveDuration(appointment.getDurationMin());
        Date newStart = appointment.getAppointTime();
        Date newEnd = Date.from(newStart.toInstant().plus(duration, ChronoUnit.MINUTES));
        Date windowStart = Date.from(newStart.toInstant().minus(1, ChronoUnit.DAYS));

        LambdaQueryWrapper<NailAppointment> lqw = Wrappers.lambdaQuery();
        lqw.eq(NailAppointment::getEmployeeId, appointment.getEmployeeId());
        lqw.and(w -> w.isNull(NailAppointment::getStatus).or().ne(NailAppointment::getStatus, STATUS_CANCELLED));
        lqw.ne(appointment.getId() != null, NailAppointment::getId, appointment.getId());
        lqw.ge(NailAppointment::getAppointTime, windowStart);
        lqw.lt(NailAppointment::getAppointTime, newEnd);
        List<NailAppointment> candidates = baseMapper.selectList(lqw);
        for (NailAppointment exist : candidates) {
            if (exist.getAppointTime() == null) {
                continue;
            }
            int existDuration = resolveDuration(exist.getDurationMin());
            Date existStart = exist.getAppointTime();
            Date existEnd = Date.from(existStart.toInstant().plus(existDuration, ChronoUnit.MINUTES));
            if (newStart.before(existEnd) && existStart.before(newEnd)) {
                throw new ServiceException("该美甲师在此时段已有预约（" + exist.getAppointNo()
                    + "），同一时间段只能服务一位顾客，请调整时间或更换美甲师");
            }
        }
    }

    private int resolveDuration(Integer durationMin) {
        if (durationMin == null || durationMin <= 0) {
            return DEFAULT_DURATION_MIN;
        }
        return durationMin;
    }
}
