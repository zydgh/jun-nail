package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailServiceItemBo;
import org.dromara.nail.domain.vo.NailServiceItemVo;

import java.util.Collection;
import java.util.List;

/**
 * 美甲项目Service接口
 */
public interface INailServiceItemService {

    NailServiceItemVo queryById(Long id);

    TableDataInfo<NailServiceItemVo> queryPageList(NailServiceItemBo bo, PageQuery pageQuery);

    List<NailServiceItemVo> queryList(NailServiceItemBo bo);

    Boolean insertByBo(NailServiceItemBo bo);

    Boolean updateByBo(NailServiceItemBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
