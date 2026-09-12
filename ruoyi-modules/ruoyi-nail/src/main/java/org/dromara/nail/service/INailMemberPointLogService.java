package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailMemberPointLogBo;
import org.dromara.nail.domain.vo.NailMemberPointLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 积分变动Service接口
 */
public interface INailMemberPointLogService {

    NailMemberPointLogVo queryById(Long id);

    TableDataInfo<NailMemberPointLogVo> queryPageList(NailMemberPointLogBo bo, PageQuery pageQuery);

    List<NailMemberPointLogVo> queryList(NailMemberPointLogBo bo);

    Boolean insertByBo(NailMemberPointLogBo bo);

    Boolean updateByBo(NailMemberPointLogBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
