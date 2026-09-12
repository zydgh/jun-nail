package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailStoreBo;
import org.dromara.nail.domain.vo.NailStoreVo;

import java.util.Collection;
import java.util.List;

/**
 * 门店Service接口
 */
public interface INailStoreService {

    NailStoreVo queryById(Long id);

    TableDataInfo<NailStoreVo> queryPageList(NailStoreBo bo, PageQuery pageQuery);

    List<NailStoreVo> queryList(NailStoreBo bo);

    Boolean insertByBo(NailStoreBo bo);

    Boolean updateByBo(NailStoreBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
