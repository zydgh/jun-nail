package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailServiceItem;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

/**
 * 美甲项目视图对象 nail_service_item
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailServiceItem.class)
public class NailServiceItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 项目编号
     */
    @ExcelProperty(value = "项目编号")
    private String itemCode;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String itemName;

    /**
     * 分类
     */
    @ExcelProperty(value = "分类")
    private String category;

    /**
     * 时长(分钟)
     */
    @ExcelProperty(value = "时长(分钟)")
    private Integer durationMin;

    /**
     * 原价
     */
    @ExcelProperty(value = "原价")
    private BigDecimal price;

    /**
     * 会员价
     */
    @ExcelProperty(value = "会员价")
    private BigDecimal memberPrice;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
