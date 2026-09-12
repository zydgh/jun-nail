package org.dromara.nail.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员充值/调账
 */
@Data
public class NailMemberRechargeBo {

    @NotNull(message = "会员不能为空")
    private Long memberId;

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    private BigDecimal giftAmount;

    private String remark;
}
