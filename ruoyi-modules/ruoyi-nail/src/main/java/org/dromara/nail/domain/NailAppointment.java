package org.dromara.nail.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

import java.util.Date;

/**
 * 预约对象 nail_appointment
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_appointment")
public class NailAppointment extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 预约单号
     */
    private String appointNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 美甲师ID
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
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

    /**
     * 删除标志（0正常 1删除）
     */
    @TableLogic
    private String delFlag;

}
