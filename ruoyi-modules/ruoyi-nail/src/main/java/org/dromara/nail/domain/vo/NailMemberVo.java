package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailMember;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员视图对象 nail_member
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailMember.class)
public class NailMemberVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 会员编号
     */
    @ExcelProperty(value = "会员编号")
    private String memberNo;

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
     * 生日
     */
    @ExcelProperty(value = "生日")
    private Date birthday;

    /**
     * 会员等级
     */
    @ExcelProperty(value = "会员等级")
    private Long levelId;

    /**
     * 积分
     */
    @ExcelProperty(value = "积分")
    private Integer point;

    /**
     * 余额
     */
    @ExcelProperty(value = "余额")
    private BigDecimal balance;

    /**
     * 累计充值
     */
    @ExcelProperty(value = "累计充值")
    private BigDecimal totalRecharge;

    /**
     * 累计消费
     */
    @ExcelProperty(value = "累计消费")
    private BigDecimal totalConsume;

    /**
     * 消费次数
     */
    @ExcelProperty(value = "消费次数")
    private Integer consumeTimes;

    /**
     * 最近到店
     */
    @ExcelProperty(value = "最近到店")
    private Date lastVisitTime;

    /**
     * 所属门店
     */
    @ExcelProperty(value = "所属门店")
    private Long storeId;

    /**
     * 来源
     */
    @ExcelProperty(value = "来源")
    private String source;

    /**
     * 标签
     */
    @ExcelProperty(value = "标签")
    private String tags;

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
