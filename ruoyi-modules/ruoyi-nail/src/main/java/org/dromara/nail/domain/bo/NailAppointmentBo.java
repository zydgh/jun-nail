package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailAppointment;


import java.util.Date;

/**
 * 预约业务对象 nail_appointment
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailAppointment.class, reverseConvertGenerate = false)
public class NailAppointmentBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 预约单号
     */
    private String appointNo;

    /**
     * 会员ID
     */
    @NotNull(message = "会员ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long memberId;

    /**
     * 美甲师ID，可空；有值时同一时段只能服务一位顾客
     */
    private Long employeeId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 项目ID
     */
    private Long itemId;

    /**
     * 预约时间
     */
    @NotNull(message = "预约时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date appointTime;

    /**
     * 时长(分钟)
     */
    private Integer durationMin;

    /**
     * 预约状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
