package org.dromara.nail.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

import java.math.BigDecimal;

/**
 * 会员等级对象 nail_member_level
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_member_level")
public class NailMemberLevel extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 升级积分
     */
    private Integer upgradePoint;

    /**
     * 排序
     */
    private Integer sort;

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
