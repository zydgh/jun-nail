package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailMemberBalanceLog;


import java.math.BigDecimal;

/**
 * 余额变动业务对象 nail_member_balance_log
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailMemberBalanceLog.class, reverseConvertGenerate = false)
public class NailMemberBalanceLogBo extends BaseEntity {

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
     * 变动金额
     */
    @NotNull(message = "变动金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal amount;

    /**
     * 变动前余额
     */
    private BigDecimal beforeBalance;

    /**
     * 变动后余额
     */
    private BigDecimal afterBalance;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 备注
     */
    private String remark;

}
