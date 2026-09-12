package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailCommission;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工提成视图对象 nail_commission
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailCommission.class)
public class NailCommissionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 员工ID
     */
    @ExcelProperty(value = "员工ID")
    private Long employeeId;

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 提成比例
     */
    @ExcelProperty(value = "提成比例")
    private BigDecimal commissionRate;

    /**
     * 提成金额
     */
    @ExcelProperty(value = "提成金额")
    private BigDecimal commissionAmount;

    /**
     * 结算状态
     */
    @ExcelProperty(value = "结算状态")
    private String status;

    /**
     * 结算时间
     */
    @ExcelProperty(value = "结算时间")
    private Date settleTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
