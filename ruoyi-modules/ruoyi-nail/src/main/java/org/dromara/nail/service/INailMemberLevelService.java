package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailMemberLevelBo;
import org.dromara.nail.domain.vo.NailMemberLevelVo;

import java.util.Collection;
import java.util.List;

/**
 * 会员等级Service接口
 */
public interface INailMemberLevelService {

    NailMemberLevelVo queryById(Long id);

    TableDataInfo<NailMemberLevelVo> queryPageList(NailMemberLevelBo bo, PageQuery pageQuery);

    List<NailMemberLevelVo> queryList(NailMemberLevelBo bo);

    Boolean insertByBo(NailMemberLevelBo bo);

    Boolean updateByBo(NailMemberLevelBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
