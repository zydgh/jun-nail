package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailEmployeePerformance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工业绩业务对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailEmployeePerformance.class, reverseConvertGenerate = false)
public class NailEmployeePerformanceBo extends BaseEntity {

    @NotNull(message = "主键不能为空", groups = EditGroup.class)
    private Long id;

    private Long employeeId;

    private Long storeId;

    @NotNull(message = "业绩金额不能为空", groups = {AddGroup.class, EditGroup.class})
    @DecimalMin(value = "0.01", message = "业绩金额必须大于0", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal amount;

    @NotNull(message = "服务项目不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long itemId;

    private String itemName;

    @NotBlank(message = "客户姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String customerName;

    @NotBlank(message = "客户手机号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String customerPhone;

    @NotNull(message = "消费时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date consumeTime;

    private String praise;

    private String remark;
}
