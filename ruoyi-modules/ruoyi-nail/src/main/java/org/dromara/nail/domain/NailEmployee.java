package org.dromara.nail.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工对象 nail_employee
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_employee")
public class NailEmployee extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工编号
     */
    private String employeeNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
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

    /**
     * 删除标志（0正常 1删除）
     */
    @TableLogic
    private String delFlag;

}
