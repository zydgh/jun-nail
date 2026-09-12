package org.dromara.nail.service;

import org.dromara.nail.domain.bo.NailMemberRechargeBo;
import org.dromara.nail.domain.vo.NailMemberVo;

import java.util.List;

/**
 * 会员储值与积分
 */
public interface INailMemberWalletService {

    void recharge(NailMemberRechargeBo bo);

    void adjust(NailMemberRechargeBo bo);

    List<NailMemberVo> birthdayRemind(Integer days);
}
