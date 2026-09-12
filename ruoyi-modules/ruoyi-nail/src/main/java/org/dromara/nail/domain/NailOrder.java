package org.dromara.nail.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单对象 nail_order
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_order")
public class NailOrder extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
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

    /**
     * 删除标志（0正常 1删除）
     */
    @TableLogic
    private String delFlag;

}
