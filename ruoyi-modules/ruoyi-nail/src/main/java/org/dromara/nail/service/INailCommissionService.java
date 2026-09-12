package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailCommissionBo;
import org.dromara.nail.domain.vo.NailCommissionVo;

import java.util.Collection;
import java.util.List;

/**
 * 员工提成Service接口
 */
public interface INailCommissionService {

    NailCommissionVo queryById(Long id);

    TableDataInfo<NailCommissionVo> queryPageList(NailCommissionBo bo, PageQuery pageQuery);

    List<NailCommissionVo> queryList(NailCommissionBo bo);

    Boolean insertByBo(NailCommissionBo bo);

    Boolean updateByBo(NailCommissionBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
