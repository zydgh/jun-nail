package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailCommission;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工提成业务对象 nail_commission
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailCommission.class, reverseConvertGenerate = false)
public class NailCommissionBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 员工ID
     */
    @NotNull(message = "员工ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long employeeId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 提成比例
     */
    private BigDecimal commissionRate;

    /**
     * 提成金额
     */
    private BigDecimal commissionAmount;

    /**
     * 结算状态
     */
    private String status;

    /**
     * 结算时间
     */
    private Date settleTime;

    /**
     * 备注
     */
    private String remark;

}
