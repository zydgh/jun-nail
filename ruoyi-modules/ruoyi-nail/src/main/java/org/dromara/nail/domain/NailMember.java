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
 * 会员对象 nail_member
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_member")
public class NailMember extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 会员编号
     */
    private String memberNo;

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

    /**
     * 删除标志（0正常 1删除）
     */
    @TableLogic
    private String delFlag;

}
