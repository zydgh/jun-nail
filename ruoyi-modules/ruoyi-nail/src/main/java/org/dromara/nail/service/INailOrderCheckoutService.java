package org.dromara.nail.service;

import org.dromara.nail.domain.bo.NailCheckoutBo;
import org.dromara.nail.domain.vo.NailOrderVo;

/**
 * 收银结算
 */
public interface INailOrderCheckoutService {

    NailOrderVo checkout(NailCheckoutBo bo);
}
