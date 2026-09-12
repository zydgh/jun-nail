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
 * 员工提成对象 nail_commission
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_commission")
public class NailCommission extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工ID
     */
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

    /**
     * 删除标志（0正常 1删除）
     */
    @TableLogic
    private String delFlag;

}
