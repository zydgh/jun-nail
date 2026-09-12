package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailOrderItemBo;
import org.dromara.nail.domain.vo.NailOrderItemVo;

import java.util.Collection;
import java.util.List;

/**
 * 订单明细Service接口
 */
public interface INailOrderItemService {

    NailOrderItemVo queryById(Long id);

    TableDataInfo<NailOrderItemVo> queryPageList(NailOrderItemBo bo, PageQuery pageQuery);

    List<NailOrderItemVo> queryList(NailOrderItemBo bo);

    Boolean insertByBo(NailOrderItemBo bo);

    Boolean updateByBo(NailOrderItemBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
