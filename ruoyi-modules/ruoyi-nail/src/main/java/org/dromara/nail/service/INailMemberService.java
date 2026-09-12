package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailMemberBo;
import org.dromara.nail.domain.vo.NailMemberVo;

import java.util.Collection;
import java.util.List;

/**
 * 会员Service接口
 */
public interface INailMemberService {

    NailMemberVo queryById(Long id);

    TableDataInfo<NailMemberVo> queryPageList(NailMemberBo bo, PageQuery pageQuery);

    List<NailMemberVo> queryList(NailMemberBo bo);

    Boolean insertByBo(NailMemberBo bo);

    Boolean updateByBo(NailMemberBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
