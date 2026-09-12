package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailMemberPointLog;



/**
 * 积分变动业务对象 nail_member_point_log
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailMemberPointLog.class, reverseConvertGenerate = false)
public class NailMemberPointLogBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 会员ID
     */
    @NotNull(message = "会员ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long memberId;

    /**
     * 变动类型
     */
    @NotBlank(message = "变动类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String changeType;

    /**
     * 变动积分
     */
    @NotNull(message = "变动积分不能为空", groups = { AddGroup.class, EditGroup.class })
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

}
