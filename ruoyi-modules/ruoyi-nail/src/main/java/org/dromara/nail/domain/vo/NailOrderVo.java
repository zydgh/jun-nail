package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailOrder;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单视图对象 nail_order
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailOrder.class)
public class NailOrderVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 会员ID
     */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 美甲师ID
     */
    @ExcelProperty(value = "美甲师ID")
    private Long employeeId;

    /**
     * 门店ID
     */
    @ExcelProperty(value = "门店ID")
    private Long storeId;

    /**
     * 下单时间
     */
    @ExcelProperty(value = "下单时间")
    private Date orderTime;

    /**
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    private String payType;

    /**
     * 订单总额
     */
    @ExcelProperty(value = "订单总额")
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    @ExcelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    @ExcelProperty(value = "实付金额")
    private BigDecimal payAmount;

    /**
     * 赠送积分
     */
    @ExcelProperty(value = "赠送积分")
    private Integer pointAmount;

    /**
     * 订单状态
     */
    @ExcelProperty(value = "订单状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
