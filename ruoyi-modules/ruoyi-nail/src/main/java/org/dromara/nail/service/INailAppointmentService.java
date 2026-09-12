package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailAppointmentBo;
import org.dromara.nail.domain.vo.NailAppointmentVo;

import java.util.Collection;
import java.util.List;

/**
 * 预约Service接口
 */
public interface INailAppointmentService {

    NailAppointmentVo queryById(Long id);

    TableDataInfo<NailAppointmentVo> queryPageList(NailAppointmentBo bo, PageQuery pageQuery);

    List<NailAppointmentVo> queryList(NailAppointmentBo bo);

    Boolean insertByBo(NailAppointmentBo bo);

    Boolean updateByBo(NailAppointmentBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
