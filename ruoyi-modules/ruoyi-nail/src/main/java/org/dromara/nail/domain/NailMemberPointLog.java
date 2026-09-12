package org.dromara.nail.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;


/**
 * 积分变动对象 nail_member_point_log
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_member_point_log")
public class NailMemberPointLog extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 变动类型
     */
    private String changeType;

    /**
     * 变动积分
     */
    private Integer point;

    /**
     * 变动前积分
     */
    private Integer beforePoint;

    /**
     * 变动后积分
     */
    private Integer afterPoint;

    /**
     * 业务单号
     */
    private String bizNo;

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
