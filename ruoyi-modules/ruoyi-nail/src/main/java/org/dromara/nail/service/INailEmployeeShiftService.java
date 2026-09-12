package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailEmployeeShiftBo;
import org.dromara.nail.domain.vo.NailEmployeeShiftVo;

import java.util.Collection;
import java.util.List;

/**
 * 员工排班Service接口
 */
public interface INailEmployeeShiftService {

    NailEmployeeShiftVo queryById(Long id);

    TableDataInfo<NailEmployeeShiftVo> queryPageList(NailEmployeeShiftBo bo, PageQuery pageQuery);

    List<NailEmployeeShiftVo> queryList(NailEmployeeShiftBo bo);

    Boolean insertByBo(NailEmployeeShiftBo bo);

    Boolean updateByBo(NailEmployeeShiftBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
