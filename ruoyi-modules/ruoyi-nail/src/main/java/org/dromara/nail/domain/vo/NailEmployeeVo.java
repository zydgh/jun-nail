package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.nail.domain.NailEmployee;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工视图对象 nail_employee
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailEmployee.class)
public class NailEmployeeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 员工编号
     */
    @ExcelProperty(value = "员工编号")
    private String employeeNo;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    private String sex;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    private String avatar;

    /**
     * 头像Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "avatar")
    private String avatarUrl;

    /**
     * 所属门店
     */
    @ExcelProperty(value = "所属门店")
    private Long storeId;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位")
    private String position;

    /**
     * 等级
     */
    @ExcelProperty(value = "等级")
    private String grade;

    /**
     * 提成比例
     */
    @ExcelProperty(value = "提成比例")
    private BigDecimal commissionRate;

    /**
     * 入职日期
     */
    @ExcelProperty(value = "入职日期")
    private Date hireDate;

    /**
     * 关联用户
     */
    @ExcelProperty(value = "关联用户")
    private Long userId;

    /**
     * 擅长项目
     */
    @ExcelProperty(value = "擅长项目")
    private String skill;

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
