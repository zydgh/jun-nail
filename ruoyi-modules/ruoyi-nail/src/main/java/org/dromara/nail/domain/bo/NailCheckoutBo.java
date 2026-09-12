package org.dromara.nail.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收银下单
 */
@Data
public class NailCheckoutBo {

    private Long memberId;

    private Long employeeId;

    private Long storeId;

    @NotNull(message = "项目不能为空")
    private Long itemId;

    @NotNull(message = "数量不能为空")
    private Integer quantity;

    private String payType;

    private BigDecimal discountAmount;

    private String remark;
}
