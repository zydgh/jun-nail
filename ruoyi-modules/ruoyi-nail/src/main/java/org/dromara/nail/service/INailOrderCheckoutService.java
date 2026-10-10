package org.dromara.nail.service;

import org.dromara.nail.domain.bo.NailCheckoutBo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceRankVo;
import org.dromara.nail.domain.vo.NailOrderVo;
import org.dromara.nail.domain.vo.NailPerformanceMemberCheckVo;

import java.util.List;
import java.util.Map;

/**
 * 收银结算
 */
public interface INailOrderCheckoutService {

    NailOrderVo checkout(NailCheckoutBo bo);

    Map<String, Object> options();

    NailPerformanceMemberCheckVo checkMember(String phone, String name);

    List<NailEmployeePerformanceRankVo> ranking(String period);
}
