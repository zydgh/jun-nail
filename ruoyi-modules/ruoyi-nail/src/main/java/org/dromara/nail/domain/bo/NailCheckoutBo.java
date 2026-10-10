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

    private String customerName;

    private String customerPhone;

    private Long storeId;

    @NotNull(message = "项目不能为空")
    private Long itemId;

    private Integer quantity;

    private String payType;

    private BigDecimal amount;

    private BigDecimal discountAmount;

    private String praise;

    private String praiseImages;

    private String remark;
}
