package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailAppointment;

import java.io.Serial;
import java.io.Serializable;

import java.util.Date;

/**
 * 预约视图对象 nail_appointment
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailAppointment.class)
public class NailAppointmentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 预约单号
     */
    @ExcelProperty(value = "预约单号")
    private String appointNo;

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
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long itemId;

    /**
     * 预约时间
     */
    @ExcelProperty(value = "预约时间")
    private Date appointTime;

    /**
     * 时长(分钟)
     */
    @ExcelProperty(value = "时长(分钟)")
    private Integer durationMin;

    /**
     * 预约状态
     */
    @ExcelProperty(value = "预约状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
