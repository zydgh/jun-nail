package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailEmployee;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工业务对象 nail_employee
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailEmployee.class, reverseConvertGenerate = false)
public class NailEmployeeBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 员工编号
     */
    private String employeeNo;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 所属门店
     */
    private Long storeId;

    /**
     * 职位
     */
    private String position;

    /**
     * 等级
     */
    private String grade;

    /**
     * 提成比例
     */
    private BigDecimal commissionRate;

    /**
     * 入职日期
     */
    private Date hireDate;

    /**
     * 关联用户
     */
    private Long userId;

    /**
     * 擅长项目
     */
    private String skill;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
