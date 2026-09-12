package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailMemberPointLog;

import java.io.Serial;
import java.io.Serializable;


/**
 * 积分变动视图对象 nail_member_point_log
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailMemberPointLog.class)
public class NailMemberPointLogVo implements Serializable {

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
     * 变动积分
     */
    @ExcelProperty(value = "变动积分")
    private Integer point;

    /**
     * 变动前积分
     */
    @ExcelProperty(value = "变动前积分")
    private Integer beforePoint;

    /**
     * 变动后积分
     */
    @ExcelProperty(value = "变动后积分")
    private Integer afterPoint;

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
