package org.dromara.nail.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.nail.domain.bo.NailEmployeePerformanceBo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceRankVo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceVo;
import org.dromara.nail.domain.vo.NailPerformanceMemberCheckVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 员工业绩服务
 */
public interface INailEmployeePerformanceService {

    NailEmployeePerformanceVo queryById(Long id);

    TableDataInfo<NailEmployeePerformanceVo> queryPageList(
        NailEmployeePerformanceBo bo, PageQuery pageQuery);

    List<NailEmployeePerformanceVo> queryList(NailEmployeePerformanceBo bo);

    List<NailEmployeePerformanceRankVo> ranking(String period);

    Map<String, Object> options();

    NailPerformanceMemberCheckVo checkMember(String phone, String name);

    Boolean insertByBo(NailEmployeePerformanceBo bo);

    Boolean updateByBo(NailEmployeePerformanceBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
