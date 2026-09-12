package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailMemberBalanceLogBo;
import org.dromara.nail.domain.vo.NailMemberBalanceLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 余额变动Service接口
 */
public interface INailMemberBalanceLogService {

    NailMemberBalanceLogVo queryById(Long id);

    TableDataInfo<NailMemberBalanceLogVo> queryPageList(NailMemberBalanceLogBo bo, PageQuery pageQuery);

    List<NailMemberBalanceLogVo> queryList(NailMemberBalanceLogBo bo);

    Boolean insertByBo(NailMemberBalanceLogBo bo);

    Boolean updateByBo(NailMemberBalanceLogBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
