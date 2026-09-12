package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailOrderBo;
import org.dromara.nail.domain.vo.NailOrderVo;

import java.util.Collection;
import java.util.List;

/**
 * 订单Service接口
 */
public interface INailOrderService {

    NailOrderVo queryById(Long id);

    TableDataInfo<NailOrderVo> queryPageList(NailOrderBo bo, PageQuery pageQuery);

    List<NailOrderVo> queryList(NailOrderBo bo);

    Boolean insertByBo(NailOrderBo bo);

    Boolean updateByBo(NailOrderBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
