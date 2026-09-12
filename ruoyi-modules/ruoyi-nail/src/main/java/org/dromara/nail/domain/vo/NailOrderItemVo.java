package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailOrderItem;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

/**
 * 订单明细视图对象 nail_order_item
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailOrderItem.class)
public class NailOrderItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long itemId;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String itemName;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Integer quantity;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal price;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    private BigDecimal amount;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
