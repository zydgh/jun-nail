package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailMemberLevel;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

/**
 * 会员等级视图对象 nail_member_level
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailMemberLevel.class)
public class NailMemberLevelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 等级名称
     */
    @ExcelProperty(value = "等级名称")
    private String levelName;

    /**
     * 折扣
     */
    @ExcelProperty(value = "折扣")
    private BigDecimal discount;

    /**
     * 升级积分
     */
    @ExcelProperty(value = "升级积分")
    private Integer upgradePoint;

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
