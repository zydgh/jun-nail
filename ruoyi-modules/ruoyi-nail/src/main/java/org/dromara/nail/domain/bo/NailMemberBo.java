package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailMember;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员业务对象 nail_member
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailMember.class, reverseConvertGenerate = false)
public class NailMemberBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 会员编号
     */
    private String memberNo;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 会员等级
     */
    private Long levelId;

    /**
     * 积分
     */
    private Integer point;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 累计充值
     */
    private BigDecimal totalRecharge;

    /**
     * 累计消费
     */
    private BigDecimal totalConsume;

    /**
     * 消费次数
     */
    private Integer consumeTimes;

    /**
     * 最近到店
     */
    private Date lastVisitTime;

    /**
     * 所属门店
     */
    private Long storeId;

    /**
     * 来源
     */
    private String source;

    /**
     * 标签
     */
    private String tags;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
