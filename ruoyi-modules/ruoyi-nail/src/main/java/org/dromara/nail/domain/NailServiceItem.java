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
 * 美甲项目对象 nail_service_item
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_service_item")
public class NailServiceItem extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目编号
     */
    private String itemCode;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 分类
     */
    private String category;

    /**
     * 时长(分钟)
     */
    private Integer durationMin;

    /**
     * 原价
     */
    private BigDecimal price;

    /**
     * 会员价
     */
    private BigDecimal memberPrice;

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
