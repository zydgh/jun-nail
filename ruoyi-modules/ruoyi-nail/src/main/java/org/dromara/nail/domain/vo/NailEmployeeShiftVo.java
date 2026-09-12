package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailEmployeeShift;

import java.io.Serial;
import java.io.Serializable;

import java.util.Date;

/**
 * 员工排班视图对象 nail_employee_shift
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailEmployeeShift.class)
public class NailEmployeeShiftVo implements Serializable {

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
     * 门店ID
     */
    @ExcelProperty(value = "门店ID")
    private Long storeId;

    /**
     * 排班日期
     */
    @ExcelProperty(value = "排班日期")
    private Date shiftDate;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private String endTime;

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
