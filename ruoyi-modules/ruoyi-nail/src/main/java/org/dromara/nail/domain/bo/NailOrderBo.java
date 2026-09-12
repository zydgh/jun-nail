package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailOrder;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单业务对象 nail_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailOrder.class, reverseConvertGenerate = false)
public class NailOrderBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 美甲师ID
     */
    private Long employeeId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 订单总额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 赠送积分
     */
    private Integer pointAmount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
