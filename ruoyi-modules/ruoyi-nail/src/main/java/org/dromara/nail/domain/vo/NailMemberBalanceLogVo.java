package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailMemberBalanceLog;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

/**
 * 余额变动视图对象 nail_member_balance_log
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailMemberBalanceLog.class)
public class NailMemberBalanceLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 会员ID
     */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 变动类型
     */
    @ExcelProperty(value = "变动类型")
    private String changeType;

    /**
     * 变动金额
     */
    @ExcelProperty(value = "变动金额")
    private BigDecimal amount;

    /**
     * 变动前余额
     */
    @ExcelProperty(value = "变动前余额")
    private BigDecimal beforeBalance;

    /**
     * 变动后余额
     */
    @ExcelProperty(value = "变动后余额")
    private BigDecimal afterBalance;

    /**
     * 业务单号
     */
    @ExcelProperty(value = "业务单号")
    private String bizNo;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
