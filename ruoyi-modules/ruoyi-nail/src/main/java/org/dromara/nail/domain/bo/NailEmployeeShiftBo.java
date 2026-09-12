package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailEmployeeShift;


import java.util.Date;

/**
 * 员工排班业务对象 nail_employee_shift
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailEmployeeShift.class, reverseConvertGenerate = false)
public class NailEmployeeShiftBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 员工ID
     */
    @NotNull(message = "员工ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long employeeId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 排班日期
     */
    @NotNull(message = "排班日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date shiftDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
