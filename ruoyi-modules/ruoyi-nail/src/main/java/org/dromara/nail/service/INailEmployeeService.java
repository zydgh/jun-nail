package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailEmployeeBo;
import org.dromara.nail.domain.vo.NailEmployeeVo;

import java.util.Collection;
import java.util.List;

/**
 * 员工Service接口
 */
public interface INailEmployeeService {

    NailEmployeeVo queryById(Long id);

    TableDataInfo<NailEmployeeVo> queryPageList(NailEmployeeBo bo, PageQuery pageQuery);

    List<NailEmployeeVo> queryList(NailEmployeeBo bo);

    Boolean insertByBo(NailEmployeeBo bo);

    Boolean updateByBo(NailEmployeeBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
